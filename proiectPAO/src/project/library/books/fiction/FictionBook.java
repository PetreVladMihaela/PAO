package project.library.books.fiction;

import project.library.books.Book;

import java.util.Objects;

public class FictionBook extends Book {
    private int yearWritten;
    private String originalLanguage;
    private String translatedInto;

    protected FictionBook(FictionBookBuilder builder) {
        super(builder);
        this.yearWritten = builder.yearWritten;
        this.originalLanguage = builder.originalLanguage;
        this.translatedInto = builder.translatedInto;
    }

    protected FictionBook(FictionBookBuilder.AbstractFictionBuilder<?> abstractBuilder) {
        super(abstractBuilder);
        this.yearWritten = abstractBuilder.fictionBuilder.yearWritten;
        this.originalLanguage = abstractBuilder.fictionBuilder.originalLanguage;
        this.translatedInto = abstractBuilder.fictionBuilder.translatedInto;
    }

    @Override
    public String getCategory() {
        return "Fiction";
    }

    @Override
    public String getSubcategory() {
        return null;
    }

    public int getYearWritten() {
        return yearWritten;
    }

    public void setYearWritten(int yearWritten) {
        this.yearWritten = yearWritten;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTranslatedInto() {
        return translatedInto;
    }

    public void setTranslatedInto(String translatedInto) {
        this.translatedInto = translatedInto;
    }

    @Override
    public String toString() {
        return super.toString() + ",\n" +
                "      category=" + getCategory() + ",\n" +
                "      yearWritten=" + yearWritten + ",\n" +
                "      originalLanguage=" + originalLanguage + ",\n" +
                "      translatedInto=" + translatedInto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FictionBook)) return false;
        if (!super.equals(o)) return false;
        FictionBook that = (FictionBook) o;
        return getYearWritten() == that.getYearWritten() &&
                Objects.equals(getOriginalLanguage(), that.getOriginalLanguage()) &&
                Objects.equals(getTranslatedInto(), that.getTranslatedInto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getYearWritten(), getOriginalLanguage(), getTranslatedInto());
    }

    public static FictionBookBuilder newFictionBook() {
        return new FictionBookBuilder();
    }

    public static class FictionBookBuilder extends BookBuilder<FictionBookBuilder> {
        private int yearWritten;
        private String originalLanguage = " ";
        private String translatedInto = " ";

        @Override
        public FictionBookBuilder getThisBase() {
            return this;
        }

        public FictionBookBuilder yearWritten(int yearWritten)
        {
            this.yearWritten = yearWritten;
            return this;
        }

        public FictionBookBuilder originalLanguage(String originalLanguage)
        {
            this.originalLanguage = originalLanguage;
            return this;
        }

        public FictionBookBuilder translatedInto(String translatedInto)
        {
            this.translatedInto = translatedInto;
            return this;
        }

        public FictionBook buildFictionBook() {
            return new FictionBook(this);
        }


        public abstract static class AbstractFictionBuilder<T extends AbstractFictionBuilder<T>>
                extends BookBuilder<AbstractFictionBuilder<T>>
        {
            FictionBookBuilder fictionBuilder = new FictionBookBuilder();

            @Override
            public AbstractFictionBuilder<T> getThisBase() {
                return this;
            }

            public abstract T getThis();

            public T yearWritten(int yearWritten)
            {
                this.fictionBuilder.yearWritten = yearWritten;
                return this.getThis();
            }

            public T originalLanguage(String originalLanguage)
            {
                this.fictionBuilder.originalLanguage = originalLanguage;
                return this.getThis();
            }

            public T translatedInto(String translatedInto)
            {
                this.fictionBuilder.translatedInto = translatedInto;
                return this.getThis();
            }
        }
    }

}
