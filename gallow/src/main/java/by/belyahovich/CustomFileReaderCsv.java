package by.belyahovich;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Optional;

public class CustomFileReaderCsv implements CustomFileReader {

    private static final Logger log = LogManager.getLogger(CustomFileReaderCsv.class);

    private static volatile CustomFileReaderCsv instance;
    private static final String PATH_OF_CSV_FILE_WITH_WORDS = "gallow/src/main/resources/vocabulary.csv";

    public static CustomFileReaderCsv getInstance(){
        CustomFileReaderCsv localInstance = instance;
        if (localInstance == null){
            synchronized (CustomFileReaderCsv.class){
                localInstance = instance;
                if (localInstance == null){
                    instance = localInstance = new CustomFileReaderCsv();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void saveMystery(Mystery mystery) {
        File file = new File(PATH_OF_CSV_FILE_WITH_WORDS);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mystery);
            objectOutputStream.close();
            log.info("Save " + mystery.getWord() + " to vocabulary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<Mystery> getAllMystery() {
        return null;
    }

    @Override
    public Optional<Mystery> getRandomMystery() {
        return Optional.empty();
    }
}
