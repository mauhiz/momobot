package net.mauhiz.irc.bouncer;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;

public class DummyAccountStore extends AccountStore {
    private final IIrcServerPeer serverPeer;

    public DummyAccountStore(IIrcServerPeer serverPeer) {
        super();
        this.serverPeer = serverPeer;
        serverPeer.introduceMyself("MomoBouncer", null, null);
    }

    @Override
    protected void load() {
        synchronized (ACCOUNTS) {
            Account acc = new Account();
            acc.setUsername("mauhiz");
            acc.setPassword("mauhiz");
            /* beware */
            acc.setServer(serverPeer.getNetwork());
            /* cette technique ne marche que pour 1 seul account */
            IrcUser myself = serverPeer.getMyself();
            myself.getMask().setUser(acc.getUsername());
            myself.setNick(acc.getUsername());
            ACCOUNTS.add(acc);
        }
    }
}
