package net.mauhiz.irc.base.model;

import java.util.Set;

import junit.framework.Assert;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetServer;

import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class ChannelsTest {
    
    /**
     * Test method for {@link IrcServer#getChannelsForUser(IrcUser)}.
     */
    @Test
    public void testGetChannels() {
        /* init */
        IrcServer bidon = new QnetServer("irc://irc.quakenet.org:6667/");
        bidon.setMyNick("momobot");
        IrcUser truite = bidon.findUser(bidon.getMyNick(), true);
        bidon.findChannel("#tsi.fr", true).add(truite);
        bidon.findChannel("#-duck-", true).add(truite);
        /* get */
        IrcUser mmb = bidon.findUser(bidon.getMyNick(), false);
        Set<IrcChannel> myChans = bidon.getChannelsForUser(mmb);
        Assert.assertEquals(2, myChans.size());
    }
    
}
