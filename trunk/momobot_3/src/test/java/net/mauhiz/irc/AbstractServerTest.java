package net.mauhiz.irc;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetServer;

public class AbstractServerTest {
    
    protected static final IrcServer QNET = new QnetServer("irc://uk.quakenet.org:6667/");
    
    static {
        QNET.setAlias("QuakeNet");
        
        IrcUser myself = QNET.newUser("momobot3");
        myself.setUser("mmb");
        myself.setFullName("MMB v3");
        QNET.setMyself(myself);
    }
}
