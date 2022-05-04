package project.library.books.nonfiction;

import java.util.Objects;

public class Textbook extends NonfictionBook {
    private String domain;
    private String subject;
    private String level;

    private Textbook(TextbookBuilder builder) {
        super(builder);
        this.domain = builder.domain;
        this.subject = builder.subject;
        this.level = builder.level;
    }

    @Override
    public String getSubcategory() {
        return "Textbook";
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLevel() { return level; }

    public void setLevel(String level) { this.level = level; }

    @Override
    public String toString() {
        return super.toString() + ",\n" +
                "      subcategory=" + getSubcategory() + ",\n" +
                "      domain=" + domain + ",\n" +
                "      subject=" + subject + ",\n" +
                "      level=" + level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Textbook)) return false;
        if (!super.equals(o)) return false;
        Textbook textbook = (Textbook) o;
        return Objects.equals(getDomain(), textbook.getDomain()) && Objects.equals(getSubject(), textbook.getSubject())
                && Objects.equals(getLevel(), textbook.getLevel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDomain(), getSubject(), getLevel());
    }

    public static TextbookBuilder newTextbook() {
        return new TextbookBuilder();
    }

    public static class TextbookBuilder extends AbstractNonfictionBuilder<TextbookBuilder> {
        private String domain;
        private String subject;
        private String level;

        @Override
        public TextbookBuilder getThis() {
            return this;
        }

        public TextbookBuilder domain(String domain)
        {
            this.domain = domain;
            return this;
        }

        public TextbookBuilder subject(String subject)
        {
            this.subject = subject;
            return this;
        }

        public TextbookBuilder level(String level)
        {
            this.level = level;
            return this;
        }

        public Textbook buildTextbook() {
            return new Textbook(this);
        }
    }
}
