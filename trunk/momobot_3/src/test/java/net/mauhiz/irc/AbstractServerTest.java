package net.mauhiz.irc;

import net.mauhiz.irc.base.data.IIrcDecoder;
import net.mauhiz.irc.base.data.IrcDecoder;
import net.mauhiz.irc.base.data.IrcNetwork;
import net.mauhiz.irc.base.data.IrcServerFactory;
import net.mauhiz.irc.base.data.IIrcServerPeer;
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
    protected static final IIrcDecoder DECODER;
    protected static final IrcNetwork QNET;
    protected static final IIrcServerPeer TO_QNET = IrcServerFactory.createServer("irc://uk.quakenet.org:6667/");

    static {
        Assert.assertTrue(TO_QNET instanceof QnetServer);
        IrcUser myself = TO_QNET.introduceMyself("momobot3", "mmb", "MMB v3");
        Assert.assertTrue(myself instanceof QnetUser);
        QNET = TO_QNET.getNetwork();
        DECODER = IrcDecoder.getInstance();
    }
}
