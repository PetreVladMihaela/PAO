package project.library.books.nonfiction;

import project.library.books.Book;

import java.util.Objects;

public class NonfictionBook extends Book {
    private String language;

    protected NonfictionBook(NonfictionBookBuilder builder) {
        super(builder);
        this.language = builder.language;
    }

    protected NonfictionBook(AbstractNonfictionBuilder<?> builder) {
        super(builder);
        this.language = builder.language;
    }

    @Override
    public String getCategory() {
        return "Nonfiction";
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return super.toString() + ",\n" +
                "      category=" + getCategory() + ",\n" +
                "      language=" + language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NonfictionBook)) return false;
        if (!super.equals(o)) return false;
        NonfictionBook that = (NonfictionBook) o;
        return Objects.equals(getLanguage(), that.getLanguage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLanguage());
    }

    public static NonfictionBookBuilder newNonfictionBook() {
        return new NonfictionBookBuilder();
    }

    public static class NonfictionBookBuilder extends BookBuilder<NonfictionBookBuilder> {
        private String language;

        @Override
        public NonfictionBookBuilder getThisBase() {
            return this;
        }

        public NonfictionBookBuilder language(String language)
        {
            this.language = language;
            return this;
        }

        public NonfictionBook buildNonfictionBook() {
            return new NonfictionBook(this);
        }
    }


    public abstract static class AbstractNonfictionBuilder<T extends AbstractNonfictionBuilder<T>>
            extends BookBuilder<AbstractNonfictionBuilder<T>>
    {
        private String language = " ";

        @Override
        public AbstractNonfictionBuilder<T> getThisBase() {
            return this;
        }

        public abstract T getThis();

        public T language(String language)
        {
            this.language = language;
            return this.getThis();
        }
    }
}
