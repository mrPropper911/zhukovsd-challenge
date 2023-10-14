package by.belyahovich.entity;

public class Herbivore extends Creature {
    private final static String image = "\uD83D\uDC30";//🐰


    @Override
    void move() {

    }

    @Override
    public String viewImage() {
        return image;
    }
}
