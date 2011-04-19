package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.msg.Action;
import net.mauhiz.irc.gui.GuiTriggerManager;
import net.mauhiz.util.AbstractAction;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Text;

/**
 * @author mauhiz
 */
public class SendMeAction extends AbstractAction {
    private final Text bar;
    private final GuiTriggerManager gtm;
    private final IIrcServerPeer server;
    private final Target target;

    public SendMeAction(Text bar1, GuiTriggerManager gtm1, IIrcServerPeer server1, Target target) {
        super();
        gtm = gtm1;
        bar = bar1;
        server = server1;
        this.target = target;
    }

    @Override
    protected void doAction() {
        String toSend = bar.getText();
        if (StringUtils.isEmpty(toSend)) {
            return;
        }
        Action msg = new Action(server, null, target, toSend);
        gtm.getClient().sendMsg(msg);
        bar.setText("");
    }

    @Override
    protected boolean isAsynchronous() {
        return false; // this should be quick 
    }
}
