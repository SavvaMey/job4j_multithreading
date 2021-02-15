package thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class WgetFile implements Runnable {
    private final String url;
    private final int speed;

    public WgetFile(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("pom_tmp.xml")) {
            long before = System.currentTimeMillis();
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            bytesRead = in.read(dataBuffer, 0, 1024);

            long after = System.currentTimeMillis();
            long dist = speed * 1000L - (after - before);
            if (dist > 0) {
                Thread.sleep(dist);
            }
            if (bytesRead == -1) {
                return;
            }
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args "https://raw.githubusercontent.com/peterarsentev/course_test/master/pom.xml"
     *             10 sec
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new WgetFile(url, speed));
        wget.start();
        wget.join();
    }
}
