package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.gui.GuiTriggerManager;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Text;

/**
 * @author mauhiz
 */
public class SendNoticeAction extends AbstractAction {
    private final Text bar;
    private final GuiTriggerManager gtm;
    private final IIrcServerPeer server;
    private final Target target;

    public SendNoticeAction(Text bar, GuiTriggerManager gtm, IIrcServerPeer server, Target target) {
        super();
        this.gtm = gtm;
        this.bar = bar;
        this.server = server;
        this.target = target;
    }

    @Override
    protected ExecutionType getExecutionType() {
        return ExecutionType.GUI_ASYNCHRONOUS; // this should be quick 
    }

    @Override
    public void trun() {
        String toSend = bar.getText();
        if (StringUtils.isEmpty(toSend)) {
            return;
        }
        Notice msg = new Notice(server, null, target, toSend);
        gtm.getClient().sendMsg(msg);
        bar.setText("");
    }
}
