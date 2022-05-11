package project.library;

import project.library.books.Author;
import project.library.books.Book;
import project.library.books.fiction.FictionBook;
import project.library.books.fiction.Novel;
import project.library.books.nonfiction.NonfictionBook;
import project.library.books.poetry.Poetry;
import project.library.members.Address;
import project.library.members.LibraryMember;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LibraryCsvService implements LibraryService {

    private final File booksCsvFile;
    private final File authorsCsvFile;
    private final File membersCsvFile;
    private final File borrowedBooksCsvFile;
    private final File novelsCsvFile;

    private void checkFileExists(File file) {
        if(!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.out.println(file + " could not be created.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public LibraryCsvService() {
        booksCsvFile = new File("src/project/library/BooksCsvFile.csv");
        authorsCsvFile = new File("src/project/library/AuthorsCsvFile.csv");
        membersCsvFile = new File("src/project/library/MembersCsvFile.csv");
        borrowedBooksCsvFile = new File("src/project/library/BorrowedBooksCsvFile.csv");
        novelsCsvFile = new File("src/project/library/NovelsCsvFile.csv");
        checkFileExists(booksCsvFile);
        checkFileExists(authorsCsvFile);
        checkFileExists(membersCsvFile);
        checkFileExists(borrowedBooksCsvFile);
        checkFileExists(novelsCsvFile);
    }

    @Override
    public void addBooks(Book... booksToAdd) {
        try (FileWriter fileWriter1 = new FileWriter(booksCsvFile, true)) {
            BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
            LinkedHashSet<Book> allBooks = getAllBooksSortedAlphabetically();
            int lastBookId = 0;
            if (allBooks.size() > 0) {
                Book[] bookArray = new Book[allBooks.size()];
                bookArray = allBooks.toArray(bookArray);
                lastBookId = bookArray[0].getId();
            }
            for (Book book : booksToAdd) {
                if (Objects.nonNull(book.getAuthors())) {
                    try (FileWriter fileWriter2 = new FileWriter(authorsCsvFile, true)) {
                        BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
                        SortedSet<Author> allAuthors = getAllAuthors();
                        int lastId = 0;
                        if (allAuthors.size() > 0)
                            lastId = allAuthors.last().getId();
                        for (Author author : book.getAuthors()) {
                            boolean authorAlreadyExists = allAuthors.stream()
                                    .anyMatch(storedAuthor -> storedAuthor.equals(author));
                            if (authorAlreadyExists)
                                continue;
                            lastId++;
                            author.setId(lastId);
                            bufferedWriter2.write(authorFormatForCsv(author));
                        }
                        bufferedWriter2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                boolean bookAlreadyExists = allBooks.stream()
                        .anyMatch(storedBook -> (Objects.equals(storedBook.getTitle(), book.getTitle())
                                && Objects.equals(storedBook.getAuthors(), book.getAuthors())
                                && Objects.equals(storedBook.getDatePublished(), book.getDatePublished())));
                if (bookAlreadyExists)
                    continue;
                lastBookId++;
                book.setId(lastBookId);
                bufferedWriter1.write(bookFormatForCsv(book));
                if (Objects.equals(book.getSubcategory(), "Novel"))
                    writeNovelToFile((Novel) book);
            }
            bufferedWriter1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeNovelToFile(Novel book) {
        StringJoiner genres = new StringJoiner(",");
        if (nonNull(book.getGenres())) {
            book.getGenres().forEach(genres::add);
        }
        if (genres.length() == 0)
            genres.add("null");
        String novel =  book.getId() + "," + book.getTitle() + "," + book.getDatePublished() + "," +
                book.getCategory() + "," + book.getSubcategory() + "," + book.getYearWritten() + "," +
                book.getOriginalLanguage() + "," + book.getTranslatedInto() + "," + genres;
        try (FileWriter fileWriter = new FileWriter(novelsCsvFile, true)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(novel + "\n");
            bufferedWriter.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedHashSet<Novel> getAllNovels() {
        try (FileReader fileReader = new FileReader(novelsCsvFile)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return bufferedReader.lines()
                    .map(this::getNovelFromCsvFile)
                    .sorted(Comparator.comparing(Novel::getTitle))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedHashSet<>();
    }

    private Novel getNovelFromCsvFile(String line) {
        String[] values = line.split(",");
        LocalDate datePublished = null;
        if (!values[2].equals("null"))
            datePublished = LocalDate.parse(values[2]);

        Set<String> genres;
        if (values[8].equals("null"))
            genres = null;
        else {
            genres = new HashSet<>(Arrays.asList(values).subList(8, values.length));
        }
        Novel novel = Novel.newNovel().title(values[1]).datePublished(datePublished)
                .originalLanguage(values[6]).translatedInto(values[7])
                .genres(genres).buildNovel();
        novel.setId(Integer.parseInt(values[0]));
        return novel;
    }

    private String bookFormatForCsv(Book book) {
        StringJoiner authors = new StringJoiner(",");
        if (nonNull(book.getAuthors())) {
            book.getAuthors().forEach(author -> authors.add(Integer.toString(author.getId())));
        }
        if (authors.length() == 0)
            authors.add("null");
        return book.getId() + "," + book.getTitle() + "," + book.getDatePublished() + "," +
                book.getCategory() + "," + book.getSubcategory() + "," + authors + "\n";
    }

    private String authorFormatForCsv(Author author) {
        return author.getId() + "," + author.getName() + "," + author.getCountry() + "," +
                author.getDateBorn() + "," + author.getDateDied() + "\n";
    }

    @Override
    public void addMembers(LibraryMember... membersToAdd) {
        try (FileWriter fileWriter1 = new FileWriter(membersCsvFile, true)) {
            BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
            SortedSet<LibraryMember> allMembers = getAllMembers();
            int lastId = 0;
            if (allMembers.size() > 0)
                lastId = allMembers.last().getId();
            for (LibraryMember member : membersToAdd) {
                boolean memberAlreadyExists = allMembers.stream()
                        .anyMatch(storedMember -> storedMember.equals(member));
                if (memberAlreadyExists)
                    continue;
                lastId++;
                member.setId(lastId);
                bufferedWriter1.write(memberFormatForCsv(member));
                allMembers.add(member);
            }
            bufferedWriter1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String memberFormatForCsv(LibraryMember member) {
        String memberAddress = "null";
        if (nonNull(member.getAddress())) {
            Address address = member.getAddress();
            memberAddress = address.getCountry() + "," + address.getCity() + "," + address.getStreet();
        }
        return member.getId() + "," + member.getName() + "," +
                member.getEmail() + "," + member.getPhone() + "," +
                member.getMembershipStarted() + "," + member.getMembershipExpires() + "," + memberAddress + "\n";
    }

    @Override
    public SortedSet<LibraryMember> getAllMembers() {
        try (FileReader fileReader = new FileReader(membersCsvFile)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return bufferedReader.lines()
                    .map(this::getMemberFromCsvLine)
                    .collect(Collectors.toCollection(TreeSet::new));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TreeSet<>();
    }

    private LibraryMember getMemberFromCsvLine(String line) {
        String[] values = line.split(",");
        Address address;
        if (values[6].equals("null"))
            address = null;
        else
            address = new Address(values[6], values[7], values[8]);
        ArrayList<Book> borrowedBooks = new ArrayList<>();
        try (FileReader fileReader = new FileReader(borrowedBooksCsvFile)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line=bufferedReader.readLine())!=null) {
                String[] lineValues = line.split(",");
                if (Objects.equals(lineValues[0], values[0])) {
                    LinkedHashSet<Book> books = getBooksByTitle(lineValues[1]);
                    borrowedBooks.addAll(books);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book[] books = new Book[borrowedBooks.size()];
        LibraryMember member = LibraryMember.newMember()
                .name(values[1])
                .email(values[2])
                .phone(values[3])
                .membershipStarted(LocalDate.parse(values[4]))
                .membershipExpires(LocalDate.parse(values[5]))
                .address(address)
                .borrowedBooks(borrowedBooks.toArray(books))
                .buildMember();
        member.setId(Integer.parseInt(values[0]));
        return member;
    }

    @Override
    public LinkedHashSet<Book> getAllBooksSortedAlphabetically() {
        try (FileReader fileReader = new FileReader(booksCsvFile)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return bufferedReader.lines()
                    .map(this::getBookFromCsvLine)
                    .sorted(Comparator.comparing(Book::getTitle))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedHashSet<>();
    }

    private Book getBookFromCsvLine(String line) {
        String[] values = line.split(",");
        LocalDate datePublished = null;
        if (!values[2].equals("null"))
            datePublished = LocalDate.parse(values[2]);
        Set<Author> authors = new HashSet<>();
        if (values[5].equals("null"))
            authors = null;
        else {
            for (int i=5; i<values.length; i++) {
                authors.add(getAuthorById(Integer.parseInt(values[i])));
            }
        }
        Book book;
        if (values[3].equals("Nonfiction"))
            book = NonfictionBook.newNonfictionBook().title(values[1]).authors(authors)
                    .datePublished(datePublished).buildNonfictionBook();

        else if (values[3].equals("Poetry"))
            book = Poetry.newPoetryBook().title(values[1]).authors(authors)
                    .datePublished(datePublished).buildPoetryBook();

        else book = FictionBook.newFictionBook().title(values[1]).authors(authors)
                .datePublished(datePublished).buildFictionBook();
        book.setId(Integer.parseInt(values[0]));
        return book;
    }

    @Override
    public SortedSet<Author> getAllAuthors() {
        try (FileReader fileReader = new FileReader(authorsCsvFile)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            return bufferedReader.lines()
                    .map(this::getAuthorFromCsvLine)
                    .collect(Collectors.toCollection(TreeSet::new));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new TreeSet<>();
    }

    private Author getAuthorFromCsvLine(String line) {
        String[] values = line.split(",");
        LocalDate dateBorn = null;
        LocalDate dateDied = null;
        if (!values[3].equals("null"))
            dateBorn = LocalDate.parse(values[3]);
        if (!values[4].equals("null"))
            dateDied = LocalDate.parse(values[4]);
        Author author = Author.newAuthor()
                .name(values[1])
                .country(values[2])
                .dateBorn(dateBorn)
                .dateDied(dateDied)
                .buildAuthor();
        author.setId(Integer.parseInt(values[0]));
        return author;
    }

    public Author getAuthorById(int id) {
        Optional<Author> authorOptional = getAllAuthors().stream()
                .filter(diploma -> diploma.getId() == id)
                .findFirst();
        return authorOptional.orElse(null);
    }

    public Book getBookById(int id) {
        Optional<Book> bookOptional = getAllBooksSortedAlphabetically().stream()
                .filter(book -> book.getId() == id)
                .findFirst();
        return bookOptional.orElse(null);
    }

    @Override
    public void deleteBooks(Book... booksToDelete) {
        LinkedHashSet<Book> remainingBooks = new LinkedHashSet<>();
        for (Book book : booksToDelete) {
            List<Book> books = getAllBooksSortedAlphabetically().stream()
                    .filter(storedBook -> !Objects.equals(storedBook.getTitle(), book.getTitle())
                            || !Objects.equals(storedBook.getAuthors(), book.getAuthors())
                            || !Objects.equals(storedBook.getDatePublished(), book.getDatePublished()))
                    .sorted(Comparator.comparing(Book::getId))
                    .collect(Collectors.toList());
            remainingBooks.addAll(books);

            book.getAuthors().stream()
                    .filter(this::authorHasAtMostOneBook)
                    .forEach(this::deleteAuthor);
        }
        try(FileWriter fileWriter = new FileWriter(booksCsvFile, false)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(Book book : remainingBooks) {
                bufferedWriter.write(bookFormatForCsv(book));
            }
            bufferedWriter.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAuthor(Author authorToDelete) {
        List<Author> remainingAuthors = getAllAuthors().stream()
                .filter(storedAuthor -> !storedAuthor.equals(authorToDelete))
                .collect(Collectors.toList());

        try(FileWriter fileWriter = new FileWriter(authorsCsvFile, false)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(Author author : remainingAuthors) {
                bufferedWriter.write(authorFormatForCsv(author));
            }
            bufferedWriter.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean authorHasAtMostOneBook(Author author) {
        return getBooksByAuthor(author).size() <= 1;
    }

    @Override
    public LinkedHashSet<Book> getBooksByTitle(String title) {
        return getAllBooksSortedAlphabetically().stream()
                .filter(book -> book.getTitle().equals(title))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Book> getBooksByAuthor(Author author) {
        return getAllBooksSortedAlphabetically().stream()
                .filter(book -> nonNull(book.getAuthors()))
                .filter(book -> book.getAuthors().contains(author))
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
    public LinkedHashSet<Book> getBooksByCategory(String category) {
        return getAllBooksSortedAlphabetically().stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LibraryMember getMemberById(int id) {
        Optional<LibraryMember> optionalMember = getAllMembers().stream()
                .filter(member -> member.getId()==id)
                .findFirst();
        return optionalMember.orElse(null);
    }

    @Override
    public Set<LibraryMember> getMembersWithExpiredMembership() {
        return getAllMembers().stream()
                .filter(member -> LocalDate.now().compareTo(member.getMembershipExpires()) > 0)
                .collect(Collectors.toSet());
    }

    @Override
    public void addBorrowedBooksToMember(int memberId, Book... books) {
        LibraryMember member = getMemberById(memberId);
        if (Objects.nonNull(member)) {
            for (Book book : books) {
                try (FileWriter fileWriter = new FileWriter(borrowedBooksCsvFile, true)) {
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    if (member.getBorrowedBooks().size() < member.getMaxBooksAllowed()) {
                        //member.borrowBook(book);
                        bufferedWriter.write(member.getId() + "," + book.getTitle() + "\n");
                    }
                bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void returnBorrowedBooks(int memberId, Book... returnedBooks) {
//        LibraryMember member = getMemberById(memberId);
//        if (Objects.nonNull(member)) {
//            member.returnBooks(returnedBooks);
//        }
        for (Book book : returnedBooks) {
            try(FileReader fileReader = new FileReader(borrowedBooksCsvFile)) {
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                List<String[]> remaining = bufferedReader.lines()
                        .map(line -> line.split(","))
                        .filter(values -> Integer.parseInt(values[0]) != memberId ||
                                values[1].equals(book.getTitle()))
                        .collect(Collectors.toList());
                try(FileWriter fileWriter = new FileWriter(borrowedBooksCsvFile, false)) {
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    for(String[] line : remaining) {
                        bufferedWriter.write(line[0]+","+line[1]+"\n");
                    }
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void deleteMembers(LibraryMember... membersToDelete) {
        List<Integer> memberIds = new ArrayList<>();
        try(FileReader fileReader = new FileReader(borrowedBooksCsvFile)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            memberIds = bufferedReader.lines()
                    .map(line -> line.split(","))
                    .map(values -> Integer.parseInt(values[0]))
                    .collect(Collectors.toList());
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        LinkedHashSet<LibraryMember> remainingMembers = new LinkedHashSet<>();
        try (FileWriter fileWriter = new FileWriter(membersCsvFile, false)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            List<Integer> finalMemberIds = memberIds;
            Arrays.asList(membersToDelete).forEach(memberToDelete -> {
                if (finalMemberIds.contains(memberToDelete.getId()))
                    System.out.println("Cannot delete member with id "+memberToDelete.getId()+" - still has borrowed books.");
                else {
                    List<LibraryMember> members = getAllMembers().stream()
                            .filter(storedMember -> !Objects.equals(storedMember.getName(), memberToDelete.getName()))
                            .sorted(Comparator.comparing(LibraryMember::getId))
                            .collect(Collectors.toList());
                    remainingMembers.addAll(members);
                }
            });
            for (LibraryMember member : remainingMembers) {
                bufferedWriter.write(memberFormatForCsv(member));
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public LinkedHashSet<Book> getNovelsByGenre(String genre) {
        return getAllNovels().stream()
                .filter(novel -> nonNull(novel.getGenres()))
                .filter(novel -> novel.getGenres().stream().
                        map(String::toLowerCase).collect(Collectors.toList()).contains(genre.toLowerCase()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }





    @Override
    public LinkedHashSet<Book> getBooksBySubcategory(String subcategory) {
        return null;
    }

    @Override
    public Set<LibraryMember> getMembersByCustomFilter(Predicate<LibraryMember> filter) {
        return null;
    }

    @Override
    public LinkedHashSet<Book> getDramasByGenre(String dramaGenre) {
        return null;
    }

    @Override
    public LinkedHashSet<Book> getTextbooksByDomainAndYears(String domain, int startYear, int endYear) {
        return null;
    }

    @Override
    public LinkedHashSet<Book> getDictionariesByLanguages(String writtenIn, String targetLanguage) {
        return null;
    }

    @Override
    public LinkedHashSet<Book> findBooksByCustomFilter(Predicate<Book> filter) {
        return null;
    }

    @Override
    public Set<Author> getAuthorsByMinNumOfBooks(int min) {
        return null;
    }

    @Override
    public HashMap<Author, Book> getAuthorsWithAutobiography() {
        return null;
    }

    @Override
    public Set<Author> getAuthorByCustomFilter(Predicate<Author> filter) {
        return null;
    }
}
