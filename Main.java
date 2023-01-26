import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static void main(String[] args) {
        ArrayBlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100, true);
        ArrayBlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100, true);
        ArrayBlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100, true);

        int numberStr = 10000;//количество слов
        int lengthStr = 100000; // длинна слова

        new Thread(() ->//Генерирующий поток
        {
            Random random = new Random();
            for (int i = 0; i < numberStr; i++) {
                String str = generateText("abc", lengthStr + random.nextInt(3));
                try {
                    queue1.put(str);
                    queue2.put(str);
                    queue3.put(str);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                queue1.put("end");
                queue2.put("end");
                queue3.put("end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {//считает буквы а
            numberMaxLettersWord(queue1, 'a');
        }).start();

        new Thread(() -> {//считает буквы b
            numberMaxLettersWord(queue2, 'b');
        }).start();

        new Thread(() -> {//считает буквы c
            numberMaxLettersWord(queue3, 'c');
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    //подсчет количества символов 'c' в слове
    public static int numberLettersWord(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                count++;
        }
        return count;
    }

    //подсчет максимального количества заданных букв в слове коллекции
    public static void numberMaxLettersWord(ArrayBlockingQueue<String> queue, char letter) {
        int countMax = 0;
        while (true) {
            {
                try {
                    String str1 = queue.take();
                    if (!(str1 == null) && !str1.equals("end")) {
                        int count = numberLettersWord(str1, letter);
                        if (countMax < count) countMax = count;
                    } else break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        System.out.println("Максимальное количество букв " + letter + " в слове: " + countMax + " шт.");

    }
}

