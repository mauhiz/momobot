package net.mauhiz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static final Charset ASCII = Charset.forName("ASCII");
    public static final Charset ISO8859_15 = Charset.forName("ISO-8859-15");
    public static final Charset UTF8 = Charset.forName("UTF-8");
    
    public static List<String> readFileInCp(String fileName, Charset encoding) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            return readLines(is, encoding);
        } finally {
            is.close();
        }
    }
    
    static List<String> readLines(InputStream is, Charset encoding) throws IOException {
        Reader reader = new InputStreamReader(is, encoding);
        try {
            return readLines(reader);
        } finally {
            reader.close();
        }
    }
    
    static List<String> readLines(Reader reader) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader bReader = new BufferedReader(reader);
        try {
            while (true) {
                String line = bReader.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } finally {
            bReader.close();
        }
        return lines;
    }
}
