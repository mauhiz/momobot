package net.mauhiz.irc.gui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.ITriggerManager;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.util.AbstractRunnable;

import org.eclipse.swt.widgets.Display;

/**
 * @author mauhiz
 */
public class GuiTriggerManager implements ITriggerManager {
    IrcControl client = new IrcControl(this);
    BlockingQueue<IIrcMessage> incoming = new LinkedBlockingQueue<IIrcMessage>();
    
    /**
     * @return the client
     */
    public IrcControl getClient() {
        return client;
    }
    
    /**
     * @return next msg
     */
    public IIrcMessage nextMsg() {
        return incoming.poll();
    }
    
    /**
     * @see net.mauhiz.irc.base.ITriggerManager#processMsg(IIrcMessage, IIrcControl)
     */
    @Override
    public void processMsg(IIrcMessage msg, IIrcControl ircControl) {
        if (msg == null) {
            return;
        }
        try {
            incoming.put(msg);
        } catch (InterruptedException e) {
            AbstractRunnable.handleInterruption(e);
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.ITriggerManager#shutdown()
     */
    @Override
    public void shutdown() {
        Display.getDefault().dispose();
    }
}
