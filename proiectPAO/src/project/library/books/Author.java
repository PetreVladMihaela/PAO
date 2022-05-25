package project.library.books;

import java.time.LocalDate;
import java.util.Objects;

public class Author implements Comparable<Author> {
    private int id;
    private String name;
    private String country;
    private LocalDate dateBorn;
    private LocalDate dateDied;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDateBorn() {
        return dateBorn;
    }

    public void setDateBorn(LocalDate dateBorn) {
        this.dateBorn = dateBorn;
    }

    public LocalDate getDateDied() {
        return dateDied;
    }

    public void setDateDied(LocalDate dateDied) {
        this.dateDied = dateDied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return Objects.equals(getName(), author.getName()) &&
                Objects.equals(getCountry(), author.getCountry()) &&
                Objects.equals(getDateBorn(), author.getDateBorn()) &&
                Objects.equals(getDateDied(), author.getDateDied());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCountry(), getDateBorn(), getDateDied());
    }

    @Override
    public int compareTo(Author o) {
        if (this.equals(o)) {
            return 0;
        }
        else return this.id - o.id;
    }

    public static AuthorBuilder newAuthor() {
        return new AuthorBuilder();
    }

    public static class AuthorBuilder {
        //private int id;
        private String name = "unknown";
        private String country = "null";
        private LocalDate dateBorn;
        private LocalDate dateDied;

        public AuthorBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AuthorBuilder country(String country) {
            this.country = country;
            return this;
        }

        public AuthorBuilder dateBorn(LocalDate dateBorn) {
            this.dateBorn = dateBorn;
            return this;
        }

        public AuthorBuilder dateDied(LocalDate dateDied) {
            this.dateDied = dateDied;
            return this;
        }

        public Author buildAuthor() {
            Author author = new Author();
            author.setName(this.name);
            author.setCountry(this.country);
            author.setDateBorn(this.dateBorn);
            author.setDateDied(this.dateDied);
            return author;
        }
    }

    @Override
    public String toString() {
        return "Author{name=" + name + ", country=" + country +
                ", dateBorn=" + dateBorn + ", dateDied=" + dateDied + '}';
    }
}
