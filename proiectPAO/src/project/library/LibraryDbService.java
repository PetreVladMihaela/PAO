package project.library;

import project.library.books.Author;
import project.library.books.Book;
import project.library.books.fiction.FictionBook;
import project.library.books.fiction.Novel;
import project.library.books.nonfiction.Dictionary;
import project.library.books.nonfiction.NonfictionBook;
import project.library.books.nonfiction.Textbook;
import project.library.books.poetry.Drama;
import project.library.books.poetry.Poetry;
import project.library.members.Address;
import project.library.members.LibraryMember;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LibraryDbService implements LibraryService {

    private Connection connection;

    public LibraryDbService() {
        try {
            this.connection = DbConnection.getInstance();
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void addBooks(Book... booksToAdd) {
        String queryBooks = "insert into books values (null, ?, ?, ?, ?, ?, ?, ?, ?)";
        String queryAuthors = "insert into authors values (null, ?, ?, ?, ?)";
        String queryLastBookId = "select max(id) from books";
        String queryLastAuthorId = "select max(id) from authors";
        String query = "insert into book_authors values (?, ?)";
        String queryNovels = "insert into novels values (?, ?, ?, ?)";
        String queryDramas = "insert into dramas values (?, ?, ?, ?)";
        String queryTextbooks = "insert into textbooks values (?, ?, ?, ?, ?)";
        String queryDictionaries = "insert into dictionaries values (?, ?, ?, ?, ?)";

        for (Book book : booksToAdd) {
            LinkedHashSet<Book> allBooks = getBooksByTitle(book.getTitle());
            boolean bookAlreadyExists;
            if (book.getAuthors() == null)
                bookAlreadyExists = allBooks.stream()
                        .anyMatch(storedBook -> (Objects.equals(storedBook.getTitle(), book.getTitle())
                                && Objects.equals(storedBook.getDatePublished(), book.getDatePublished())));
            else
                bookAlreadyExists = allBooks.stream()
                    .anyMatch(storedBook -> (Objects.equals(storedBook.getTitle(), book.getTitle())
                            && Objects.equals(storedBook.getAuthors(), book.getAuthors())
                            && Objects.equals(storedBook.getDatePublished(), book.getDatePublished())));
            if (bookAlreadyExists)
                continue;
            int lastBookId = 0;
            try(PreparedStatement preparedStatement = connection.prepareStatement(queryBooks)) {
                preparedStatement.setString(1, book.getTitle());
                if (book.getDatePublished() == null)
                    preparedStatement.setDate(2, null);
                else
                    preparedStatement.setDate(2, Date.valueOf(book.getDatePublished()));
                preparedStatement.setInt(3, book.getCopiesInLibrary());
                preparedStatement.setString(4, book.getCategory());
                preparedStatement.setString(5, book.getSubcategory());

                if (book.getCategory().equals("Fiction")) {
                    FictionBook fictionBook = (FictionBook) book;
                    preparedStatement.setInt(6, fictionBook.getYearWritten());
                    preparedStatement.setString(7, fictionBook.getOriginalLanguage());
                    preparedStatement.setString(8, fictionBook.getTranslatedInto());
                }
                else if (book.getCategory().equals("Nonfiction")) {
                    NonfictionBook nonfictionBook = (NonfictionBook) book;
                    preparedStatement.setNull(6, java.sql.Types.NULL);
                    preparedStatement.setString(7, nonfictionBook.getLanguage());
                    preparedStatement.setString(8, null);
                }
                else { //category == poetry
                    Poetry poetryBook = (Poetry) book;
                    preparedStatement.setNull(6, java.sql.Types.NULL);
                    preparedStatement.setString(7, poetryBook.getOriginalLanguage());
                    preparedStatement.setString(8, poetryBook.getTranslatedInto());
                }
                preparedStatement.executeUpdate();

                PreparedStatement pstmt1 = connection.prepareStatement(queryLastBookId);
                ResultSet result = pstmt1.executeQuery();
                result.next();
                lastBookId = result.getInt(1);
                pstmt1.close();

                if (book.getSubcategory() != null) {
                   switch (book.getSubcategory()) {
                        case "Novel":
                            if (book instanceof Novel) {
                                Novel novel = (Novel) book;
                                PreparedStatement pstmt2 = connection.prepareStatement(queryNovels);
                                pstmt2.setInt(1, lastBookId);
                                pstmt2.setString(2, book.getTitle());
                                if (nonNull(novel.getGenres())) {
                                    StringJoiner genres = new StringJoiner(";");
                                    novel.getGenres().forEach(genres::add);
                                    pstmt2.setString(3, String.valueOf(genres));
                                } else
                                    pstmt2.setString(3, null);
                                if (nonNull(novel.getThemes())) {
                                    StringJoiner themes = new StringJoiner(";");
                                    novel.getThemes().forEach(themes::add);
                                    pstmt2.setString(4, String.valueOf(themes));
                                } else
                                    pstmt2.setString(4, null);
                                pstmt2.executeUpdate();
                                pstmt2.close();
                            }
                            break;
                        case "Drama":
                            if (book instanceof Drama) {
                                Drama drama = (Drama) book;
                                PreparedStatement pstmt2 = connection.prepareStatement(queryDramas);
                                pstmt2.setInt(1, lastBookId);
                                pstmt2.setString(2, book.getTitle());
                                pstmt2.setString(3, drama.getDramaGenre());
                                if (nonNull(drama.getThemes())) {
                                    StringJoiner themes = new StringJoiner(";");
                                    drama.getThemes().forEach(themes::add);
                                    pstmt2.setString(4, String.valueOf(themes));
                                } else
                                    pstmt2.setString(4, null);
                                pstmt2.executeUpdate();
                                pstmt2.close();
                            }
                            break;
                        case "Textbook":
                            if (book instanceof Textbook) {
                                Textbook textbook = (Textbook) book;
                                PreparedStatement pstmt2 = connection.prepareStatement(queryTextbooks);
                                pstmt2.setInt(1, lastBookId);
                                pstmt2.setString(2, book.getTitle());
                                pstmt2.setString(3, textbook.getDomain());
                                pstmt2.setString(4, textbook.getSubject());
                                pstmt2.setString(5, textbook.getLevel());
                                pstmt2.executeUpdate();
                                pstmt2.close();
                            }
                            break;
                        case "Dictionary":
                            if (book instanceof Dictionary) {
                                Dictionary dict = (Dictionary) book;
                                PreparedStatement pstmt2 = connection.prepareStatement(queryDictionaries);
                                pstmt2.setInt(1, lastBookId);
                                pstmt2.setString(2, book.getTitle());
                                pstmt2.setString(3, dict.getType());
                                pstmt2.setString(4, dict.getTargetLanguage());
                                pstmt2.setString(5, dict.getField());
                                pstmt2.executeUpdate();
                                pstmt2.close();
                            }
                            break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (book.getAuthors() != null) {
                for(Author author : book.getAuthors()) {
                    Optional<Author> optAuthor = getAllAuthors().stream()
                            .filter(storedAuthor -> storedAuthor.equals(author))
                            .findFirst();
                    int authorId = 0;
                    if (optAuthor.isPresent()) {
                        authorId = optAuthor.get().getId();
                    } else {
                        try(PreparedStatement preparedStatement = connection.prepareStatement(queryAuthors)) {
                            preparedStatement.setString(1, author.getName());
                            preparedStatement.setString(2, author.getCountry());
                            if (author.getDateBorn() == null)
                                preparedStatement.setDate(3, null);
                            else
                                preparedStatement.setDate(3, Date.valueOf(author.getDateBorn()));
                            if (author.getDateDied() == null)
                                preparedStatement.setDate(4, null);
                            else
                                preparedStatement.setDate(4, Date.valueOf(author.getDateDied()));
                            preparedStatement.executeUpdate();

                            PreparedStatement pstmt = connection.prepareStatement(queryLastAuthorId);
                            ResultSet result = pstmt.executeQuery();
                            result.next();
                            authorId = result.getInt(1);
                            pstmt.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (lastBookId > 0 && authorId > 0) {
                        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setInt(1, lastBookId);
                            preparedStatement.setInt(2, authorId);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
       }
    }

    public Set<Author> getBookAuthors(int bookId) {
        String queryBookAuthors = "select authorId from book_authors where bookId = ?";
        String queryAuthor = "select * from authors where id = ?";
        Set<Author> authors = new HashSet<>();
        try(PreparedStatement pstmt1 = connection.prepareStatement(queryBookAuthors)) {
            pstmt1.setInt(1, bookId);
            ResultSet results = pstmt1.executeQuery();
            PreparedStatement pstmt2 = connection.prepareStatement(queryAuthor);
            while(results.next()) {
                int authorId = results.getInt("authorId");
                pstmt2.setInt(1, authorId);
                ResultSet resultAuthor = pstmt2.executeQuery();
                resultAuthor.next();
                String name = resultAuthor.getString("name");
                String country = resultAuthor.getString("country");
                Date dateBorn = resultAuthor.getDate("dateBorn");
                Date dateDied = resultAuthor.getDate("dateDied");
                LocalDate dateBornLocalDate = null;
                if (dateBorn != null)
                    dateBornLocalDate = new Date(dateBorn.getTime()).toLocalDate();
                LocalDate dateDiedLocalDate = null;
                if (dateDied != null)
                    dateDiedLocalDate = new Date(dateDied.getTime()).toLocalDate();
                Author author = Author.newAuthor().name(name).country(country)
                        .dateBorn(dateBornLocalDate).dateDied(dateDiedLocalDate)
                        .buildAuthor();
                author.setId(resultAuthor.getInt("id"));
                authors.add(author);
            }
            pstmt2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }


    @Override
    public LinkedHashSet<Book> getAllBooksSortedAlphabetically() {
        LinkedHashSet<Book> books = new LinkedHashSet<>();
        String queryAllBooks = "select * from books order by title";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryAllBooks)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int bookId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Date datePublished = resultSet.getDate("datePublished");
                int copiesInLibrary = resultSet.getInt("copiesInLibrary");
                String category = resultSet.getString("category");
                String subcategory = resultSet.getString("subcategory");
                int yearWritten = resultSet.getInt("yearWritten");
                String originalLanguage = resultSet.getString("originalLanguage");
                String translatedInto = resultSet.getString("translatedInto");

                LocalDate localDatePublished = null;
                if (datePublished != null)
                    localDatePublished = new Date(datePublished.getTime()).toLocalDate();

                Set<Author> authors = getBookAuthors(bookId);

                Book book;
                if (category.equals("Nonfiction"))
                    book = NonfictionBook.newNonfictionBook().title(title).authors(authors)
                            .datePublished(localDatePublished).copiesInLibrary(copiesInLibrary)
                            .language(originalLanguage).buildNonfictionBook();
                else if (category.equals("Poetry"))
                    book = Poetry.newPoetryBook().title(title).authors(authors)
                            .datePublished(localDatePublished).copiesInLibrary(copiesInLibrary)
                            .originalLanguage(originalLanguage).translatedInto(translatedInto)
                            .buildPoetryBook();
                else book = FictionBook.newFictionBook().title(title).authors(authors)
                            .datePublished(localDatePublished).copiesInLibrary(copiesInLibrary)
                            .yearWritten(yearWritten).originalLanguage(originalLanguage)
                            .translatedInto(translatedInto).buildFictionBook();
                book.setId(bookId);
                book.setSubcategory(subcategory);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public LinkedHashSet<Book> getBooksByTitle(String title) {
        return getAllBooksSortedAlphabetically().stream()
                .filter(book -> book.getTitle().equals(title))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Book> getBooksByAuthorName(String authorName) {
        return getAllBooksSortedAlphabetically().stream()
                .filter(book -> nonNull(book.getAuthors()))
                .filter(book -> book.getAuthors().stream()
                        .map(Author::getName).collect(Collectors.toList()).contains(authorName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<Author> getAllAuthors() {
        Set<Author> authors = new HashSet<>();
        String query = "select * from authors";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String name = resultSet.getString("name");
                String country = resultSet.getString("country");
                Date dateBorn = resultSet.getDate("dateBorn");
                Date dateDied = resultSet.getDate("dateDied");
                LocalDate dateBornLocalDate = null;
                if (dateBorn != null)
                    dateBornLocalDate = new Date(dateBorn.getTime()).toLocalDate();
                LocalDate dateDiedLocalDate = null;
                if (dateDied != null)
                    dateDiedLocalDate = new Date(dateDied.getTime()).toLocalDate();
                Author author = Author.newAuthor().name(name).country(country)
                        .dateBorn(dateBornLocalDate).dateDied(dateDiedLocalDate)
                        .buildAuthor();
                author.setId(resultSet.getInt("id"));
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public void deleteBooks(Book... booksToDelete) {
        for (Book book : booksToDelete) {
            Optional<Book> optBookToDelete = getBooksByTitle(book.getTitle()).stream()
                    .filter(storedBook -> (Objects.equals(storedBook.getTitle(), book.getTitle())
                            && Objects.equals(storedBook.getAuthors(), book.getAuthors())
                            && Objects.equals(storedBook.getDatePublished(), book.getDatePublished())))
                    .findFirst();
            if(optBookToDelete.isPresent()) {
                Book bookToDelete = optBookToDelete.get();
                String query = "delete from books where id = " + bookToDelete.getId();
                try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.executeUpdate();
                    bookToDelete.getAuthors().stream()
                            .filter(author -> getBooksByAuthor(author).size() == 0)
                            .forEach(this::deleteAuthor);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public LinkedHashSet<Book> getBooksByAuthor(Author author) {
        return getAllBooksSortedAlphabetically().stream()
                .filter(book -> nonNull(book.getAuthors()))
                .filter(book -> book.getAuthors().contains(author))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void deleteAuthor(Author authorToDelete) {
        String query = "delete from authors where id = " + authorToDelete.getId();
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addMembers(LibraryMember... members) {
        String queryInsertMember = "insert into library_members values (null, ?, ?, ?, ?, ?, ?)";
        String queryInsertAddress = "insert into addresses values (null, ?, ?, ?)";
        String queryLastId = "select max(id) from addresses";
        for (LibraryMember member : members) {
            boolean memberAlreadyExists = getAllMembers().stream()
                    .anyMatch(storedMember -> storedMember.equals(member));
            if (memberAlreadyExists)
                continue;
            try(PreparedStatement preparedStatement1 = connection.prepareStatement(queryInsertAddress)) {
                PreparedStatement preparedStatement2 = connection.prepareStatement(queryInsertMember);
                if (member.getAddress() != null) {
                    preparedStatement1.setString(1, member.getAddress().getCountry());
                    preparedStatement1.setString(2, member.getAddress().getCity());
                    preparedStatement1.setString(3, member.getAddress().getStreet());
                    preparedStatement1.executeUpdate();

                    PreparedStatement pstmt = connection.prepareStatement(queryLastId);
                    ResultSet result = pstmt.executeQuery();
                    result.next();
                    int lastAddressId = result.getInt(1);
                    pstmt.close();
                    preparedStatement2.setInt(6, lastAddressId);
                } else
                    preparedStatement2.setNull(6, java.sql.Types.NULL);
                preparedStatement2.setString(1, member.getName());
                preparedStatement2.setString(2, member.getEmail());
                preparedStatement2.setString(3, member.getPhone());
                preparedStatement2.setDate(4, Date.valueOf(member.getMembershipStarted()));
                preparedStatement2.setDate(5, Date.valueOf(member.getMembershipExpires()));
                preparedStatement2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<LibraryMember> getAllMembers() {
        Set<LibraryMember> members = new HashSet<>();
        String queryMembers = "select * from library_members order by id";
        try(PreparedStatement preparedStatement1 = connection.prepareStatement(queryMembers)) {
            ResultSet resultSet = preparedStatement1.executeQuery();
            while(resultSet.next()) {
                int memberId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                Date membershipStarted = resultSet.getDate("membershipStarted");
                Date membershipExpires = resultSet.getDate("membershipExpires");

                int addressId = resultSet.getInt("address_id");
                String queryAddress = "select * from addresses where id = " + addressId;
                PreparedStatement preparedStatement2 = connection.prepareStatement(queryAddress);
                ResultSet result = preparedStatement2.executeQuery();
                Address address = null;
                if(result.next()) {
                    String country = result.getString("country");
                    String city = result.getString("city");
                    String street = result.getString("street");
                    address = new Address(country, city, street);
                }
                preparedStatement2.close();

                LinkedList<Book> borrowedBooks = new LinkedList<>();
                String query = "select bookId from borrowed_books where memberId = " + memberId;
                PreparedStatement preparedStatement3 = connection.prepareStatement(query);
                ResultSet results = preparedStatement3.executeQuery();
                while (results.next()) {
                    Book book = getBookById(results.getInt("bookId"));
                    if (book != null)
                        borrowedBooks.add(book);
                }

                Book[] books = new Book[borrowedBooks.size()];
                LibraryMember member = LibraryMember.newMember()
                        .name(name).email(email).phone(phone)
                        .membershipStarted(new Date(membershipStarted.getTime()).toLocalDate())
                        .membershipExpires(new Date(membershipExpires.getTime()).toLocalDate())
                        .address(address).borrowedBooks(borrowedBooks.toArray(books))
                        .buildMember();
                member.setId(memberId);
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public Book getBookById(int id) {
        Optional<Book> optionalBook = getAllBooksSortedAlphabetically().stream()
                .filter(book -> book.getId() == id)
                .findFirst();
        return optionalBook.orElse(null);
    }

    @Override
    public LibraryMember getMemberById(int id) {
        Optional<LibraryMember> optionalMember = getAllMembers().stream()
                .filter(member -> member.getId() == id)
                .findFirst();
        return optionalMember.orElse(null);
    }

    @Override
    public void addBorrowedBooksToMember(int memberId, Book... booksToBorrow) {
        LibraryMember member = getMemberById(memberId);
        if (Objects.nonNull(member)) {
            String queryInsert = "insert into borrowed_books values (?, ?)";
            String queryUpdate = "update books set copiesInLibrary = ? where id = ?";
            for (Book book : booksToBorrow) {
                Optional<Book> bookOpt = getAllBooksSortedAlphabetically().stream()
                        .filter(storedBook -> (Objects.equals(storedBook.getTitle(), book.getTitle())
                                && Objects.equals(storedBook.getAuthors(), book.getAuthors())
                                && Objects.equals(storedBook.getDatePublished(), book.getDatePublished())))
                        .findFirst();
                if (bookOpt.isPresent()) {
                    Book bookToBorrow = bookOpt.get();
                    LinkedList<Book> borrowedBooks = member.getBorrowedBooks();
                    boolean bookAlreadyBorrowed = borrowedBooks.stream()
                            .map(Book::getId)
                            .anyMatch(id -> id == bookToBorrow.getId());
                    if (bookAlreadyBorrowed)
                        continue;
                    if (borrowedBooks.size() < member.getMaxBooksAllowed()) {
                        if (bookToBorrow.getCopiesInLibrary() > 0) {
                            try(PreparedStatement preparedStatement1 = connection.prepareStatement(queryInsert)) {
                                preparedStatement1.setInt(1, member.getId());
                                preparedStatement1.setInt(2, bookToBorrow.getId());
                                preparedStatement1.executeUpdate();

                                PreparedStatement preparedStatement2 = connection.prepareStatement(queryUpdate);
                                preparedStatement2.setInt(1, bookToBorrow.getCopiesInLibrary()-1);
                                preparedStatement2.setInt(2, bookToBorrow.getId());
                                preparedStatement2.executeUpdate();
                                preparedStatement2.close();
                                book.setCopiesInLibrary(book.getCopiesInLibrary()-1);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else
                            System.out.println("There are no more copies of "+book.getTitle()+" in the library.");
                    }
                    else
                        System.out.println(member.getName()+" cannot have more than 3 borrowed books.");
                }
            }
        }
    }

    @Override
    public void returnBorrowedBooks(int memberId, Book... returnedBooks) {
        String queryDelete = "delete from borrowed_books where memberId="+memberId+" and bookId=?";
        String queryUpdate = "update books set copiesInLibrary = ? where id = ?";
        for (Book book : returnedBooks) {
            Optional<Book> bookToReturn = getAllBooksSortedAlphabetically().stream()
                    .filter(storedBook -> (Objects.equals(storedBook.getTitle(), book.getTitle())
                            && Objects.equals(storedBook.getAuthors(), book.getAuthors())
                            && Objects.equals(storedBook.getDatePublished(), book.getDatePublished())))
                    .findFirst();
            if (bookToReturn.isPresent()) {
                try(PreparedStatement preparedStatement1 = connection.prepareStatement(queryDelete)) {
                    preparedStatement1.setInt(1, bookToReturn.get().getId());
                    int n = preparedStatement1.executeUpdate();
                    if (n > 0) {
                        PreparedStatement preparedStatement2 = connection.prepareStatement(queryUpdate);
                        preparedStatement2.setInt(1,  bookToReturn.get().getCopiesInLibrary()+1);
                        preparedStatement2.setInt(2, bookToReturn.get().getId());
                        preparedStatement2.executeUpdate();
                        preparedStatement2.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deleteMembers(LibraryMember... membersToDelete) {
        for (LibraryMember member : membersToDelete) {
            Optional<LibraryMember> memberOpt = getAllMembers().stream()
                    .filter(m -> m.equals(member))
                    .findFirst();
            if(memberOpt.isPresent()) {
                LibraryMember memberToDelete = memberOpt.get();
                if(memberToDelete.getBorrowedBooks().size() > 0)
                    System.out.println("Member with id "+memberToDelete.getId()+" still has borrowed books-cannot delete");
                else {
                    String query = "delete from library_members where id = " + memberToDelete.getId();
                    try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public Set<LibraryMember> getMembersWithExpiredMembership() {
        return getAllMembers().stream()
                .filter(member -> LocalDate.now().compareTo(member.getMembershipExpires()) > 0)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Author> getAuthorsByMinNumOfBooks(int min) {
        return getAllAuthors().stream()
                .filter(author -> getBooksByAuthor(author).size() >= min)
                .collect(Collectors.toSet());
    }

    @Override
    public LinkedHashSet<Book> getBooksByCategory(String category) {
        return getAllBooksSortedAlphabetically().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    @Override
    public LinkedHashSet<Book> getBooksBySubcategory(String subcategory) {
         return getAllBooksSortedAlphabetically().stream()
                .filter(book -> book.getSubcategory().equalsIgnoreCase(subcategory))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public HashMap<Author, Book> getAuthorsWithAutobiography() {
        HashMap<Author, Book> hashmap = new HashMap<>();
        LinkedHashSet<Book> books = getBooksBySubcategory("autobiography").stream().
                filter(book -> nonNull(book.getAuthors()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        books.forEach(book -> book.getAuthors()
                .forEach(author -> hashmap.put(author, book)));
        return hashmap;
    }

    @Override
    public LinkedHashSet<Book> getNovelsByGenre(String genre) {
        LinkedHashSet<Book> novels = new LinkedHashSet<>();
        String query = "select * from books inner join novels on books.id = novels.bookId " +
                "where genres like '%"+genre+"%' order by title";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int bookId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Date datePublished = resultSet.getDate("datePublished");
                int copiesInLibrary = resultSet.getInt("copiesInLibrary");
                int yearWritten = resultSet.getInt("yearWritten");
                String originalLanguage = resultSet.getString("originalLanguage");
                String translatedInto = resultSet.getString("translatedInto");
                String[] genres = resultSet.getString("genres").split(";");

                Set<Author> authors = getBookAuthors(bookId);

                LocalDate localDatePublished = null;
                if (datePublished != null)
                    localDatePublished = new Date(datePublished.getTime()).toLocalDate();

                Novel novel = Novel.newNovel().title(title).authors(authors)
                        .datePublished(localDatePublished).copiesInLibrary(copiesInLibrary)
                        .originalLanguage(originalLanguage).translatedInto(translatedInto)
                        .yearWritten(yearWritten).genres(new HashSet<>(Arrays.asList(genres)))
                        .buildNovel();
                if (resultSet.getString("themes") != null) {
                    String[] themes = resultSet.getString("themes").split(";");
                    novel.setThemes(new HashSet<>(Arrays.asList(themes)));
                }
                novel.setId(bookId);
                novels.add(novel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return novels;
    }

    @Override
    public LinkedHashSet<Book> getDramasByGenre(String dramaGenre) {
        LinkedHashSet<Book> dramas = new LinkedHashSet<>();
        String query = "select * from books inner join dramas on books.id = dramas.bookId " +
                "where dramaGenre like '"+dramaGenre+"' order by title";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int bookId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Date datePublished = resultSet.getDate("datePublished");
                int copiesInLibrary = resultSet.getInt("copiesInLibrary");
                String originalLanguage = resultSet.getString("originalLanguage");
                String translatedInto = resultSet.getString("translatedInto");

                Set<Author> authors = getBookAuthors(bookId);
                LocalDate localDatePublished = null;
                if (datePublished != null)
                    localDatePublished = new Date(datePublished.getTime()).toLocalDate();

                Drama drama = Drama.newDrama().title(title).authors(authors)
                        .datePublished(localDatePublished).copiesInLibrary(copiesInLibrary)
                        .originalLanguage(originalLanguage).translatedInto(translatedInto)
                        .dramaGenre(dramaGenre).buildDrama();
                if (resultSet.getString("themes") != null) {
                    String[] themes = resultSet.getString("themes").split(";");
                    drama.setThemes(new HashSet<>(Arrays.asList(themes)));
                }
                drama.setId(bookId);
                dramas.add(drama);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dramas;
    }

    @Override
    public LinkedHashSet<Book> getDictionariesByLanguages(String writtenIn, String targetLanguage) {
        LinkedHashSet<Book> dictionaries = new LinkedHashSet<>();
        String query = "select * from books inner join dictionaries on books.id = dictionaries.bookId where " +
                "originalLanguage like '"+writtenIn+"' and targetLanguage like '"+targetLanguage+"' order by title";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int bookId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Date datePublished = resultSet.getDate("datePublished");
                int copiesInLibrary = resultSet.getInt("copiesInLibrary");
                String language = resultSet.getString("originalLanguage");
                String type = resultSet.getString("type");
                String field = resultSet.getString("field");

                Set<Author> authors = getBookAuthors(bookId);
                LocalDate localDatePublished = null;
                if (datePublished != null)
                    localDatePublished = new Date(datePublished.getTime()).toLocalDate();

                Dictionary dictionary = Dictionary.newDictionary().title(title).authors(authors)
                        .datePublished(localDatePublished).copiesInLibrary(copiesInLibrary)
                        .language(language).targetLanguage(targetLanguage)
                        .type(type).field(field).buildDictionary();
                dictionary.setId(bookId);
                dictionaries.add(dictionary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dictionaries;
    }

    @Override
    public LinkedHashSet<Book> getTextbooksByDomainAndYears(String domain, int startYear, int endYear) {
        LinkedHashSet<Book> textbooks = new LinkedHashSet<>();
        String query = "select * from books inner join textbooks on books.id = textbooks.bookId where (domain like '"
                +domain+"') and (year(datePublished) between '"+startYear+"' and '"+endYear+"') order by title";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int bookId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Date datePublished = resultSet.getDate("datePublished");
                int copiesInLibrary = resultSet.getInt("copiesInLibrary");
                String language = resultSet.getString("originalLanguage");
                String subject = resultSet.getString("subject");
                String level = resultSet.getString("level");

                Set<Author> authors = getBookAuthors(bookId);
                LocalDate localDatePublished = null;
                if (datePublished != null)
                    localDatePublished = new Date(datePublished.getTime()).toLocalDate();

                Textbook textbook = Textbook.newTextbook().title(title).authors(authors)
                        .datePublished(localDatePublished).copiesInLibrary(copiesInLibrary)
                        .language(language).domain(domain).subject(subject)
                        .level(level).buildTextbook();
                textbook.setId(bookId);
                textbooks.add(textbook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return textbooks;
    }
}
