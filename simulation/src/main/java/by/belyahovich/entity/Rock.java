package by.belyahovich.entity;

public class Rock extends Entity{
    private final String image = "\uD83E\uDDF1";//🧱

    @Override
    public String viewImage() {
        return image;
    }
}
