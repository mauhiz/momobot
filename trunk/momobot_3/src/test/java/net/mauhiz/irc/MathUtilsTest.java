package net.mauhiz.irc;

import junit.framework.Assert;

import net.mauhiz.util.Power;

import org.junit.Test;

/**
 * @author mauhiz
 */
public class MathUtilsTest {
    
    @Test
    public void testPower() {
        Assert.assertEquals(81, Power.power(3, 4));
        Assert.assertEquals(0, Power.power(0, 7));
        Assert.assertEquals(1, Power.power(0, 0));
        Assert.assertEquals(1, Power.power(1, 10));
        Assert.assertEquals(32, Power.power(2, 5));
    }
}
