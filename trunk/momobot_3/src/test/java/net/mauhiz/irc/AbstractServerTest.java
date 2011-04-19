package net.mauhiz.irc;

import net.mauhiz.irc.base.data.IIrcDecoder;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcDecoder;
import net.mauhiz.irc.base.data.IrcNetwork;
import net.mauhiz.irc.base.data.IrcServerFactory;

/**
 * Effectue les tests avec un serveur type Qnet
 * 
 * @author mauhiz
 */
public abstract class AbstractServerTest {
    protected final IIrcDecoder DECODER;
    protected final IrcNetwork QNET;
    protected final IIrcServerPeer TO_QNET = IrcServerFactory.createServer("Quakenet", "irc://uk.quakenet.org:6667/");

    public AbstractServerTest() {
        super();
        TO_QNET.introduceMyself("momobot3", "mmb", "MMB v3");
        QNET = TO_QNET.getNetwork();
        DECODER = IrcDecoder.getInstance();
    }
}
