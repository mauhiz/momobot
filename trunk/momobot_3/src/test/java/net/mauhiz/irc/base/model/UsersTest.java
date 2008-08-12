/**
 * 
 */
package net.mauhiz.irc.base.model;

import java.net.URISyntaxException;

import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.model.Users;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class UsersTest {
    static IrcServer qnet;
    static {
        try {
            qnet = new IrcServer("irc://uk.quakenet.org:6667/");
        } catch (URISyntaxException e) {
            throw new ExceptionInInitializerError(e);
        }
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
     * Test method for {@link net.mauhiz.irc.base.model.Users#getChannels(net.mauhiz.irc.base.data.IrcUser)}.
     */
    @Test
    public void testMultiChan() {
        Channel chan = new Channel("#tsi.fr");
        IrcUser peon = new IrcUser("Truite");
        IrcUser peon2 = new IrcUser("Gruiiik");
        Users.get(qnet).put(chan, peon);
        Users.get(qnet).put(chan, peon2);
        Channel chan2 = new Channel("#-duCk-");
        Users.get(qnet).put(chan2, peon2);
        Assert.assertTrue(2 == Users.get(qnet).getChannels(peon2).size());
        Assert.assertTrue(2 == Users.get(qnet).countUsers());
    }

    /**
     * Test method for
     * {@link net.mauhiz.irc.base.model.Users#put(net.mauhiz.irc.base.data.Channel, net.mauhiz.irc.base.data.IrcUser)}.
     */
    @Test
    public void testSingleChan() {
        Channel chan = new Channel("#tsi.fr");
        IrcUser peon = new IrcUser("Truite");
        IrcUser peon2 = new IrcUser("Gruiiik");
        Users.get(qnet).put(chan, peon);
        Users.get(qnet).put(chan, peon2);
        Assert.assertTrue(Users.get(qnet).isKnown(peon));
        Assert.assertTrue(Users.get(qnet).isKnown(peon2));
        Assert.assertTrue(2 == Users.get(qnet).getUsers(chan).size());
    }
}
