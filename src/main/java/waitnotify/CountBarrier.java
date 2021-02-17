package waitnotify;

public class CountBarrier {
    private final Object monitor = this;

    private final int total;

    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            count++;
            monitor.notifyAll();
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count != total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        CountBarrier barrier = new CountBarrier(10);
        Thread master = new Thread(
                () -> {
                    for (int i = 0; i <= 10; i++) {
                        barrier.count();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("hello from counter +" + i);

                    }
                },
                "counter"
        );
        Thread slave = new Thread(
                () -> {
                    barrier.await();
                    System.out.println("no wait anymore");
                },
                "Slave"
        );
        master.start();
        slave.start();
    }
}
