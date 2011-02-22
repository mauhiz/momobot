package net.mauhiz.irc.gui.actions;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.gui.GuiTriggerManager;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * @author mauhiz
 */
public class ConnectAction implements SelectionListener {
    GuiTriggerManager gtm;
    IrcServer server;
    
    /**
     * @param gtm1
     * @param server1
     */
    public ConnectAction(GuiTriggerManager gtm1, IrcServer server1) {
        server = server1;
        gtm = gtm1;
    }
    
    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent arg0) {
        /* nothing */
    }
    
    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent arg0) {
        gtm.getClient().connect(server);
    }
}
