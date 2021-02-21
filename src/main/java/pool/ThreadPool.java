package pool;

import waitnotify.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {
    private final int size = Runtime.getRuntime().availableProcessors();
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(10);
    private final Runnable thread = () -> {
        while (!Thread.interrupted()) {
            try {
                tasks.poll().run();
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    };

    public ThreadPool() {
        for (int i = 0; i < size; i++) {
            threads.add(new Thread(thread));
        }
        threads.forEach(Thread::start);
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public void shutdown() {
        for (Thread threadUniq : threads) {
            threadUniq.interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPool threadPool = new ThreadPool();
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threadPool.work(new Thread(
                    () -> System.out.println(finalI + "task")
            ));
        }
        threadPool.shutdown();
    }
}
