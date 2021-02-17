package nonblockingalg;

import commonresources.DCLSingleton;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>(0);

    public void increment() {
        Integer ref;
        Integer tmp;
        do {
            ref = count.get();
            tmp = ref + 1;
        } while (!count.compareAndSet(ref, tmp));
    }

    public int get() {
        return count.get();
    }

    public static void main(String[] args) throws InterruptedException {
        CASCount cas = new CASCount();
        Thread threadOne = new Thread(
                () -> {
                    for (int i = 0; i < 100; i++) {
                        cas.increment();
                    }
                }
        );
        Thread threadTwo = new Thread(
                () -> {
                    for (int i = 0; i < 100; i++) {
                        cas.increment();
                    }
                }
        );
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
        System.out.println(cas.get());
    }
}
