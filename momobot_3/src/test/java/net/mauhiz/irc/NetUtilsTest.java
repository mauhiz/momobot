package net.mauhiz.irc;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import net.mauhiz.util.NetUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class NetUtilsTest {
    /**
     * 192.168.0.255
     */
    private static final long LONG_CLASS_C_MASK = (long) 192 * 256 * 256 * 256 + 168 * 256 * 256 + 255;
    /**
     * 192.168.0.255
     */
    private static final String STR_CLASS_C_MASK = "192.168.0.255";

    @Test
    public void testIaToLong() throws UnknownHostException {
        Assert.assertEquals(LONG_CLASS_C_MASK, NetUtils.iaToLong(InetAddress.getByName(STR_CLASS_C_MASK)));
    }

    @Test
    public void testLongToIa() throws UnknownHostException {
        Assert.assertEquals(InetAddress.getByName(STR_CLASS_C_MASK), NetUtils.longToIa(LONG_CLASS_C_MASK));
    }

    @Test
    public void testMakeISA() {
        InetSocketAddress add1 = (InetSocketAddress) NetUtils.makeISA("127.0.0.1:27015");
        Assert.assertEquals(27015, add1.getPort());
        Assert.assertEquals("127.0.0.1", add1.getAddress().getCanonicalHostName());
    }
}
