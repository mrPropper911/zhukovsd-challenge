package by.belyahovich.entity;

public class Tree extends Entity {
    private final static String image = "\uD83C\uDF33";//🌳

    @Override
    public String viewImage() {
        return image;
    }
}
