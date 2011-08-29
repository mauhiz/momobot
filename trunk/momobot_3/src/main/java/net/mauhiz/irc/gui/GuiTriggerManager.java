package net.mauhiz.irc.gui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.mauhiz.irc.base.IIrcClientControl;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.trigger.DefaultTriggerManager;
import net.mauhiz.util.ThreadUtils;

/**
 * @author mauhiz
 */
public class GuiTriggerManager extends DefaultTriggerManager {
    IIrcClientControl client = new IrcClientControl(this);
    private final BlockingQueue<IIrcMessage> incoming = new LinkedBlockingQueue<>();

    /**
     * @return the client
     */
    public IIrcClientControl getClient() {
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
            ThreadUtils.handleInterruption(e);
            return true;
        }
    }
}
