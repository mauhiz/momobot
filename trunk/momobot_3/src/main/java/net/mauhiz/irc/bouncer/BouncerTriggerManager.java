package net.mauhiz.irc.bouncer;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.irc.base.ITriggerManager;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public class BouncerTriggerManager implements ITriggerManager, Runnable {
    List<BncClient> currentlyConnected = new ArrayList<BncClient>();
    
    /**
     * @see net.mauhiz.irc.base.ITriggerManager#processMsg(net.mauhiz.irc.base.msg.IIrcMessage,
     *      net.mauhiz.irc.base.IrcControl)
     */
    @Override
    public void processMsg(final IIrcMessage msg, final IrcControl ircControl) {
        for (BncClient client : currentlyConnected) {
            client.sendData(msg.toString());
        }
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        while (true) {
            /* TODO receive client msgs */
            break;
        }
    }
}
