package net.mauhiz.irc.base.model;

import net.mauhiz.irc.AbstractServerTest;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class UsersTest extends AbstractServerTest {
    
    /**
     * 
     */
    @Test
    public void testMultiChan() {
        IrcChannel chan = QNET.findChannel("#tsi.Fr", true);
        IrcUser peon = QNET.findUser("Truite", true);
        IrcUser peon2 = QNET.findUser("Gruiiik", true);
        chan.add(peon);
        chan.add(peon2);
        IrcChannel chan2 = QNET.findChannel("#-duCk-", true);
        chan2.add(peon2);
        Assert.assertEquals(2, QNET.getChannelsForUser(peon2).size());
        Assert.assertEquals(2, QNET.countUsers());
    }
    
    /**
     * 
     */
    @Test
    public void testSingleChan() {
        IrcChannel chan = QNET.findChannel("#tsi.Fr", true);
        IrcChannel chan2 = QNET.findChannel("#tsi.fr", true);
        Assert.assertTrue(chan == chan2);
        IrcUser peon = QNET.findUser("Truite", true);
        IrcUser peon2 = QNET.findUser("Gruiiik", true);
        chan.add(peon);
        chan.add(peon2);
        Assert.assertNotNull(QNET.findUser(peon.getNick(), false));
        Assert.assertNotNull(QNET.findUser(peon.getNick(), false));
        Assert.assertEquals(2, chan.size());
    }
}
