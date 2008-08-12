package net.mauhiz.irc.base.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class MaskTest {
    /**
     * Test method for {@link net.mauhiz.irc.base.data.Mask#buildMask()}.
     */
    @Test
    public void testBuildMask() {
        Mask m = new Mask("~truite!poisson@mauhiz.net");
        Assert.assertEquals("~truite", m.nick);
        Assert.assertEquals("poisson", m.user);
        Assert.assertEquals("mauhiz.net", m.host);
    }
}
