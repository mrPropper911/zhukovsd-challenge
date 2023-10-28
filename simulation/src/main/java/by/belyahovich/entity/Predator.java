package by.belyahovich.entity;

import java.util.ArrayList;
import java.util.List;

public class Predator extends Creature{
    private final static String image = "\uD83E\uDD8A";//🦊

    public Predator() {
        super();
    }

    @Override
    void move() {

    }

    @Override
    public String viewImage() {
        return image;
    }

    public List<Predator> getRandomListPredator(int countOfPredator){
        List<Predator> predatorList = new ArrayList<>();
        for (int i = 0; i < countOfPredator; i++){
            predatorList.add(new Predator());
        }
        return predatorList;
    }
}
