package by.belyahovich;

import java.util.*;
import java.util.regex.Pattern;

public class Game {

    private static final CustomFileReaderCsv customFileReaderCsv = CustomFileReaderCsv.getInstance();

    public static void init() {
        boolean exitChoose = false;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the game hangman!!!\n");
        while (!exitChoose) {
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
        Pattern pattern = Pattern.compile("[a-z]");
        List<Character> enterUserCharacter = new ArrayList<>();

        Mystery mystery = customFileReaderCsv.getRandomMystery();
        char[] wordMystery = mystery.getWord().toCharArray();
        char[] wordUser = new char[wordMystery.length];
        Arrays.fill(wordUser, '_');
        System.out.println("Difficulty: " + mystery.getDifficult());
        short numberOfTry = 0;

        while (numberOfTry < 8) {
            numberOfTry++;
            printWord(wordUser);
            System.out.println("Number of try: " + numberOfTry + " of 8");
            try {
                char character = scanner.next(pattern)
                        .charAt(0);
                enterUserCharacter.add(character);
                if (enterUserCharacter.contains(character)) {
                    System.out.println("You have already entered this letter");
                    numberOfTry--;
                    continue;
                }
                for (int i = 0; i < wordUser.length; i++) {
                    if (wordMystery[i] == character) {
                        wordUser[i] = character;
                    }
                }
                if (Arrays.toString(wordUser).indexOf('_') == -1) {
                    System.out.println("You win!!!");
                    break;
                }
            } catch (InputMismatchException e) {
                numberOfTry--;
                System.out.println("Please enter only 1 lower case character");
                scanner.next();
            }
            if (numberOfTry == 8) {
                System.out.println("You lose =(\n");
            }
        }
    }

    private static void printWord(char[] word) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Character index : word) {
            stringBuilder.append(index);
        }
        System.out.println(stringBuilder);
    }

    private static void addNewWord(String word) {
        Mystery mystery = Mystery.newBuilder()
                .setMysteryId(new Random().nextInt(1_000_000))
                .setMysteryWord(word.toLowerCase())
                .build();
        customFileReaderCsv.saveMystery(mystery);
    }
}
