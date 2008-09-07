package net.mauhiz.irc.bot.event;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Topper
 * 
 */
public class SeekChannelMessageTest {
    
    /**
     * exemple de test
     */
    @Test
    public void testSplit() {
        SeekWar seekwar = new SeekWar();
        Assert.assertTrue(seekwar.submitChannelMessage("1vs1 off mid"));
    }
}
