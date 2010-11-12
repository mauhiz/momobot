package net.mauhiz.irc.base.data.defaut;

import java.util.Collections;
import java.util.Set;

import net.mauhiz.irc.base.data.AbstractIrcServer;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author mauhiz
 * 
 */
public class DefaultServer extends AbstractIrcServer {
    
    /**
     * @param uriStr
     */
    public DefaultServer(String uriStr) {
        super(uriStr);
    }
    
    @Override
    public int getLineMaxLength() {
        return 127; // TODO confirm?
    }
    
    @Override
    public Set<String> getServiceNicks() {
        return Collections.emptySet();
    }
    
    /**
     * @see net.mauhiz.irc.base.data.AbstractIrcServer#newChannel(java.lang.String)
     */
    @Override
    public IrcChannel newChannel(String chanLowerCase) {
        return new DefaultChannel(chanLowerCase);
    }
    
    /**
     * @see net.mauhiz.irc.base.data.AbstractIrcServer#newUser(java.lang.String)
     */
    @Override
    public IrcUser newUser(String nick) {
        return new DefaultUser(nick);
    }
}
