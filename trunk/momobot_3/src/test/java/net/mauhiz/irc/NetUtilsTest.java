package net.mauhiz.irc;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class NetUtilsTest {
    /**
     * 192.168.0.255
     */
    private static final byte[] BYTES_CLASS_C_MASK = new byte[]{-64, -88, 0, -1};
    /**
     * 192.168.0.255
     */
    private static final char[] CHARS_CLASS_C_MASK = new char[]{192, 168, 0, 255};
    /**
     * 192.168.0.255
     */
    private static final int INT_CLASS_C_MASK = 192 * 256 * 256 * 256 + 168 * 256 * 256 + 255;
    /**
     * 192.168.0.255
     */
    private static final long LONG_CLASS_C_MASK = (long) 192 * 256 * 256 * 256 + 168 * 256 * 256 + 255;
    
    /**
     * Test method for {@link net.mauhiz.irc.NetUtils#byteTabToSignedInt(byte[])}.
     * 
     * @throws UnknownHostException
     */
    @Test
    public void testBytesToInt() throws UnknownHostException {
        int toInt = NetUtils.byteTabToSignedInt(BYTES_CLASS_C_MASK);
        Assert.assertEquals(INT_CLASS_C_MASK, toInt);
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.NetUtils#charTabToIa(char[])}.
     */
    @Test
    public void testIntTabToIp() {
        InetAddress localhost = NetUtils.charTabToIa(CHARS_CLASS_C_MASK);
        Assert.assertEquals("192.168.0.255", localhost.getHostAddress());
    }
    /**
     * Test method for {@link net.mauhiz.irc.NetUtils#intToBytes(int)}.
     */
    @Test
    public void testIntToBytes() {
        byte[] toBytes = NetUtils.intToBytes(INT_CLASS_C_MASK);
        Assert.assertEquals(BYTES_CLASS_C_MASK.length, toBytes.length);
        for (int i = 0; i < BYTES_CLASS_C_MASK.length; ++i) {
            Assert.assertEquals(BYTES_CLASS_C_MASK[i], toBytes[i]);
        }
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.NetUtils#iaToLong(InetAddress)}.
     * 
     * @throws UnknownHostException
     */
    @Test
    public void testIpToLong() throws UnknownHostException {
        InetAddress localhost = InetAddress.getByName("192.168.0.255");
        Assert.assertEquals(LONG_CLASS_C_MASK, NetUtils.iaToLong(localhost));
    }
    /**
     * Test method for {@link net.mauhiz.irc.NetUtils#longToCharTab(long)}.
     */
    @Test
    public void testLongToIntTab() {
        char[] maskc = NetUtils.longToCharTab(LONG_CLASS_C_MASK);
        Assert.assertEquals(CHARS_CLASS_C_MASK.length, maskc.length);
        for (int i = 0; i < CHARS_CLASS_C_MASK.length; ++i) {
            Assert.assertEquals(CHARS_CLASS_C_MASK[i], maskc[i]);
        }
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.NetUtils#makeISA(java.lang.String)}.
     */
    @Test
    public void testMakeISA() {
        InetSocketAddress add1 = NetUtils.makeISA("127.0.0.1:27015");
        Assert.assertEquals(27015, add1.getPort());
        Assert.assertEquals("localhost", add1.getHostName());
    }
}
