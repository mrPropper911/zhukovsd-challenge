package by.belyahovich.run;

import by.belyahovich.entity.Creature;
import by.belyahovich.entity.Entity;
import by.belyahovich.entity.Herbivore;
import by.belyahovich.entity.Predator;
import by.belyahovich.world.Card;
import by.belyahovich.world.Coordinates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulation {

    private static final Card world = Card.getInstance();
    private static final Render instanceRender = Render.getInstance();


    public static void run() {
        //Generate map
        int height = 10;
        int width = 10;
        world.setHeight(height);
        world.setWidth(width);
        world.generateEmptyWorld();

        startSimulation();


    }

    public static void startSimulation() {

        int numberOfPredator = 2;
        int numberOfHerbivore = 5;
        List<Predator> predatorList = new Predator().getRandomListPredator(numberOfPredator);
        List<Herbivore> herbivoreList = new Herbivore().getRandomListHerbivore(numberOfHerbivore);

        HashMap<Coordinates, Entity> worldTest = world.getWorld();
        for (int i = 0; i < predatorList.size(); i++){
            worldTest.put(new Coordinates(1,2), predatorList.get(i));
        }
        for (int i = 0; i < herbivoreList.size(); i++){
            worldTest.put(new Coordinates(3,2), herbivoreList.get(i));
        }

        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            instanceRender.render(world);

        }
    }

    private void addCreatureToRandomPlaceInWorld (Creature creature){

    }

}



