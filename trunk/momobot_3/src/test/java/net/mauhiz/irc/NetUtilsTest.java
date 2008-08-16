package net.mauhiz.irc;

import java.net.InetSocketAddress;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class NetUtilsTest {
    
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
