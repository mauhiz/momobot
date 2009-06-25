package net.mauhiz.irc;

import junit.framework.Assert;
import net.mauhiz.util.FileUtil;

import org.junit.Test;

public class EncodingTest {
    
    @Test
    public void testEncoding() {
        byte[] str1 = {(byte) 0xe9};
        Assert.assertEquals("é", new String(str1, FileUtil.ISO8859_15));
    }
}
