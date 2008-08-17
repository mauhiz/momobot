/**
 * 
 */
package net.mauhiz.irc.base;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class ColorTest {
    
    /**
     * Test method for {@link net.mauhiz.irc.base.Color#toString()}.
     */
    @Test
    public void testToString() {
        Assert.assertEquals("00", Color.WHITE.toString());
    }
    
}
