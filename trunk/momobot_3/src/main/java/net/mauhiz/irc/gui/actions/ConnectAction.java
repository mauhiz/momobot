package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.gui.SwtIrcClient;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class ConnectAction extends AbstractAction {
    private final SwtIrcClient ircClient;
    private final IrcServer server;

    public ConnectAction(SwtIrcClient ircClient, IrcServer server1) {
        server = server1;
        this.ircClient = ircClient;
    }

    @Override
    protected void doAction() {
        ircClient.doConnect(server);
    }

    @Override
    protected boolean isAsynchronous() {
        return true; // this action takes time
    }
}
