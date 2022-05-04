package project.library;

import project.library.books.Author;
import project.library.books.Book;
import project.library.books.fiction.Novel;
import project.library.books.nonfiction.Autobiography;
import project.library.books.nonfiction.Dictionary;
import project.library.books.nonfiction.Textbook;
import project.library.books.poetry.Drama;
import project.library.members.LibraryMember;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class LibraryMemoryService implements LibraryService {

    LinkedHashSet<Book> libraryBooks = new LinkedHashSet<>();
    SortedSet<LibraryMember> libraryMembers = new TreeSet<>();
    Set<Author> bookAuthors = new HashSet<>();

    @Override
    public void addBooks(Book... books) {
        LinkedHashSet<Book> booksSet = new LinkedHashSet<>(Arrays.asList(books));
        //libraryBooks.addAll(booksSet);
        booksSet.forEach(book -> { libraryBooks.add(book);
            if (Objects.nonNull(book.getAuthors()))
                bookAuthors.addAll(book.getAuthors()); });
    }

    @Override
    public void addMembers(LibraryMember... members) {
        int lastId = 0;
        if(libraryMembers.size() > 0) {
            lastId = libraryMembers.last().getId();
        }
        for(LibraryMember member : members) {
            if (libraryMembers.contains(member))
                continue;
            lastId ++;
            member.setId(lastId);
            libraryMembers.add(member);
        }
    }

    @Override
    public LinkedHashSet<Book> getAllBooksSortedAlphabetically() {
        return libraryBooks.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<Author> getAllAuthors() {
        return bookAuthors;
    }

    @Override
    public Set<LibraryMember> getAllMembers() {
        return libraryMembers;
    }

    @Override
    public LibraryMember getMemberById(int id) {
        Optional<LibraryMember> optionalMember = libraryMembers.stream()
                .filter(member -> member.getId()==id)
                .findFirst();
        return optionalMember.orElse(null);
    }

    @Override
    public void addBorrowedBooksToMember(int memberId, Book... books) {
        LibraryMember member = getMemberById(memberId);
        if (Objects.nonNull(member)) {
            for (Book book : books) {
                if (libraryBooks.contains(book)) {
                    if (member.getBorrowedBooks().size() < member.getMaxBooksAllowed()) {
                        member.borrowBook(book); }
                } else
                    System.out.println("The book titled '"+book.getTitle()+"' that you tried to borrow " +
                            "does not exist in the library.");
            }
        }
    }

    @Override
    public void returnBorrowedBooks(int memberId, Book... returnedBooks) {
        LibraryMember member = getMemberById(memberId);
        if (Objects.nonNull(member)) {
            member.returnBooks(returnedBooks);
        }
    }

    @Override
    public void deleteMembers(LibraryMember... membersToDelete) {
        //Arrays.asList(membersToDelete).forEach(this.libraryMembers::remove);
        Arrays.asList(membersToDelete).forEach(member -> {
            if (libraryMembers.contains(member)) {
                if (member.getBorrowedBooks().size() > 0)
                    System.out.println("Cannot delete member with id "+member.getId()+" - still has borrowed books.");
                else {
                    libraryMembers.remove(member);
                    System.out.println("Deleted member with id " + member.getId() + " successfully.");
                }
            } else
                System.out.println("The member with id "+member.getId()+" does not exist.");
        });
    }

    @Override
    public void deleteBooks(Book... booksToDelete) {
        Arrays.asList(booksToDelete).forEach(book -> { book.getAuthors().stream()
                .filter(this::authorHasAtMostOneBook)
                .forEach(author -> bookAuthors.remove(author));
            libraryBooks.remove(book);
        });
    }

    @Override
    public boolean authorHasAtMostOneBook(Author author) {
        return getBooksByAuthor(author).size() <= 1;
    }

    @Override
    public Set<LibraryMember> getMembersWithExpiredMembership() {
        return getAllMembers().stream()
                .filter(member -> LocalDate.now().compareTo(member.getMembershipExpires()) > 0)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LibraryMember> getMembersByCustomFilter(Predicate<LibraryMember> filter) {
        return libraryMembers.stream()
                .filter(filter)
                .collect(Collectors.toSet());
    }

    @Override
    public LinkedHashSet<Book> getBooksByTitle(String title) {
        return libraryBooks.stream()
                .filter(book -> book.getTitle().equals(title))
                .collect(Collectors.toCollection(LinkedHashSet::new));
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
                .filter(book -> {
                    if (nonNull(book.getSubcategory()))
                        return book.getSubcategory().equalsIgnoreCase(subcategory);
                    else return false;
                })
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
    public LinkedHashSet<Book> getNovelsByGenre(String genre) {
        Set<Book> books = getBooksBySubcategory("Novel");
        return books.stream()
                .map(book -> (Novel) book)
                .filter(novel -> nonNull(novel.getGenres()))
                .filter(novel -> novel.getGenres().stream().
                        map(String::toLowerCase).collect(Collectors.toList()).contains(genre.toLowerCase()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Book> getDramasByGenre(String dramaGenre) {
        return getBooksBySubcategory("Drama").stream()
                .map(book -> (Drama) book)
                .filter(drama -> {
                    if (nonNull(drama.getDramaGenre()))
                        return drama.getDramaGenre().equalsIgnoreCase(dramaGenre);
                    else return false;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Book> getTextbooksByDomainAndYears(String domain, int startYear, int endYear) {
        return getBooksBySubcategory("textbook").stream()
                .map(book -> (Textbook) book)
                .filter(textbook -> nonNull(textbook.getDomain()) && nonNull(textbook.getDatePublished()))
                .filter(textbook -> textbook.getDomain().equalsIgnoreCase(domain))
                .filter(textbook -> textbook.getDatePublished().getYear() >= startYear
                        && textbook.getDatePublished().getYear() <= endYear)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Book> getDictionariesByLanguages(String writtenIn, String targetLanguage) {
        Set<Book> books = getBooksBySubcategory("dictionary");
        return books.stream()
                .map(book -> (Dictionary) book)
                .filter(dict -> nonNull(dict.getLanguage()) && nonNull(dict.getTargetLanguage()))
                .filter(dict -> dict.getLanguage().equalsIgnoreCase(writtenIn)
                        && dict.getTargetLanguage().equalsIgnoreCase(targetLanguage))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Book> findBooksByCustomFilter(Predicate<Book> filter) {
        return libraryBooks.stream()
                .filter(filter)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<Author> getAuthorsByMinNumOfBooks(int min) {
        return bookAuthors.stream()
                .filter(author -> getBooksByAuthor(author).size() >= min)
                .collect(Collectors.toSet());
    }

    public HashMap<Author, Book> getAuthorsWithAutobiography() {
        Set<Book> books = getBooksBySubcategory("autobiography");
        HashMap<Author, Book> hashmap = new HashMap<>();
        books = books.stream().filter(book -> nonNull(book.getAuthors()))
                .collect(Collectors.toSet());
        books.forEach(book -> book.getAuthors().stream()
                .sorted(Comparator.comparing(Author::getName))
                .forEach(author -> hashmap.put(author, book)));
        return hashmap;
    }

    @Override
    public Set<Author> getAuthorByCustomFilter(Predicate<Author> filter) {
        return bookAuthors.stream()
                .filter(filter)
                .collect(Collectors.toSet());
    }

}
