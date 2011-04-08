package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.gui.ChannelUpdateTrigger;
import net.mauhiz.irc.gui.GuiTriggerManager;
import net.mauhiz.irc.gui.SwtChanTab;
import net.mauhiz.irc.gui.SwtIrcClient;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class JoinAction extends AbstractAction {
    String channel;
    ChannelUpdateTrigger cut;
    GuiTriggerManager gtm;
    IrcServer server;
    SwtIrcClient swtIrcClient;

    /**
     * @param gtm1
     * @param server1
     * @param channel1
     */
    public JoinAction(GuiTriggerManager gtm1, IrcServer server1, String channel1, ChannelUpdateTrigger cut,
            SwtIrcClient swtIrcClient) {
        server = server1;
        gtm = gtm1;
        channel = channel1;
        this.cut = cut;
        this.swtIrcClient = swtIrcClient;
    }

    @Override
    protected void doAction() {
        Join msg = new Join(server, channel);
        IIrcControl control = gtm.getClient();
        assert control != null;
        control.sendMsg(msg);
        SwtChanTab tab = swtIrcClient.createTab(channel);
        cut.addChannel(server.findChannel(channel, true), tab.getUsersInChan());
    }

    @Override
    protected boolean isAsynchronous() {
        return false; // sending the message should be quick
    }
}
