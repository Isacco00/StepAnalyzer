package stepanalyzer.utility;//
// Decompiled by Procyon v0.5.36
//

import java.io.*;

public class FileHandler {

    public FileHandler() {
    }

    public BufferedReader getFileReader(final File file) throws FileNotFoundException {
        final FileReader fileReader = new FileReader(file);
        return new BufferedReader(fileReader);
    }

    public BufferedReader getFileReader(final InputStream file) {
        return new BufferedReader(new InputStreamReader(file));
    }

    public PrintWriter getFileWriter(String fileName) throws IOException {
        return new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
    }
}
