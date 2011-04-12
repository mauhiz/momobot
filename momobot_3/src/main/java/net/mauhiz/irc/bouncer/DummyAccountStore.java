package net.mauhiz.irc.bouncer;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcServerFactory;
import net.mauhiz.irc.base.data.IrcUser;

public class DummyAccountStore extends AccountStore {
    private static final IrcServer QNET;

    static {
        QNET = IrcServerFactory.createServer("irc://uk.quakenet.org:6667/");

        IrcUser myself = QNET.newUser("MomoBouncer");
        QNET.setMyself(myself);
    }

    @Override
    protected void load() {
        synchronized (ACCOUNTS) {
            Account acc = new Account();
            acc.setUsername("mauhiz");
            acc.setPassword("mauhiz");
            /* beware */
            acc.setServer(QNET);
            /* cette technique ne marche que pour 1 seul account */
            IrcUser myself = acc.getServer().getMyself();
            myself.getMask().setUser(acc.getUsername());
            myself.setNick(acc.getUsername());
            ACCOUNTS.add(acc);
        }
    }
}
