package by.belyahovich;





import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

public class CustomFileReaderCsv implements CustomFileReader {

    private static final Logger log = LogManager.getLogger(CustomFileReaderCsv.class);

    private static volatile CustomFileReaderCsv instance;
    private static final String PATH_OF_CSV_FILE_WITH_WORDS = "gallow/src/main/resources/vocabulary.txt";


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
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mystery);
            objectOutputStream.close();
            log.info("Save " + mystery.getWord() + " to vocabulary");
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
    }

    @Override
    public Set<Mystery> getAllMystery() {
        File file = new File(PATH_OF_CSV_FILE_WITH_WORDS);
        Set<Mystery> allMystery = new HashSet<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            for (;;){
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    Object mystery = objectInputStream.readObject();
                    if (mystery instanceof Mystery){
                        allMystery.add((Mystery) mystery);
                    }
                } catch (EOFException e){
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getStackTrace());
        }
        return allMystery;
    }

    @Override
    public Mystery getRandomMystery() {
        List<Mystery> allMystery = getAllMystery()
                .stream()
                .toList();
        int randomIndexOfMystery= new Random()
                .nextInt(allMystery.size());
        return allMystery.get(randomIndexOfMystery);
    }
}
