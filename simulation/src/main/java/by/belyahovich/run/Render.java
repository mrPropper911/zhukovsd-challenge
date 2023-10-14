package by.belyahovich.run;

import by.belyahovich.world.Card;
import by.belyahovich.world.Coordinates;

public class Render {

    private static volatile Render instance;

    public static Render getInstance() {
        Render localInstance = instance;
        if (localInstance == null) {
            synchronized (Render.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Render();
                }
            }
        }
        return localInstance;
    }

    void render(Card card) {
        for (int i = 0; i < card.getHeight(); i++) {
            for (int j = 0; j < card.getWidth(); j++) {
                if (card.getWorld().containsKey(new Coordinates(i, j))) {
                    var imageOfEntity = card.getWorld().get(new Coordinates(i, j)).viewImage();
                    System.out.print(imageOfEntity + "   ");
                }
            }
            System.out.print("\n");
        }
    }
}
