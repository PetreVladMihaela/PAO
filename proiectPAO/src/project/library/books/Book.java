package project.library.books;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public abstract class Book {
    private int id;
    private String title;
    private Set<Author> authors;
    private LocalDate datePublished;
    private int copiesInLibrary;

    private String subcategory = " ";

    protected Book(BookBuilder<?> builder) {
        this.title = builder.title;
        this.authors = builder.authors;
        this.datePublished = builder.datePublished;
        this.copiesInLibrary = builder.copiesInLibrary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) { this.authors = authors; }

    public LocalDate getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(LocalDate datePublished) {
        this.datePublished = datePublished;
    }

    public int getCopiesInLibrary() {
        return copiesInLibrary;
    }

    public void setCopiesInLibrary(int copiesInLibrary) {
        this.copiesInLibrary = copiesInLibrary;
    }

    public abstract String getCategory();

    public String getSubcategory() { return subcategory; }

    public void setSubcategory(String s) { this.subcategory = s; }

    @Override
    public String toString() {
        return "Book: title='" + title + "',\n" +
                "      authors=" + authors + ",\n" +
                "      datePublished=" + datePublished + ",\n" +
                "      copiesInLibrary=" + copiesInLibrary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(getTitle(), book.getTitle()) && Objects.equals(getAuthors(), book.getAuthors())
                && Objects.equals(getDatePublished(), book.getDatePublished());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getAuthors(), getDatePublished());
    }

    public abstract static class BookBuilder<T extends BookBuilder<T>>
    {
        //private int id;
        private String title = "no title";
        private Set<Author> authors;
        private LocalDate datePublished;
        private int copiesInLibrary = 1;

        public abstract T getThisBase();

        public T title(String title)
        {
            this.title = title;
            return this.getThisBase();
        }

        public T authors(Set<Author> authors)
        {
            this.authors = authors;
            return this.getThisBase();
        }

        public T datePublished(LocalDate datePublished)
        {
            this.datePublished = datePublished;
            return this.getThisBase();
        }

        public T copiesInLibrary(int copiesInLibrary)
        {
            this.copiesInLibrary = copiesInLibrary;
            return this.getThisBase();
        }

    }
}
