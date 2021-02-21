package thread;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class FileDownload {
    public static void main(String[] args) throws Exception {
        String file = "https://raw.githubusercontent.com/peterarsentev/course_test/master/pom.xml";
        try (BufferedInputStream in = new BufferedInputStream(new URL(file).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("pom_tmp.xml")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                System.out.println(bytesRead);
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                Thread.sleep(1000);
            }
//            for (int i = 0; i < dataBuffer.length; i++) {
//                System.out.println(dataBuffer[i]);
//
//            }
//            bytesRead = in.read(dataBuffer, 0, 1024);
//            System.out.println(bytesRead);
//            for (int i = 0; i < dataBuffer.length; i++) {
//                System.out.println(dataBuffer[i]);
//
//            }
//            fileOutputStream.write(dataBuffer, 0, bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
