import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final int CAPACITY = 100;
    private static final String LETTERS = "abc";
    private static final int LENGTH = 10_000;
    private static final int COUNT = 100_000;

    private static BlockingQueue<String> aQueue = new ArrayBlockingQueue<>(CAPACITY);
    private static BlockingQueue<String> bQueue = new ArrayBlockingQueue<>(CAPACITY);
    private static BlockingQueue<String> cQueue = new ArrayBlockingQueue<>(CAPACITY);

    public static void main(String[] args) {

        justDoIt();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void theLongestWord(char letter, BlockingQueue<String> queue) throws InterruptedException {
        String longestWord = "";
        int maxCount = 0;
        int letterCount = 0;

        for (int i = 0; i < COUNT; i++) {
            String word = queue.take();
            letterCount = 0;
            for (int j = 0; j < LENGTH; j++) {
                if (word.charAt(j) == letter) {
                    letterCount++;
                }
            }
            if (letterCount > maxCount) {
                maxCount = letterCount;
                longestWord = word;
            }
        }

        if (letterCount == 0) {
            System.out.println("Слов с буквой " + letter + " не сгенерировалось!");
        } else {
            System.out.printf("Слово с наибольшим числом повторений буквы %c содержит %d повторений, это слово \"%s\"." +
                    "\n", letter, letterCount, longestWord);
        }


    }

    public static void justDoIt() {
        new Thread(() -> {
            for (int i = 0; i < COUNT; i++) {
                String word = generateText(LETTERS, LENGTH);
                try {
                    aQueue.put(word);
                    bQueue.put(word);
                    cQueue.put(word);
                } catch (InterruptedException ignored) {
                }

            }
        }).start();

        new Thread(() -> {
            try {
                theLongestWord('a', aQueue);
            } catch (InterruptedException ignored) {
            }
        }).start();

        new Thread(() -> {
            try {
                theLongestWord('b', bQueue);
            } catch (InterruptedException ignored) {
            }
        }).start();

        new Thread(() -> {
            try {
                theLongestWord('c', cQueue);
            } catch (InterruptedException ignored) {
            }
        }).start();

    }
}