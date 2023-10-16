package by.belyahovich.entity;

import java.util.ArrayList;
import java.util.List;

public class Herbivore extends Creature {
    private final static String image = "\uD83D\uDC30";//🐰

    public Herbivore() {
        super();
    }

    @Override
    void move() {

    }

    @Override
    public String viewImage() {
        return image;
    }

    public List<Herbivore> getRandomListHerbivore(int countOfHerbivore){
        List<Herbivore> herbivoreList = new ArrayList<>();
        for (int i = 0; i < countOfHerbivore; i++){
            herbivoreList.add(new Herbivore());
        }
        return herbivoreList;
    }
}
