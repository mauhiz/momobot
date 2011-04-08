package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.gui.ChannelUpdateTrigger;
import net.mauhiz.irc.gui.GuiTriggerManager;
import net.mauhiz.util.AbstractAction;

import org.eclipse.swt.widgets.List;

/**
 * @author mauhiz
 */
public class JoinAction extends AbstractAction {
    String channel;
    ChannelUpdateTrigger cut;
    GuiTriggerManager gtm;
    IrcServer server;
    List userList;

    /**
     * @param gtm1
     * @param server1
     * @param channel1
     */
    public JoinAction(GuiTriggerManager gtm1, IrcServer server1, String channel1, ChannelUpdateTrigger cut,
            List userList) {
        server = server1;
        gtm = gtm1;
        channel = channel1;
        this.cut = cut;
        this.userList = userList;
    }

    @Override
    protected void doAction() {
        Join msg = new Join(server, channel);
        IIrcControl control = gtm.getClient();
        assert control != null;
        control.sendMsg(msg);
        cut.addChannel(server.findChannel(channel, true), userList);
    }

    @Override
    protected boolean isAsynchronous() {
        return false; // sending the message should be quick
    }
}
