package net.mauhiz.irc.base.data.qnet;

import net.mauhiz.irc.base.data.AbstractIrcServer;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author mauhiz
 */
public class QnetServer extends AbstractIrcServer {
    
    /**
     * @param uriStr
     */
    public QnetServer(final String uriStr) {
        super(uriStr);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.AbstractIrcServer#newChannel(java.lang.String)
     */
    @Override
    protected IrcChannel newChannel(final String chanLowerCase) {
        return new QnetChannel(chanLowerCase);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.AbstractIrcServer#newUser(java.lang.String)
     */
    @Override
    public IrcUser newUser(final String nick) {
        return new QnetUser(nick);
    }
    
}
