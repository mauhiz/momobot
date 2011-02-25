package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.gui.GuiTriggerManager;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class JoinAction extends AbstractAction {
    String channel;
    GuiTriggerManager gtm;
    IrcServer server;

    /**
     * @param gtm1
     * @param server1
     * @param channel1
     */
    public JoinAction(GuiTriggerManager gtm1, IrcServer server1, String channel1) {
        server = server1;
        gtm = gtm1;
        channel = channel1;
    }

    @Override
    protected void doAction() {
        Join msg = new Join(server, channel);
        gtm.getClient().sendMsg(msg);
    }
}
