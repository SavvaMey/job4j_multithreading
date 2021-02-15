package thread;

public class Wget {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(
                () -> {
                    for (int i = 0; i < 100; i++) {
                        System.out.print("\rLoading : " + i + "%");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        thread.start();
        thread.join();
        System.out.println("Loaded main");
    }
}
