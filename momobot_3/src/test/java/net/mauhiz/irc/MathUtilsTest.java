package net.mauhiz.irc;

import junit.framework.Assert;

import net.mauhiz.util.MathUtils;

import org.junit.Test;

/**
 * @author mauhiz
 */
public class MathUtilsTest {
    
    @Test
    public void testPower() {
        Assert.assertEquals(81, MathUtils.power(3, 4));
        Assert.assertEquals(0, MathUtils.power(0, 7));
        Assert.assertEquals(1, MathUtils.power(0, 0));
        Assert.assertEquals(1, MathUtils.power(1, 10));
        Assert.assertEquals(32, MathUtils.power(2, 5));
    }
}
