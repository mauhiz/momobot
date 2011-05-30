package net.mauhiz.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FileUtil {
    public static final Charset ASCII = Charset.forName("ASCII");
    public static final Charset ISO8859_15 = Charset.forName("ISO-8859-15");
    public static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * Java-5 friendly
     */
    public static byte[] getBytes(String string, Charset charset) {
        try {
            return string.getBytes(charset.name());
        } catch (UnsupportedEncodingException uee) {
            throw new InternalError();
        }
    }

    /**
     * Java-5 friendly
     */
    public static String newString(byte[] bytes, Charset charset) {
        try {
            return new String(bytes, charset.name());
        } catch (UnsupportedEncodingException uee) {
            throw new InternalError();
        }
    }

    public static List<String> readFileInCp(String fileName, Charset encoding) throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            return IOUtils.readLines(is, encoding.name());
        } finally {
            is.close();
        }
    }
}
