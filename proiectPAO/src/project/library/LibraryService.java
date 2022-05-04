package project.library;

import project.library.books.Author;
import project.library.books.Book;
import project.library.members.LibraryMember;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

public interface LibraryService {
    void addBooks(Book... books);

    void addMembers(LibraryMember... members);

    LinkedHashSet<Book> getAllBooksSortedAlphabetically();

    Set<Author> getAllAuthors();

    Set<LibraryMember> getAllMembers();

    void deleteBooks(Book... booksToDelete);

    boolean authorHasAtMostOneBook(Author author);

    void deleteMembers(LibraryMember... membersToDelete);

    LibraryMember getMemberById(int id);

    void addBorrowedBooksToMember(int memberId, Book... books);

    void returnBorrowedBooks(int memberId, Book... returnedBooks);

    Set<LibraryMember> getMembersWithExpiredMembership();

    Set<LibraryMember> getMembersByCustomFilter(Predicate<LibraryMember> filter);

    LinkedHashSet<Book> getBooksByTitle(String title);

    LinkedHashSet<Book> getBooksByCategory(String category);

    LinkedHashSet<Book> getBooksBySubcategory(String subcategory);

    LinkedHashSet<Book> getBooksByAuthor(Author author);

    LinkedHashSet<Book> getBooksByAuthorName(String authorName);

    LinkedHashSet<Book> getNovelsByGenre(String genre);

    LinkedHashSet<Book> getDramasByGenre(String dramaGenre);

    LinkedHashSet<Book> getTextbooksByDomainAndYears(String domain, int startYear, int endYear);

    LinkedHashSet<Book> getDictionariesByLanguages(String writtenIn, String targetLanguage);

    LinkedHashSet<Book> findBooksByCustomFilter(Predicate<Book> filter);

    Set<Author> getAuthorsByMinNumOfBooks(int min);

    HashMap<Author, Book> getAuthorsWithAutobiography();

    Set<Author> getAuthorByCustomFilter(Predicate<Author> filter);
}
