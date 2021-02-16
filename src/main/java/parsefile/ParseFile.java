package parsefile;

import java.io.*;

public class ParseFile {
    private File file;

    public synchronized void setFile(File f) {
        file = f;
    }

    public synchronized File getFile() {
        return file;
    }

    public String getContent() throws IOException {
        BufferedInputStream i = new BufferedInputStream(new FileInputStream(file));
        StringBuilder output = new StringBuilder();
        int data;
        while ((data = i.read()) > 0) {
            output.append((char) data);
        }
        return output.toString();
    }

    public synchronized String getContentWithoutUnicode() throws IOException {
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        StringBuilder output = new StringBuilder();
        int data;
        while ((data = reader.read()) > 0) {
            if (data < 0x80) {
                output.append((char) data);
            }
        }
        return output.toString();
    }

    public synchronized void saveContent(String content) {
        try (PrintStream printer = new PrintStream(file)) {
            printer.print(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
