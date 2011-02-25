package net.mauhiz.irc.gui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.trigger.DefaultTriggerManager;
import net.mauhiz.util.AbstractRunnable;

import org.eclipse.swt.widgets.Display;

/**
 * @author mauhiz
 */
public class GuiTriggerManager extends DefaultTriggerManager {
    IIrcControl client = new IrcControl(this);
    BlockingQueue<IIrcMessage> incoming = new LinkedBlockingQueue<IIrcMessage>();

    /**
     * @return the client
     */
    public IIrcControl getClient() {
        return client;
    }

    /**
     * @return next msg
     */
    public IIrcMessage nextMsg() {
        return incoming.poll();
    }

    /**
     * @see net.mauhiz.irc.base.trigger.ITriggerManager#processMsg(IIrcMessage, IIrcControl)
     */
    @Override
    public boolean processMsg(IIrcMessage msg, IIrcControl control) {
        if (super.processMsg(msg, control)) {
            return true;
        }
        try {
            incoming.put(msg);
            return false;
        } catch (InterruptedException e) {

            AbstractRunnable.handleInterruption(e);
            return true;
        }
    }

    /**
     * @see net.mauhiz.irc.base.trigger.ITriggerManager#shutdown()
     */
    @Override
    public void shutdown() {
        Display.getDefault().dispose();
        super.shutdown();
    }
}
