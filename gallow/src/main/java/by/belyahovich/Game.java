package by.belyahovich;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Game {

    private static final CustomFileReaderCsv customFileReaderCsv = CustomFileReaderCsv.getInstance();

    public static void init(){
        boolean exitChoose = false;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the game hangman!!!\n");
        while (!exitChoose){
            System.out.println("""
                Select an action:
                1 - Start new game
                2 - Add word
                3 - Exit
                """);
            short userAction = scanner.nextShort();
            switch (userAction) {
                case 1 -> startGame();
                case 2 -> {
                    System.out.println("Write the word\n");
                    String word = scanner.next();
                    addNewWord(word);
                }
                case 3 -> {
                    System.out.println("Thx for game! goodbye . . .");
                    exitChoose = true;
                }

                default -> System.out.println("Incorrect action!\n");
            }
        }
    }

    private static void startGame() {
        Scanner scanner = new Scanner(System.in);
        short numberOfTry = 0;
        Mystery mystery = customFileReaderCsv.getRandomMystery();
        char[] wordMystery = mystery.getWord().toCharArray();
        //todo for test
        System.out.println(wordMystery);


        char[] wordUser = new char[wordMystery.length];

        Arrays.fill(wordUser, '_');
        System.out.println("Difficulty: " +  mystery.getDifficult());

        Pattern pattern = Pattern.compile("[A-Za-z]");

        while (numberOfTry < 8){
            numberOfTry++;
            printWord(wordUser);
            System.out.println("Number of try: " +  numberOfTry + " of 8");
            try {
                char character = scanner.next(pattern)
                        .toLowerCase()
                        .charAt(0);
                for (int i = 0; i < wordUser.length; i++){
                    if (wordMystery[i] == character){
                        wordUser[i] = character;
                    }
                }
            }
            catch (InputMismatchException e){
                numberOfTry--;
                System.out.println("Please enter only 1 character");
                scanner.next();
            }

        }

    }

    private static void printWord(char[] word){
        StringBuilder stringBuilder = new StringBuilder();
        for (Character index: word){
            stringBuilder.append(index);
        }
        System.out.println(stringBuilder);
    }



    private static void addNewWord(String word){
        Mystery mystery = Mystery.newBuilder()
                .setMysteryId(new Random().nextInt(1_000_000))
                .setMysteryWord(word.toLowerCase())
                .build();
        customFileReaderCsv.saveMystery(mystery);
    }

}
