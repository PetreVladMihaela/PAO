package project.library.books.nonfiction;

import java.util.Objects;

public class Biography extends Autobiography{
    private String whoItIsAbout;

    public Biography(BiographyBuilder builder) {
        super(builder);
        this.whoItIsAbout = builder.whoItIsAbout;
        super.setSubcategory("Biography");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Biography)) return false;
        if (!super.equals(o)) return false;
        Biography biography = (Biography) o;
        return Objects.equals(getWhoItIsAbout(), biography.getWhoItIsAbout());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getWhoItIsAbout());
    }

    public String getWhoItIsAbout() {
        return whoItIsAbout;
    }

    public void setWhoItIsAbout(String whoItIsAbout) {
        this.whoItIsAbout = whoItIsAbout;
    }

    @Override
    public String toString() {
        return super.toString() + ",\n" +
                "      whoItIsAbout=" + whoItIsAbout;
    }

    public static BiographyBuilder newBiography() {
        return new BiographyBuilder();
    }

    public static class BiographyBuilder extends AbstractAutobioBuilder<BiographyBuilder> {
        private String whoItIsAbout;

        @Override
        public BiographyBuilder getThis() {
            return this;
        }
        @Override
        public BiographyBuilder getThisFinal() {
            return this;
        }
        public BiographyBuilder whoItIsAbout(String whoItIsAbout)
        {
            this.whoItIsAbout = whoItIsAbout;
            return this;
        }
        public Biography buildBiography() {
            return new Biography(this);
        }
    }

}
