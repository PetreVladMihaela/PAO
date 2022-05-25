package project.library.books.nonfiction;

import java.util.Objects;

public class Dictionary extends NonfictionBook {
    private String type;
    private String targetLanguage;
    private String field;

    private Dictionary(DictionaryBuilder builder) {
        super(builder);
        this.type = builder.type;
        this.targetLanguage = builder.targetLanguage;
        this.field = builder.field;
        super.setSubcategory("Dictionary");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return super.toString() + ",\n" +
                "      subcategory=" + getSubcategory() + ",\n" +
                "      type=" + type + ",\n" +
                "      targetLanguage=" + targetLanguage + ",\n" +
                "      field=" + field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dictionary)) return false;
        if (!super.equals(o)) return false;
        Dictionary that = (Dictionary) o;
        return Objects.equals(getType(), that.getType()) && Objects.equals(getTargetLanguage(), that.getTargetLanguage())
                && Objects.equals(getField(), that.getField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getType(), getTargetLanguage(), getField());
    }

    public static DictionaryBuilder newDictionary() {
        return new DictionaryBuilder();
    }

    public static class DictionaryBuilder extends AbstractNonfictionBuilder<DictionaryBuilder> {
        private String type;
        private String targetLanguage;
        private String field;

        @Override
        public DictionaryBuilder getThis() {
            return this;
        }

        public DictionaryBuilder type(String type)
        {
            this.type = type;
            return this;
        }

        public DictionaryBuilder targetLanguage(String targetLanguage)
        {
            this.targetLanguage = targetLanguage;
            return this;
        }

        public DictionaryBuilder field(String field)
        {
            this.field = field;
            return this;
        }

        public Dictionary buildDictionary() {
            return new Dictionary(this);
        }
    }
}
