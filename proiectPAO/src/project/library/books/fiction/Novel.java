package project.library.books.fiction;

import java.util.Objects;
import java.util.Set;

public class Novel extends FictionBook {
    private Set<String> genres;
    private Set<String> themes;

    private Novel(NovelBuilder builder) {
        super(builder);
        this.genres = builder.genres;
        this.themes = builder.themes;
        super.setSubcategory("Novel");
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
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
                "      genres=" + genres + ",\n" +
                "      themes=" + themes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Novel)) return false;
        if (!super.equals(o)) return false;
        Novel novel = (Novel) o;
        return Objects.equals(getGenres(), novel.getGenres()) && Objects.equals(getThemes(), novel.getThemes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGenres(), getThemes());
    }

    public static NovelBuilder newNovel() {
        return new NovelBuilder();
    }

    public static class NovelBuilder extends FictionBookBuilder.AbstractFictionBuilder<NovelBuilder> {
        private Set<String> genres;
        private Set<String> themes;

        @Override
        public NovelBuilder getThis() {
            return this;
        }

        public NovelBuilder genres(Set<String> genres)
        {
            this.genres = genres;
            return this;
        }

        public NovelBuilder themes(Set<String> themes)
        {
            this.themes = themes;
            return this;
        }

        public Novel buildNovel() {
            return new Novel(this);
        }
    }

}
