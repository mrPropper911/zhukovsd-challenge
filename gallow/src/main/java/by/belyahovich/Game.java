package by.belyahovich;

import java.util.Scanner;
import java.util.UUID;

public class Game {

    private static final CustomFileReaderCsv customFileReaderCsv = CustomFileReaderCsv.getInstance();

    public static void init(){
        boolean exitChoose = false;
        System.out.println("Welcome to the game hangman!!!\n");
        while (!exitChoose){
            System.out.println("""
                Select an action:
                1 - Start new game
                2 - Add word
                3 - Exit
                """);
            Scanner scanner = new Scanner(System.in);
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

    }

    private static void addNewWord(String word){
        Mystery mystery = Mystery.newBuilder()
                .setMysteryId(UUID.randomUUID().getMostSignificantBits())
                .setMysteryWord(word)
                .build();
        customFileReaderCsv.saveMystery(mystery);
    }

}
