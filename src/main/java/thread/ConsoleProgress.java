package thread;

public class ConsoleProgress implements Runnable {
    String[] process = {"-", "\\", "|", "/"};

    @Override
    public void run() {
        int count = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(200);
                System.out.print("\r load: " + process[count++]);
                if (count > 3) {
                    count = 0;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(1000); /* симулируем выполнение параллельной задачи в течение 1 секунды. */
        progress.interrupt();
        progress.join();
        System.out.println(progress.getState());
    }
}
