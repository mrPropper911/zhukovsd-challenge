package by.belyahovich.run;

import by.belyahovich.world.Card;

public class Simulation {

    private static final Card world = Card.getInstance();


    public static void run(){
        //Generate map
        int height = 10;
        int width = 10;
        world.setHeight(height);
        world.setWidth(width);
        world.generateEmptyWorld();

        int numberOfPredator = 2;
        int numberOfHerbivore = 5;

        Render.getInstance().render(world);
    }


}
