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
    public void testMask() {
        Mask m = new Mask("~truite!poisson@mauhiz.net");
        Assert.assertEquals("~truite", m.getNick());
        Assert.assertEquals("poisson", m.getUser());
        Assert.assertEquals("mauhiz.net", m.getHost());
    }
}
