package net.mauhiz.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FileUtil {
    public static final Charset ASCII = Charset.forName("ASCII");
    public static final Charset ISO8859_15 = Charset.forName("ISO-8859-15");
    public static final Charset UTF8 = Charset.forName("UTF-8");
    
    public static List<String> readFileInCp(String fileName, Charset encoding) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            return IOUtils.readLines(is, encoding.name());
        } finally {
            is.close();
        }
    }
}