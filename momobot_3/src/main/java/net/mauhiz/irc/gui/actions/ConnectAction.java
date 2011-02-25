package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.gui.GuiTriggerManager;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class ConnectAction extends AbstractAction {
    private final GuiTriggerManager gtm;
    private final IrcServer server;

    /**
     * @param gtm1
     * @param server1
     */
    public ConnectAction(GuiTriggerManager gtm1, IrcServer server1) {
        server = server1;
        gtm = gtm1;
    }

    @Override
    protected void doAction() {
        gtm.getClient().connect(server);
    }
}
