package net.mauhiz.irc.bouncer;

import net.mauhiz.irc.base.data.IrcNetwork;

/**
 * @author mauhiz
 */
public class Account {
    private String password;
    private transient BouncerTriggerManager relatedManager = new BouncerTriggerManager();
    private IrcNetwork server;
    // private long startTime;
    private String username;

    public String getPassword() {
        return password;
    }

    public BouncerTriggerManager getRelatedManager() {
        return relatedManager;
    }

    public IrcNetwork getServer() {
        return server;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRelatedManager(BouncerTriggerManager relatedManager) {
        this.relatedManager = relatedManager;
    }

    public void setServer(IrcNetwork server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
