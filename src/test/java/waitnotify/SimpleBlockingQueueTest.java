package waitnotify;

import org.junit.Test;
import syn.SingleLockList;

import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void add() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        Set<Integer> rsl = new TreeSet<>();
        Thread first = new Thread(
                () -> {
                    int count = 0;
                    do {
                        count++;
                        try {
                            queue.offer(count);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (count != 15);
                    System.out.println("завершил работу " + Thread.currentThread().getName());
                }, "продюсер один"
        );
        Thread second = new Thread(
                () -> {
                    for (int i = 0; i < 15; i++) {
                        try {
                            rsl.add(queue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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

}