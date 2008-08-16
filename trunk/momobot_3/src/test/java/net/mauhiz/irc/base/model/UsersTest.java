package net.mauhiz.irc.base.model;

import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class UsersTest {
    static IrcServer qnet;
    static {
        qnet = new IrcServer("irc://uk.quakenet.org:6667/");
        qnet.setMyLogin("mmb");
        qnet.setMyFullName("momobot le 3eme");
        qnet.setAlias("Quakenet");
        qnet.setMyNick("momobot3");
    }
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        Users.get(qnet);
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.base.model.Channels#getChannels(net.mauhiz.irc.base.data.IrcUser)}.
     */
    @Test
    public void testMultiChan() {
        Channel chan = Channels.get(qnet).getChannel("#tsi.Fr");
        IrcUser peon = Users.get(qnet).findUser("Truite", true);
        IrcUser peon2 = Users.get(qnet).findUser("Gruiiik", true);
        chan.add(peon);
        chan.add(peon2);
        Channel chan2 = Channels.get(qnet).getChannel("#-duCk-");
        chan2.add(peon2);
        Assert.assertEquals(2, Channels.get(qnet).getChannels(peon2).size());
        Assert.assertEquals(2, Users.get(qnet).size());
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.base.model.Users#isKnown(IrcUser)}
     */
    @Test
    public void testSingleChan() {
        Channel chan = Channels.get(qnet).getChannel("#tsi.Fr");
        Channel chan2 = Channels.get(qnet).getChannel("#tsi.fr");
        Assert.assertTrue(chan == chan2);
        IrcUser peon = Users.get(qnet).findUser("Truite", true);
        IrcUser peon2 = Users.get(qnet).findUser("Gruiiik", true);
        chan.add(peon);
        chan.add(peon2);
        Assert.assertTrue(Users.get(qnet).isKnown(peon));
        Assert.assertTrue(Users.get(qnet).isKnown(peon2));
        Assert.assertEquals(2, chan.size());
    }
}
