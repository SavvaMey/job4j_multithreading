package waitnotify;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("lock")
    private Queue<T> queue = new LinkedList<>();
    private final int LIMIT;
    private final Object lock = new Object();

    public SimpleBlockingQueue(int LIMIT) {
        this.LIMIT = LIMIT;
    }

    public void offer(T value)  {
        synchronized (lock) {
            while (queue.size() == LIMIT) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.offer(value);
            lock.notifyAll();
        }
    }

    public T poll()  {
        synchronized (lock) {
            while (queue.size() == 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T value = queue.poll();
            System.out.println(queue.size() + " -razmer; "
                    + value + " -вытащали значение");
            lock.notifyAll();
//            Thread.sleep(300);
            return value;
        }
    }

    public boolean isEmpty() {
        synchronized (lock) {
            return queue.isEmpty();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread threadProduceOne = new Thread(
                () -> {
                    int count = 0;
                    do {
                        count++;
                        queue.offer(1);
                    } while (count <= 50);
                    System.out.println("завершил работу " + Thread.currentThread().getName());
                }, "продюсер один"
        );

        Thread threadConsumerOne = new Thread(
                () -> {
                    for (int i = 0; i <= 25; i++) {
                        queue.poll();
                        System.out.println("консьюмер 1 вытащил");
                    }
                    System.out.println("завершил работу " + Thread.currentThread().getName());
                }, "консюмер один"
        );

        Thread threadConsumerTwo = new Thread(
                () -> {
                    for (int i = 26; i <= 50; i++) {
                        queue.poll();
                        System.out.println("консьюмер 2 вытащил");
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
