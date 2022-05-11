package project.library.books.nonfiction;

public class Biography extends Autobiography{
    private String whoItIsAbout;

    public Biography(BiographyBuilder builder) {
        super(builder);
        this.whoItIsAbout = builder.whoItIsAbout;
    }

    @Override
    public String getSubcategory() {
        return "Biography";
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
