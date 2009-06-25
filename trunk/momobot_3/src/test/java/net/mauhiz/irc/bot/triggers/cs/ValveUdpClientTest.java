package net.mauhiz.irc.bot.triggers.cs;

import java.nio.ByteBuffer;

import junit.framework.Assert;
import net.mauhiz.util.FileUtil;

import org.junit.Test;

public class ValveUdpClientTest {
    
    private static void testArr(byte[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            Assert.assertEquals((byte) 0xff, arr[i]);
        }
    }
    
    @Test
    public void testMoinsUn() {
        // byte[] m1 = ValveUdpClient.MOINS_UN.getBytes(FileUtil.ASCII);
        //        
        // Assert.assertEquals(4, ValveUdpClient.MOINS_UN.length());
        // Assert.assertEquals(4, ByteBuffer.wrap(ValveUdpClient.MOINS_UN.getBytes(FileUtil.ASCII)).array().length);
        // for (int i = 0; i < m1.length; ++i) {
        // Assert.assertEquals(0xff, m1[i]);
        // }
        
        byte[] tbb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(-1).array();
        testArr(tbb);
        
        byte[] m1 = new String(tbb, FileUtil.ISO8859_15).getBytes(FileUtil.ISO8859_15);
        testArr(m1);
    }
}
