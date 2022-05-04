package project.library.books.poetry;

import project.library.books.Book;

import java.util.Objects;

public class Poetry extends Book {
    private String originalLanguage;
    private String translatedInto;

    protected Poetry(PoetryBuilder builder) {
        super(builder);
        this.originalLanguage = builder.originalLanguage;
        this.translatedInto = builder.translatedInto;
    }

    protected Poetry(PoetryBuilder.AbstractPoetryBuilder<?> abstractBuilder) {
        super(abstractBuilder);
        this.originalLanguage = abstractBuilder.poetryBuilder.originalLanguage;
        this.translatedInto = abstractBuilder.poetryBuilder.translatedInto;
    }

    @Override
    public String getCategory() {
        return "Poetry";
    }

    @Override
    public String getSubcategory() {
        return null;
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
                "      originalLanguage=" + originalLanguage + ",\n" +
                "      translatedInto=" + translatedInto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Poetry)) return false;
        if (!super.equals(o)) return false;
        Poetry poetry = (Poetry) o;
        return Objects.equals(getOriginalLanguage(), poetry.getOriginalLanguage()) &&
                Objects.equals(getTranslatedInto(), poetry.getTranslatedInto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getOriginalLanguage(), getTranslatedInto());
    }

    public static PoetryBuilder newPoetryBook() {
        return new PoetryBuilder();
    }

    public static class PoetryBuilder extends BookBuilder<PoetryBuilder> {
        private String originalLanguage;
        private String translatedInto;

        @Override
        public PoetryBuilder getThisBase() {
            return this;
        }

        public PoetryBuilder originalLanguage(String originalLanguage)
        {
            this.originalLanguage = originalLanguage;
            return this;
        }

        public PoetryBuilder translatedInto(String translatedInto)
        {
            this.translatedInto = translatedInto;
            return this;
        }

        public Poetry buildPoetryBook() {
            return new Poetry(this);
        }


        public abstract static class AbstractPoetryBuilder<T extends AbstractPoetryBuilder<T>>
                extends BookBuilder<AbstractPoetryBuilder<T>>
        {
            PoetryBuilder poetryBuilder = new PoetryBuilder();

            @Override
            public AbstractPoetryBuilder<T> getThisBase() {
                return this;
            }

            public abstract T getThis();

            public T originalLanguage(String originalLanguage)
            {
                this.poetryBuilder.originalLanguage = originalLanguage;
                return this.getThis();
            }

            public T translatedInto(String translatedInto)
            {
                this.poetryBuilder.translatedInto = translatedInto;
                return this.getThis();
            }
        }
    }
}
