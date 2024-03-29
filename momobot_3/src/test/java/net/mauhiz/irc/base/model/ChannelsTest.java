package net.mauhiz.irc.base.model;

import java.util.Set;

import junit.framework.Assert;
import net.mauhiz.irc.AbstractServerTest;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;

import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class ChannelsTest extends AbstractServerTest {

    /**
     * Test method for {@link net.mauhiz.irc.base.data.IrcNetwork#getChannelsForUser(IrcUser)}.
     */
    @Test
    public void testGetChannels() {

        IrcUser truite = QNET.findUser(TO_QNET.getMyself().getNick(), true);
        QNET.findChannel("#tsi.fr", true).add(truite);
        QNET.findChannel("#-duck-", true).add(truite);
        /* get */
        IrcUser mmb = QNET.findUser(TO_QNET.getMyself().getNick(), false);
        Set<IrcChannel> myChans = QNET.getChannelsForUser(mmb);
        Assert.assertEquals(2, myChans.size());
    }

}
