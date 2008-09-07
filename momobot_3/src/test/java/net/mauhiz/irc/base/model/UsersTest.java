package net.mauhiz.irc.base.model;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetServer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class UsersTest {
    static IrcServer qnet;
    static {
        qnet = new QnetServer("irc://uk.quakenet.org:6667/");
        qnet.setMyLogin("mmb");
        qnet.setMyFullName("momobot le 3eme");
        qnet.setAlias("Quakenet");
        qnet.setMyNick("momobot3");
    }
    
    /**
     * 
     */
    @Test
    public void testMultiChan() {
        IrcChannel chan = qnet.findChannel("#tsi.Fr", true);
        IrcUser peon = qnet.findUser("Truite", true);
        IrcUser peon2 = qnet.findUser("Gruiiik", true);
        chan.add(peon);
        chan.add(peon2);
        IrcChannel chan2 = qnet.findChannel("#-duCk-", true);
        chan2.add(peon2);
        Assert.assertEquals(2, qnet.getChannelsForUser(peon2).size());
        Assert.assertEquals(2, qnet.countUsers());
    }
    
    /**
     * 
     */
    @Test
    public void testSingleChan() {
        IrcChannel chan = qnet.findChannel("#tsi.Fr", true);
        IrcChannel chan2 = qnet.findChannel("#tsi.fr", true);
        Assert.assertTrue(chan == chan2);
        IrcUser peon = qnet.findUser("Truite", true);
        IrcUser peon2 = qnet.findUser("Gruiiik", true);
        chan.add(peon);
        chan.add(peon2);
        Assert.assertNotNull(qnet.findUser(peon.getNick(), false));
        Assert.assertNotNull(qnet.findUser(peon.getNick(), false));
        Assert.assertEquals(2, chan.size());
    }
}
