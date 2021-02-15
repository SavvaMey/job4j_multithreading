package thread;

import jdk.swing.interop.SwingInterOpUtils;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println("first hello")
        );
        Thread second = new Thread(
                () -> System.out.println("second hello")
        );
        System.out.println(first.getState());
        first.start();
        second.start();
        while ((first.getState() != Thread.State.TERMINATED) &&
                (second.getState() != Thread.State.TERMINATED)) {
            System.out.println(first.getState() + " first");
            System.out.println(second.getState() + " second");
        }
        System.out.println(first.getState() + " FIRST");
        System.out.println(second.getState() + " SECOND");
        System.out.println("main end");
    }
}
