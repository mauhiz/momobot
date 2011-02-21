package net.mauhiz.irc.bot.triggers.cs;

import java.nio.ByteBuffer;

import junit.framework.Assert;
import net.mauhiz.util.FileUtil;

import org.junit.Test;

public class ValveUdpClientTest {
    
    private static void isArrOk(byte[] arr) {
        for (byte element : arr) {
            Assert.assertEquals((byte) 0xff, element);
        }
    }
    
    @Test
    public void testMoinsUn() {
        // byte[] m1 = ValveUdpClient.MOINS_UN.getBytes(FileUtil.ASCII);
        //
        // Assert.assertEquals(4, ValveUdpClient.MOINS_UN.length());
        // Assert.assertEquals(4, ByteBuffer.wrap(ValveUdpClient.MOINS_UN.getBytes(FileUtil.ASCII)).array().length);
        // for (byte m : m1) {
        // Assert.assertEquals(0xff, m);
        // }
        
        byte[] tbb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(-1).array();
        isArrOk(tbb);
        
        byte[] m1 = new String(tbb, FileUtil.ISO8859_15).getBytes(FileUtil.ISO8859_15);
        isArrOk(m1);
    }
}
