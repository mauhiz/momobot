package net.mauhiz.irc.base.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class MaskTest {
    /**
     * Test method for {@link HostMask#getInstance(String)}.
     */
    @Test
    public void testMask1() {
        HostMask m = HostMask.getInstance("~truite!poisson@mauhiz.net");
        Assert.assertEquals("~truite", m.getNick());
        Assert.assertEquals("poisson", m.getUser());
        Assert.assertEquals("mauhiz.net", m.getHost());
    }

    @Test
    public void testMask2() {
        HostMask m = HostMask.getInstance("qwebirc89143!webchat@41.224.168.142");
        Assert.assertEquals("qwebirc89143", m.getNick());
        Assert.assertEquals("webchat", m.getUser());
        Assert.assertEquals("41.224.168.142", m.getHost());
    }
}
