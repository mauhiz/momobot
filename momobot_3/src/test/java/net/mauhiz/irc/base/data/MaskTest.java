package net.mauhiz.irc.base.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class MaskTest {
    /**
     * Test method for {@link net.mauhiz.irc.base.data.Mask#Mask(String)}.
     */
    @Test
    public void testMask1() {
        Mask m = new Mask("~truite!poisson@mauhiz.net");
        Assert.assertEquals("~truite", m.getNick());
        Assert.assertEquals("poisson", m.getUser());
        Assert.assertEquals("mauhiz.net", m.getHost());
    }

    @Test
    public void testMask2() {
        Mask m = new Mask("qwebirc89143!webchat@41.224.168.142");
        Assert.assertEquals("qwebirc89143", m.getNick());
        Assert.assertEquals("webchat", m.getUser());
        Assert.assertEquals("41.224.168.142", m.getHost());
    }

    //
}
