package project;

import project.library.LibraryDbService;
import project.library.books.Author;
import project.library.books.Book;
import project.library.books.fiction.Novel;
import project.library.books.fiction.ShortStories;
import project.library.books.nonfiction.Autobiography;
import project.library.books.nonfiction.Biography;
import project.library.books.nonfiction.Dictionary;
import project.library.books.nonfiction.Textbook;
import project.library.books.poetry.Drama;
import project.library.members.Address;
import project.library.members.LibraryMember;
import project.library.LibraryService;
import project.library.LibraryMemoryService;
import project.library.LibraryCsvService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MainProject {
    public static void printBooksSet(LinkedHashSet<Book> books) {
        String delimiter = ";\n\n";
        String prefix = "[";
        String suffix = "]";

        if (books.isEmpty()) {
            System.out.println("No match found.");
        }
        else {
            String str = books.stream().map(Objects::toString)
                    .collect(Collectors.joining(delimiter, prefix, suffix));
            System.out.println(str);
        }
    }
    public static void printMembersSet(Set<LibraryMember> members) {
        String delimiter = ";\n\n";
        String prefix = "[";
        String suffix = "]";

        if (members.isEmpty()) {
            System.out.println("No match found.");
        }
        else {
            String str = members.stream().map(Objects::toString)
                    .collect(Collectors.joining(delimiter, prefix, suffix));
            System.out.println(str+"\n");
        }
    }

    public static void main(String[] args) {
        LibraryService libraryCsvService = new LibraryCsvService();
        LibraryService libraryDbService = new LibraryDbService();

        Author author1 = Author.newAuthor()
                .name("Alexandre Dumas")
                .country("France")
                .dateBorn(LocalDate.parse("1802-07-24"))
                .dateDied(LocalDate.parse("1870-12-05"))
                .buildAuthor();
        Novel novel1 = Novel.newNovel()
                .title("The Three Musketeers")
                .yearWritten(1844)
                .authors(new HashSet<>(Collections.singletonList(author1)))
                .datePublished(LocalDate.parse("2012-09-25"))
                .copiesInLibrary(10)
                .originalLanguage("French")
                .translatedInto("English")
                .genres(new HashSet<>(Arrays.asList("Adventure", "Historical")))
                .themes(new HashSet<>(Arrays.asList("Friendship", "Loyalty", "Honor", "Vengeance")))
                .buildNovel();
        //System.out.println(novel1);

        Author author2 = Author.newAuthor().name("Stephen King").buildAuthor();
        Author author3 = Author.newAuthor().name("Peter Straub").buildAuthor();
        Set<Author> authorsSet = new HashSet<>(Arrays.asList(author2, author3));
        Novel novel2 = Novel.newNovel()
                .title("The Talisman")
                .authors(authorsSet)
                .copiesInLibrary(5)
                .yearWritten(1984)
                .genres(new HashSet<>(Arrays.asList("Dark Fantasy", "Adventure")))
                .buildNovel();
        //System.out.println(novel2);

        ShortStories shortStories1 = ShortStories.newShortStories()
                .title("Night Shift")
                .authors(Collections.singleton(author2))
                .yearWritten(1978)
                .storiesType("Horror Stories")
                .buildShortStories();
        //System.out.println(shortStories1);

        Author author4 = Author.newAuthor().name("William Shakespeare").buildAuthor();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
//        Date testDate = formatter.parse("16-Feb-2008");
//        System.out.println(testDate);
        Drama drama1 = Drama.newDrama()
                .title("Hamlet")
                .authors(new HashSet<>(Collections.singletonList(author4)))
                .copiesInLibrary(5)
                .datePublished(LocalDate.parse("2008-02-16"))
                .originalLanguage("Early Modern English")
                .dramaGenre("Tragedy")
                .themes(new HashSet<>(Arrays.asList("Revenge", "Madness")))
                .buildDrama();
        //System.out.println(drama1);


        libraryDbService.addBooks(novel2, novel1, shortStories1, drama1);
        System.out.println("All the books currently in the db sorted alphabetically:");
        printBooksSet(libraryDbService.getAllBooksSortedAlphabetically());

        libraryDbService.deleteBooks(shortStories1, novel1);
        libraryDbService.deleteBooks(novel1, drama1);
        System.out.println("\nTesting - deleting 3 books:");
        printBooksSet(libraryDbService.getAllBooksSortedAlphabetically());

        libraryDbService.addBooks(novel2, novel1, shortStories1, drama1);
        System.out.println("\nPoetry books from the db sorted alphabetically:");
        printBooksSet(libraryDbService.getBooksByCategory("poetry"));

        System.out.println("\nNovels from the db sorted alphabetically:");
        printBooksSet(libraryDbService.getBooksBySubcategory("Novel"));

        System.out.println("\nThe authors with 2 books");
        System.out.println(libraryDbService.getAuthorsByMinNumOfBooks(2));

        String authorName = "Stephen King";
        System.out.println("\nBooks by "+authorName+" in the library:");
        printBooksSet(libraryDbService.getBooksByAuthorName(authorName));

        System.out.println("\n--------------------------------------------------------------------------------\n");

        Novel novel3 = Novel.newNovel()
                .title("The Talisman")
                .authors(new HashSet<>(Collections.singletonList(Author.newAuthor().name("Walter Scott")
                        .buildAuthor())))
                .yearWritten(1825)
                .genres(new HashSet<>(Collections.singletonList("Historical")))
                .buildNovel();

        Author author5 = Author.newAuthor().name("Ben Jonson").country("England").buildAuthor();
        Drama drama2 = Drama.newDrama()
                .title("Every Man in His Humour")
                .authors(new HashSet<>(Collections.singletonList(author5)))
                .copiesInLibrary(3)
                .originalLanguage("Early Modern English")
                .dramaGenre("Comedy")
                .buildDrama();

        Textbook textbook1 = Textbook.newTextbook()
                .title("Introduction to Atoms and Molecules")
                .datePublished(LocalDate.parse("2020-09-01"))
                .copiesInLibrary(6)
                .language("English")
                .subject("Chemical Compounds")
                .domain("Chemistry")
                .level("Beginner")
                .buildTextbook();

        Textbook textbook2 = Textbook.newTextbook()
                .title("Analiza Genului Liric")
                .copiesInLibrary(2)
                .datePublished(LocalDate.parse("2021-08-11"))
                .language("Romanian")
                .domain("Literature")
                .buildTextbook();

        Textbook textbook3 = Textbook.newTextbook()
                .title("Chemical Laws")
                .datePublished(LocalDate.parse("2018-10-03"))
                .language("English")
                .subject("Chemical Reactions")
                .domain("Chemistry")
                .level("Advanced")
                .buildTextbook();

        Dictionary dict1 = Dictionary.newDictionary()
                .title("English-French Dictionary")
                .language("English")
                .targetLanguage("French")
                .type("Bilingual Dictionary")
                .buildDictionary();

        Dictionary dict2 = Dictionary.newDictionary()
                .title("English Medical Dictionary")
                .language("English")
                .type("Specialized Dictionary")
                .field("Medicine")
                .buildDictionary();

        Autobiography test1 = Autobiography.newAutobiography()
                .title("Autobiography Test1")
                .authors(new HashSet<>(Collections.singletonList(author1)))
                .language("English")
                .translatedFrom("French")
                .buildAutobiography();
//        System.out.println(libraryService.getAuthorsWithAutobiography());

        Autobiography test2 = Autobiography.newAutobiography()
                .title("Autobiography Test2")
                .authors(new HashSet<>(Arrays.asList(author4, author5)))
                .language("English")
                .buildAutobiography();

        libraryDbService.addBooks(novel3, drama2, textbook1, textbook3, textbook2);
        libraryDbService.addBooks(dict1, dict2, test1, test2);

        System.out.println("Historical novels in the library:");
        printBooksSet(libraryDbService.getNovelsByGenre("historical"));
        System.out.println("\nSci-Fi novels in the library:");
        printBooksSet(libraryDbService.getNovelsByGenre("Sci-Fi"));

        System.out.println("\nComedies in the library:");
        printBooksSet(libraryDbService.getDramasByGenre("comedy"));

        String bookTitle = "The Talisman";
        System.out.println("\nBooks titled '"+bookTitle+"' in the library:");
        printBooksSet(libraryDbService.getBooksByTitle(bookTitle));

        System.out.println("\nChemistry textbooks from 2019-2021 in the library:");
        printBooksSet(libraryDbService.getTextbooksByDomainAndYears("chemistry", 2019, 2021));

        System.out.println("\nEnglish-french dictionaries");
        printBooksSet(libraryDbService.getDictionariesByLanguages("english", "french"));

        System.out.println("\n--------------------------------------------------------------------------------\n");

        System.out.println("\nAuthors with autobiographies:");
        libraryDbService.getAuthorsWithAutobiography().forEach((key,value) -> System.out.println(key+" -> "+value));

        Address address1 = new Address("Romania", "Bucharest", "StreetName");
        LibraryMember member1 = LibraryMember.newMember()
                .name("Member 1")
                .email("email@yahoo.com")
                .address(address1)
                .membershipStarted(LocalDate.parse("2021-05-04"))
                .membershipExpires(LocalDate.parse("2022-11-04"))
                //.borrowedBooks(shortStories1, novel2)
                .buildMember();
        //System.out.println(member1+"\n");

        LibraryMember member2 = LibraryMember.newMember()
                .name("Member 2")
                .phone("1111-222-333")
                .membershipStarted(LocalDate.parse("2022-03-20"))
                .membershipExpires(LocalDate.parse("2022-06-20"))
                .address(new Address("France", "Paris"))
                .buildMember();

        libraryDbService.addMembers(member2, member1, member2); //member2 nu se adauga - ok

        libraryDbService.addBorrowedBooksToMember(1, shortStories1, novel1, novel2);
        libraryDbService.addBorrowedBooksToMember(2, shortStories1, drama1, novel1);
        libraryDbService.addBorrowedBooksToMember(1, drama1);

        System.out.println("\nAll library members:");
        printMembersSet(libraryDbService.getAllMembers());

        libraryDbService.returnBorrowedBooks(1, novel1, novel2);
        libraryDbService.returnBorrowedBooks(2, shortStories1, drama1);

        LibraryMember member3 = LibraryMember.newMember()
                .name("test")
                .email("testEmail")
                //.membershipStarted(LocalDate.of(2021, Month.FEBRUARY, 15))
                .membershipStarted(LocalDate.parse("2021-02-15"))
                .membershipExpires(LocalDate.parse("2022-02-15"))
                .buildMember();

        libraryDbService.addMembers(member3);

        System.out.println("\nMembers With Expired Membership:");
        printMembersSet(libraryDbService.getMembersWithExpiredMembership());

        libraryDbService.deleteMembers(member1, member3); //ok

        System.out.println("\nAll library members:");
        printMembersSet(libraryDbService.getAllMembers());

        //System.out.println("\n--------------------------------------------------------------------------------\n");

//        libraryCsvService.addBooks(novel1, shortStories1, drama1);
//        libraryCsvService.addBooks(shortStories1, novel2);
//        System.out.println("Books from CSV file sorted alphabetically:");
//        printBooksSet(libraryCsvService.getAllBooksSortedAlphabetically());

//        System.out.println();
//        printBooksSet(libraryCsvService.getBooksByTitle("The Three Musketeers"));
//        printBooksSet(libraryCsvService.getBooksByAuthorName("William Shakespeare"));
//
//        System.out.println("\nPoetry books from CSV file:");
//        printBooksSet(libraryCsvService.getBooksByCategory("poetry"));
//
//        System.out.println("Short Stories books from CSV file sorted alphabetically:");
//        printBooksSet(libraryCsvService.getBooksBySubcategory("Short Story Collection"));
//
//        System.out.println("\nHistorical Novels from CSV file:");
//        printBooksSet(libraryCsvService.getNovelsByGenre("Historical"));
//
//        libraryCsvService.deleteBooks(drama1);
//        System.out.println("\nAfter Delete:");
//        printBooksSet(libraryCsvService.getAllBooksSortedAlphabetically());

//        libraryCsvService.addMembers(member2, member1);
//        libraryCsvService.addMembers(member3, member1);
////        libraryCsvService.addBorrowedBooksToMember(3, novel1, novel2);
////        libraryCsvService.addBorrowedBooksToMember(2, shortStories1);
////        libraryCsvService.returnBorrowedBooks(3, novel2);
//                //printMembersSet(libraryCsvService.getAllMembers());
//                libraryCsvService.deleteMembers(member1,member2);
//                System.out.println("\nAll library members from CSV file:");
//                printMembersSet(libraryCsvService.getAllMembers());
//
//        System.out.println(libraryCsvService.getMemberById(2));
//        printMembersSet(libraryCsvService.getMembersWithExpiredMembership());
    }
}

