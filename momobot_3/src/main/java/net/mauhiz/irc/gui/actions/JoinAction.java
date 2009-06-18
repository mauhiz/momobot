package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.gui.GuiTriggerManager;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * @author mauhiz
 */
public class JoinAction implements SelectionListener {
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
    
    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent arg0) {
        // ?
    }
    
    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent arg0) {
        Join msg = new Join(server, channel);
        gtm.getClient().sendMsg(msg);
    }
}
