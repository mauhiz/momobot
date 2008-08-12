package net.mauhiz.irc.bot.triggers.spam;

import net.mauhiz.irc.AbstractRunnable;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;

/**
 * @author mauhiz
 */
public class SpamRunnable extends AbstractRunnable {
    /**
     * control.
     */
    private IIrcControl control;
    /**
     * 
     */
    private long delayMs;
    
    /**
     * 
     */
    private Privmsg spamMsg;
    
    /**
     * @param spamMsg1
     * @param control1
     * @param delayMs1
     */
    public SpamRunnable(final Privmsg spamMsg1, final IIrcControl control1, final long delayMs1) {
        super();
        control = control1;
        delayMs = delayMs1;
        spamMsg = spamMsg1;
    }
    
    /**
     * @return the delayMs
     */
    public long getDelayMs() {
        return delayMs;
    }
    
    /**
     * @return the msg
     */
    public String getMsg() {
        return spamMsg.getMessage();
    }
    
    /**
     * @return the targetChan
     */
    public String getTargetChan() {
        return spamMsg.getTo();
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (isRunning()) {
            sleep(delayMs);
            control.sendMsg(spamMsg);
        }
    }
}
