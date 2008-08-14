package net.mauhiz.irc.bot.event;

import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcUser;

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
        Channel chan = new Channel("#tsi.fr");
        Gather gather = new Gather(chan);
        SeekWar seekwar = new SeekWar(gather);
        IrcUser user1 = new IrcUser("a");
        gather.add(user1);
        Assert.assertEquals(seekwar.submitChannelMessage("1vs1 off mid"), true);
    }
}
