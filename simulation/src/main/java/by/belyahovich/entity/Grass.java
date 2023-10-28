package by.belyahovich.entity;

public class Grass extends Entity{
    private final String image = "\uD83C\uDF3F";//🌿


    @Override
    public String viewImage() {
        return image;
    }
}
