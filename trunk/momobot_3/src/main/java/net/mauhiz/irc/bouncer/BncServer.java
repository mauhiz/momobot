package net.mauhiz.irc.bouncer;

import java.util.Collection;
import java.util.Collections;

import net.mauhiz.irc.base.data.AbstractIrcNetwork;
import net.mauhiz.irc.base.data.IrcChannel;

public class BncServer extends AbstractIrcNetwork {

    protected BncServer(String alias) {
        super(alias);
    }

    @Override
    public int getLineMaxLength() {
        return 512;
    }

    public Collection<String> getServiceNicks() {
        return Collections.emptySet();
    }

    public IrcChannel newChannel(String chanLowerCase) {
        return null;
    }

    public BncUser newUser(String nick) {
        return null;
    }
}
