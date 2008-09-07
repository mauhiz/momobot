package net.mauhiz.irc.base.data.defaut;

import net.mauhiz.irc.base.data.AbstractIrcUser;
import net.mauhiz.irc.base.data.UserProperties;

/**
 * @author mauhiz
 */
public class DefaultUser extends AbstractIrcUser {
    /**
     * @param nick1
     */
    protected DefaultUser(final String nick1) {
        super(nick1);
        props = new UserProperties();
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#isService()
     */
    @Override
    public boolean isService() {
        return false;
    }
}
