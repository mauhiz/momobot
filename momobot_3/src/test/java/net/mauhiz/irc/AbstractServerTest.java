package net.mauhiz.irc;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcServerFactory;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetServer;
import net.mauhiz.irc.base.data.qnet.QnetUser;

import org.junit.Assert;

/**
 * Effectue les tests avec un serveur type Qnet
 * 
 * @author mauhiz
 */
public abstract class AbstractServerTest {
    protected final IrcServer QNET = IrcServerFactory.createServer("irc://uk.quakenet.org:6667/");

    public AbstractServerTest() {
        Assert.assertTrue(QNET instanceof QnetServer);
        IrcUser myself = QNET.newUser("momobot3");
        Assert.assertTrue(myself instanceof QnetUser);
        myself.getMask().setUser("mmb");
        myself.setFullName("MMB v3");
        QNET.setMyself(myself);
    }
}
