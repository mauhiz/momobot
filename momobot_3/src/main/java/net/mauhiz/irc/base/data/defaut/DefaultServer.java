package net.mauhiz.irc.base.data.defaut;

import java.util.Collections;
import java.util.Set;

import net.mauhiz.irc.base.data.AbstractIrcNetwork;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author mauhiz
 * 
 */
public class DefaultServer extends AbstractIrcNetwork {

    public DefaultServer(String alias) {
        super(alias);
    }

    @Override
    public int getLineMaxLength() {
        return 127; // TODO confirm?
    }

    public Set<String> getServiceNicks() {
        return Collections.emptySet();
    }

    /**
     * @see net.mauhiz.irc.base.data.AbstractIrcNetwork#newChannel(java.lang.String)
     */
    public IrcChannel newChannel(String chanLowerCase) {
        return new DefaultChannel(chanLowerCase);
    }

    /**
     * @see net.mauhiz.irc.base.data.AbstractIrcNetwork#newUser(java.lang.String)
     */
    public IrcUser newUser(String nick) {
        return new DefaultUser(nick);
    }
}
