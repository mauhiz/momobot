package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.gui.GuiTriggerManager;
import net.mauhiz.util.AbstractAction;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Text;

/**
 * @author mauhiz
 */
public class SendAction extends AbstractAction {
    Text bar;
    GuiTriggerManager gtm;
    IrcServer server;
    String target;

    /**
     * @param bar1
     * @param gtm1
     * @param server1
     * @param target1
     */
    public SendAction(Text bar1, GuiTriggerManager gtm1, IrcServer server1, String target1) {
        gtm = gtm1;
        bar = bar1;
        server = server1;
        target = target1;
    }

    @Override
    protected void doAction() {
        String toSend = bar.getText();
        if (StringUtils.isEmpty(toSend)) {
            return;
        }
        Privmsg msg = new Privmsg(null, target, server, toSend);
        gtm.getClient().sendMsg(msg);
        bar.setText("");
    }
}
