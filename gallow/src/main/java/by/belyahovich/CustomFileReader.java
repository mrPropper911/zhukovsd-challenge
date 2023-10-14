package by.belyahovich;

import java.util.Set;

public interface CustomFileReader {

    void saveMystery(Mystery mystery);

    Set<Mystery> getAllMystery();

    Mystery getRandomMystery();

}
