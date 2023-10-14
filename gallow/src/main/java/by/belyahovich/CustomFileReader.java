package by.belyahovich;

import java.util.Optional;

public interface CustomFileReader {

    void saveMystery(Mystery mystery);

    Iterable<Mystery> getAllMystery();

    Optional<Mystery> getRandomMystery();

}
