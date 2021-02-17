package waitnotify;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("lock")
    private Queue<T> queue = new LinkedList<>();
    private final int LIMIT = 10;
    private final Object lock = new Object();

    public void offer(T value) throws InterruptedException {
        synchronized (lock) {
            while (queue.size() == LIMIT) {
                lock.wait();
            }
            queue.offer(value);
            lock.notifyAll();
        }
    }

    public T poll() throws InterruptedException {
        synchronized (lock) {
            while (queue.size() == 0) {
                lock.wait();
            }
            T value = queue.poll();
            System.out.println(queue.size() + " -razmer; "
                    + value + " -вытащали значение");
            lock.notifyAll();
            Thread.sleep(300);
            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        Thread threadProduceOne = new Thread(
                () -> {
                    int count = 0;
                    do {
                        count++;
                        try {
                            queue.offer(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (count <= 50);
                    System.out.println("завершил работу " + Thread.currentThread().getName());
                }, "продюсер один"
        );

        Thread threadConsumerOne = new Thread(
                () -> {
                    for (int i = 0; i <= 25; i++) {
                        try {
                            queue.poll();
                            System.out.println("консьюмер 1 вытащил");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("завершил работу " + Thread.currentThread().getName());
                }, "консюмер один"
        );

        Thread threadConsumerTwo = new Thread(
                () -> {
                    for (int i = 26; i <= 50; i++) {
                        try {
                            queue.poll();
                            System.out.println("консьюмер 2 вытащил");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("завершил работу " + Thread.currentThread().getName());
                }, "консюмер два"
        );

        threadConsumerOne.start();
        threadConsumerTwo.start();
        threadProduceOne.start();

        threadConsumerOne.join();
        threadConsumerTwo.join();
        threadProduceOne.join();
    }
}
