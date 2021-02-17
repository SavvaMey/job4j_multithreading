package waitnotify;

import org.junit.Test;
import syn.SingleLockList;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void add() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Set<Integer> rsl = new TreeSet<>();
        Thread first = new Thread(
                () -> {
                    int count = 0;
                    do {
                        count++;
                        queue.offer(count);
                    } while (count != 15);
                    System.out.println("завершил работу " + Thread.currentThread().getName());
                }, "продюсер один"
        );
        Thread second = new Thread(
                () -> {
                    for (int i = 0; i < 15; i++) {
                        rsl.add(queue.poll());
                    }
                    System.out.println("завершил работу " + Thread.currentThread().getName());
                }, "консюмер один"
        );
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(rsl, is(Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)));
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < 5; i++) {
                        queue.offer(i);
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        buffer.add(queue.poll());
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer, is(Arrays.asList(0, 1, 2, 3, 4)));
    }

}