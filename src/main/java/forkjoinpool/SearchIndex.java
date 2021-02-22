package forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class SearchIndex<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final int from;
    private final int to;
    private final T object;
    private static final int THRESHOLD = Runtime.getRuntime().availableProcessors();

    public SearchIndex(T[] array, int from, int to, T object) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.object = object;
    }

    @Override
    protected Integer compute() {
//        int  threshhold = (from - to + 1) / process;
        if (to - from <= array.length / THRESHOLD) {
            return searchAnswer(array);
        }
        int mid = (from + to) / 2;
        SearchIndex<T> left = new SearchIndex<>(array, from, mid, object);
        SearchIndex<T> right = new SearchIndex<>(array, mid + 1, to, object);
        left.fork();
        right.fork();
        Integer leftIndex = left.join();
        Integer rightIndex = right.join();
        return leftIndex != -1 ? leftIndex : rightIndex;
    }

    public int searchAnswer(T[] array) {
        for (int i = from; i <= to; i++) {
            if (array[i].equals(object)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int  initSearch(T[] array, T object) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new SearchIndex<T>(array, 0, array.length - 1, object));
    }

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        Integer[] array = new Integer[80000000];

        for (int i = 0; i < array.length; i++) {
            array[i] = ThreadLocalRandom.current().nextInt(-10000000, 10000000 + 1);
        }
        long before = System.currentTimeMillis();
        Integer object = array[79900000];
        int index = SearchIndex.initSearch(array, object);
        System.out.println(index);
        long after = System.currentTimeMillis();
        System.out.println(after - before + "многопоточное время");

        before = System.currentTimeMillis();
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(object)) {
                index = i;
                break;
            }
        }
        System.out.println(index);
        after = System.currentTimeMillis();
        System.out.println(after - before + " однопоточное время");
    }
}
