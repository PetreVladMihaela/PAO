package project.library.books.fiction;

import java.util.Objects;

public class ShortStories extends FictionBook {
    private String storiesType;

    private ShortStories(ShortStoriesBuilder builder) {
        super(builder);
        this.storiesType = builder.storiesType;
    }

    @Override
    public String getSubcategory() {
        return "Short Story Collection";
    }

    public String getStoriesType() {
        return storiesType;
    }

    public void setStoriesType(String storiesType) {
        this.storiesType = storiesType;
    }

    @Override
    public String toString() {
        return super.toString() + ",\n" +
                "      subcategory=" + getSubcategory() + ",\n" +
                "      storiesType=" + storiesType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShortStories)) return false;
        if (!super.equals(o)) return false;
        ShortStories that = (ShortStories) o;
        return Objects.equals(getStoriesType(), that.getStoriesType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStoriesType());
    }

    public static ShortStoriesBuilder newShortStories() {
        return new ShortStoriesBuilder();
    }

    public static class ShortStoriesBuilder extends FictionBookBuilder.AbstractFictionBuilder<ShortStoriesBuilder> {
        private String storiesType;

        @Override
        public ShortStoriesBuilder getThis() {
            return this;
        }

        public ShortStoriesBuilder storiesType(String storiesType)
        {
            this.storiesType = storiesType;
            return this;
        }

        public ShortStories buildShortStories() {
            return new ShortStories(this);
        }
    }
}
