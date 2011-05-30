package net.mauhiz.irc;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;
import net.mauhiz.util.FileUtil;

import org.junit.Test;

public class EncodingTest {

    @Test
    public void testEncoding() throws UnsupportedEncodingException {
        byte[] str1 = { (byte) 0xe9 };
        Assert.assertEquals("é", new String(str1, FileUtil.ISO8859_15.name()));
    }
}
