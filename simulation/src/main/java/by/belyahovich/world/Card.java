package by.belyahovich.world;

import by.belyahovich.entity.EmptyPlace;
import by.belyahovich.entity.Entity;
import by.belyahovich.entity.Predator;

import java.util.HashMap;

public class Card {
    private final HashMap<Coordinates, Entity> world = new HashMap<>();
    private int height;
    private int width;

    private static volatile Card instance;

    public static Card getInstance() {
        Card localInstance = instance;
        if (localInstance == null){
            synchronized (Card.class){
                localInstance = instance;
                if (localInstance == null){
                   instance = localInstance = new Card();
                }
            }
        }
        return localInstance;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void generateEmptyWorld(){
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                instance.world.put(new Coordinates(i, j), new EmptyPlace());
            }
        }
    }

    public HashMap<Coordinates, Entity> getWorld() {
        return world;
    }
}
