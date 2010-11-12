package net.mauhiz.irc.bouncer;

import java.util.Collection;
import java.util.Collections;

import net.mauhiz.irc.base.data.AbstractIrcServer;
import net.mauhiz.irc.base.data.IrcChannel;

public class BncServer extends AbstractIrcServer {
    
    protected BncServer(String uriStr) {
        super(uriStr);
    }
    
    @Override
    public int getLineMaxLength() {
        return 512;
    }
    
    @Override
    public Collection<String> getServiceNicks() {
        return Collections.emptySet();
    }
    
    @Override
    public IrcChannel newChannel(String chanLowerCase) {
        return null;
    }
    
    @Override
    public BncUser newUser(String nick) {
        return null;
    }
}
