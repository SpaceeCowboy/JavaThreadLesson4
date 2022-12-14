import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> a = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> b = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> c = new ArrayBlockingQueue<>(100);

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void findMaxChar(BlockingQueue<String> blockingQueue, char character) {
        int maxSize = 0;
        int count = 0;
        for (int i = 0; i < 10_000; i++) {
            try {
                String text = blockingQueue.take();
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == character) {
                        count++;
                    }
                }
                if (count > maxSize) {
                    maxSize = count;
                }
                count = 0;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Количество символов " + character + " = " + maxSize);
    }
    public static void main(String[] args) throws InterruptedException {
        Thread generate = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    a.put(text);
                    b.put(text);
                    c.put(text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        generate.start();

        Thread findA = new Thread(() -> {
            findMaxChar(a, 'a');
        });
        findA.start();

        Thread findB = new Thread(() -> {
            findMaxChar(b, 'b');

        });
        findB.start();

        Thread findC = new Thread(() -> {
            findMaxChar(c, 'c');
        });
        findC.start();

        findA.join();
        findB.join();
        findC.join();


    }
}
