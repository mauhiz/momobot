package net.mauhiz.irc.gui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.mauhiz.irc.base.IIrcClientControl;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.irc.base.MsgState;
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

    @Override
    public MsgState processMsg(IIrcMessage msg, IIrcControl control) {
        MsgState superState = super.processMsg(msg, control);
        if (superState == MsgState.AVAILABLE) {
            try {
                incoming.put(msg);
                return MsgState.AVAILABLE;
            } catch (InterruptedException e) {
                ThreadUtils.handleInterruption(e);
                return MsgState.INVALID;
            }
        }
        return superState;
    }
}
