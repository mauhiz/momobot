package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.gui.GuiTriggerManager;
import net.mauhiz.util.AbstractAction;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Text;

/**
 * @author mauhiz
 */
public class SendNoticeAction extends AbstractAction {
    private final Text bar;
    private final GuiTriggerManager gtm;
    private final IrcServer server;
    private final Target target;

    public SendNoticeAction(Text bar, GuiTriggerManager gtm, IrcServer server, Target target) {
        super();
        this.gtm = gtm;
        this.bar = bar;
        this.server = server;
        this.target = target;
    }

    @Override
    protected void doAction() {
        String toSend = bar.getText();
        if (StringUtils.isEmpty(toSend)) {
            return;
        }
        Notice msg = new Notice(null, target, server, toSend);
        gtm.getClient().sendMsg(msg);
        bar.setText("");
    }

    @Override
    protected boolean isAsynchronous() {
        return false; // this should be quick 
    }
}
