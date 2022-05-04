package project.library.books.poetry;

import java.util.Objects;
import java.util.Set;

public class Drama extends Poetry {
    private String dramaGenre;
    private Set<String> themes;

    private Drama(DramaBuilder builder) {
        super(builder);
        this.dramaGenre = builder.dramaGenre;
        this.themes = builder.themes;
    }

    @Override
    public String getSubcategory() {
        return "Drama";
    }

    public String getDramaGenre() {
        return dramaGenre;
    }

    public void setDramaGenre(String dramaGenre) {
        this.dramaGenre = dramaGenre;
    }

    public Set<String> getThemes() {
        return themes;
    }

    public void setThemes(Set<String> themes) {
        this.themes = themes;
    }

    @Override
    public String toString() {
        return super.toString() + ",\n" +
                "      subcategory=" + getSubcategory() + ",\n" +
                "      dramaGenre=" + dramaGenre + ",\n" +
                "      themes=" + themes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drama)) return false;
        if (!super.equals(o)) return false;
        Drama drama = (Drama) o;
        return Objects.equals(getDramaGenre(), drama.getDramaGenre()) && Objects.equals(getThemes(), drama.getThemes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDramaGenre(), getThemes());
    }

    public static DramaBuilder newDrama() {
        return new DramaBuilder();
    }

    public static class DramaBuilder extends PoetryBuilder.AbstractPoetryBuilder<DramaBuilder> {
        private String dramaGenre;
        private Set<String> themes;

        @Override
        public DramaBuilder getThis() {
            return this;
        }

        public DramaBuilder dramaGenre(String dramaGenre)
        {
            this.dramaGenre = dramaGenre;
            return this;
        }

        public DramaBuilder themes(Set<String> themes)
        {
            this.themes = themes;
            return this;
        }

        public Drama buildDrama() {
            return new Drama(this);
        }
    }
}
