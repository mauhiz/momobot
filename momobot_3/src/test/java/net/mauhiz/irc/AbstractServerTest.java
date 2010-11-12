package net.mauhiz.irc;

import net.mauhiz.irc.base.data.qnet.QnetServer;
import net.mauhiz.irc.base.data.qnet.QnetUser;

/**
 * Effectue les tests avec un serveur type Qnet
 * 
 * @author mauhiz
 */
public abstract class AbstractServerTest {
    protected final QnetServer QNET = new QnetServer("irc://uk.quakenet.org:6667/");
    
    public AbstractServerTest() {
        QNET.setAlias("QuakeNet");
        
        QnetUser myself = QNET.newUser("momobot3");
        myself.setUser("mmb");
        myself.setFullName("MMB v3");
        QNET.setMyself(myself);
    }
}
