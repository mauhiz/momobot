package net.mauhiz.irc;

import net.mauhiz.util.Morse;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class MorseTest {
    
    /**
     * Test method for {@link net.mauhiz.util.Morse#fromMorse(java.lang.String)}.
     */
    @Test
    public void testFromMorse() {
        String morse = "... --- ...";
        String sos = Morse.fromMorse(morse);
        Assert.assertEquals("SOS", sos);
    }
    
    /**
     * Test method for {@link net.mauhiz.util.Morse#toMorse(java.lang.String)}.
     */
    @Test
    public void testToMorse() {
        String sos = "SOS";
        String morse = Morse.toMorse(sos);
        Assert.assertEquals("...---...", morse);
    }
}
