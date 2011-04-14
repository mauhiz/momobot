package net.mauhiz.irc.bouncer;

import net.mauhiz.irc.base.data.IrcServerFactory;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;

public class DummyAccountStore extends AccountStore {
    private static final IIrcServerPeer QNET;

    static {
        QNET = IrcServerFactory.createServer("irc://uk.quakenet.org:6667/");
        QNET.introduceMyself("MomoBouncer", null, null);
    }

    @Override
    protected void load() {
        synchronized (ACCOUNTS) {
            Account acc = new Account();
            acc.setUsername("mauhiz");
            acc.setPassword("mauhiz");
            /* beware */
            acc.setServer(QNET.getNetwork());
            /* cette technique ne marche que pour 1 seul account */
            IrcUser myself = QNET.getMyself();
            myself.getMask().setUser(acc.getUsername());
            myself.setNick(acc.getUsername());
            ACCOUNTS.add(acc);
        }
    }
}
