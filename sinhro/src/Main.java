import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        final String letters = "RLRFR";
        final int length = 100;
        final int numThreads = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        for (int i =0; i<numThreads; i++){
            executorService.submit(()->{
                String route = generateRoute(letters, length);
                long turnRight = route.chars().filter(ch -> ch == 'R').count();
                updateMap((int) turnRight);
            });
        }
        executorService.shutdown();
        printResult();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
    public static final Map<Integer, Integer> sizeToFreq = new ConcurrentHashMap<>();
    public static synchronized void updateMap(int turnRight) {
        sizeToFreq.merge(turnRight, 1, Integer::sum);
    }

    public static void printResult() {
        int maxCount = sizeToFreq.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() == maxCount) {
                System.out.println("Самое частое количество повторений " +
                        entry.getKey() + " (встретилось " + entry.getValue() + " раз)");
            }
        }
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() < maxCount) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }
}