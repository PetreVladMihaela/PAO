# Proiect PAO - Tema: Biblioteca

## Obiecte: 
1. Diverse tipuri de carți
    - La bază este clasa abstractă Book din care sunt derivate clasele FictionBook, NonfictionBook și respectiv Poetry. 
    - Clasele Novel și ShortStories extind FictionBook;
    - Clasa Drama extinde Poetry;
    - Clasele Textbook, Dictionary și Autobiography extind NonfictionBook.
2. Clasa Author - o carte poate avea mai mulți autori (salvați ca Set).
3. Clasa LibraryMember - reprezintă abonații la bibliotecă. Aceștia au o listă de cărți împrumutate.
4. Clasa Address - adresa unui membru.

## Acțiunile sistemului (metodele din interfața libraryService):

 1. Adaugă cărți/membri: void addBooks(Book... books); + void addMembers(LibraryMember... members);
    - Cănd se adaugă o carte autorii săi sunt salvați separat.
    - Membrii sunt ordonați dupa id, care este calculat automat la inserare.
 2. Întoarce setul de carți al librăriei in ordine alfabetică: LinkedHashSet<Book> getAllBooksSortedAlphabetically();
 3. Întoarce autorii/membrii salvați în sistem: Set<Author> getAllAuthors() + Set<LibraryMember> getAllMembers();
 4. Șterge cărți/membri: void deleteBooks(Book... booksToDelete); + void deleteMembers(LibraryMember... membersToDelete);
    - Autorii cărților sterșe sunt și ei șterși automat dacă nu mai au alte cărți în bibliotecă.
    - Membrii nu pot fi șterși dacă au cărți împrumutate.
  5. Caută membrul cu id-ul dat ca parametru: LibraryMember getMemberById(int id);
  6. Caută membrii cu abonamentrul expirat: Set<LibraryMember> getMembersWithExpiredMembership();
  7. Membrii pot imprumuta și returna cărți. Ei pot avea maxim 3 cărți împrumutate la un moment dat.
     - void addBorrowedBooksToMember(int memberId, Book... books);
     - void returnBorrowedBooks(int memberId, Book... returnedBooks);
  8. Caută cărți după titlu: LinkedHashSet<Book> getBooksByTitle(String title);
  9. Caută cărți după numele autorului: LinkedHashSet<Book> getBooksByAuthorName(String authorName);
  10. Caută cărți după categorie(fiction, nonfiction, poetry) sau subcategorie(novel, textbook, etc.): 
     - LinkedHashSet<Book> getBooksByCategory(String category);
     - LinkedHashSet<Book> getBooksBySubcategory(String subcategory);
  11. Caută nuvele și drame după gen:
     - LinkedHashSet<Book> getNovelsByGenre(String genre);
     - LinkedHashSet<Book> getDramasByGenre(String dramaGenre);
  12. Caută manuale după domenii și anii publicației: LinkedHashSet<Book> getTextbooksByDomainAndYears(String domain, int startYear, int endYear);
  13. Caută dicționare după limbă: LinkedHashSet<Book> getDictionariesByLanguages(String writtenIn, String targetLanguage);
  14. Întoarce autorii care au scris un numar minim dat de cărți: Set<Author> getAuthorsByMinNumOfBooks(int min);
  15. Întoare autorii care au scris autobiografii, împreună cu acestea: HashMap<Author, Book> getAuthorsWithAutobiography();
