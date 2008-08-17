package net.mauhiz.irc.base.model;

import java.util.Set;

import junit.framework.Assert;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;

import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class ChannelsTest {
    
    /**
     * Test method for {@link net.mauhiz.irc.base.model.Channels#getChannels(net.mauhiz.irc.base.data.IrcUser)}.
     */
    @Test
    public void testGetChannels() {
        /* init */
        IrcServer bidon = new IrcServer("irc://irc.quakenet.org:6667/");
        bidon.setMyNick("momobot");
        Channels channels = Channels.getInstance(bidon);
        IrcUser truite = Users.getInstance(bidon).findUser(bidon.getMyNick(), true);
        channels.get("#tsi.fr").add(truite);
        channels.get("#-duck-").add(truite);
        /* get */
        IrcUser mmb = Users.getInstance(bidon).findUser(bidon.getMyNick(), false);
        Set<Channel> myChans = channels.getChannels(mmb);
        Assert.assertEquals(2, myChans.size());
    }
    
}
