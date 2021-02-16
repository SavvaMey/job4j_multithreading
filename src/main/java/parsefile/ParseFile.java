package parsefile;

import java.io.*;
import java.util.function.Predicate;

public class ParseFile {
    private File file;

    public synchronized void setFile(File f) {
        file = f;
    }

    public synchronized File getFile() {
        return file;
    }

    public synchronized String getContent(Predicate<Integer> pred) {
        StringBuilder output = new StringBuilder();
        try (BufferedInputStream i = new BufferedInputStream(new FileInputStream(file))) {
            int data;
            while ((data = i.read()) > 0) {
                if (pred.test(data)) {
                    output.append((char) data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public Predicate<Integer> getContentAll() {
        return data -> true;
    }

    public Predicate<Integer> getContentWithoutUnicode() {
        return data -> data < 0x80;
    }

    public synchronized void saveContent(String content) {
        try (PrintStream printer = new PrintStream(file)) {
            printer.print(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
