package project.library.books.nonfiction;

import java.util.Objects;

public class Autobiography extends NonfictionBook {
    private String translatedFrom;

    protected Autobiography(AutobiographyBuilder builder) {
        super(builder);
        this.translatedFrom = builder.translatedFrom;
        super.setSubcategory("Autobiography");
    }

    protected Autobiography(AbstractAutobioBuilder<?> builder) {
        super(builder);
        this.translatedFrom = builder.translatedFrom;
    }

    public String getTranslatedFrom() {
        return translatedFrom;
    }

    public void setTranslatedFrom(String translatedFrom) {
        this.translatedFrom = translatedFrom;
    }

    @Override
    public String toString() {
        return super.toString() + ",\n" +
                "      subcategory=" + getSubcategory() + ",\n" +
                "      translatedFrom=" + translatedFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Autobiography)) return false;
        if (!super.equals(o)) return false;
        Autobiography that = (Autobiography) o;
        return Objects.equals(getTranslatedFrom(), that.getTranslatedFrom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTranslatedFrom());
    }

    public static AutobiographyBuilder newAutobiography() {
        return new AutobiographyBuilder();
    }

    public static class AutobiographyBuilder extends AbstractNonfictionBuilder<AutobiographyBuilder>{
        private String translatedFrom;

        @Override
        public AutobiographyBuilder getThis() { return this; }
        public AutobiographyBuilder translatedFrom(String translatedFrom)
        {
            this.translatedFrom = translatedFrom;
            return this;
        }
        public Autobiography buildAutobiography() {
            return new Autobiography(this);
        }
    }

    public static abstract class AbstractAutobioBuilder<T extends AbstractAutobioBuilder<T>>
            extends AbstractNonfictionBuilder<AbstractAutobioBuilder<T>> {

        private String translatedFrom;

        @Override
        public AbstractAutobioBuilder<T> getThis() { return this; }

        public abstract T getThisFinal();

        public T translatedFrom(String translatedFrom)
        {
            this.translatedFrom = translatedFrom;
            return this.getThisFinal();
        }
    }

}
