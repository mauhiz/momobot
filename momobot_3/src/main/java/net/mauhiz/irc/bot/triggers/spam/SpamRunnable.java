package net.mauhiz.irc.bot.triggers.spam;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.util.AbstractRunnable;

/**
 * @author mauhiz
 */
public class SpamRunnable extends AbstractRunnable {
    /**
     * control.
     */
    private final IIrcControl control;
    /**
     * 
     */
    private final long delayMs;
    
    /**
     * 
     */
    private final Privmsg spamMsg;
    
    /**
     * @param spamMsg1
     * @param control1
     * @param delayMs1
     */
    public SpamRunnable(Privmsg spamMsg1, IIrcControl control1, long delayMs1) {
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
