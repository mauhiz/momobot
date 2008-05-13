package momobot.spam;

import ircbot.Channel;
import momobot.MomoBot;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class SpamRunnable implements Runnable {
    /**
     *
     */
    private static final Logger LOG = Logger.getLogger(SpamRunnable.class);
    /**
     *
     */
    private long delayMs;
    /**
     *
     */
    private String msg;
    /**
     *
     */
    private boolean running = true;
    /**
     *
     */
    private Channel targetChan;

    /**
     *
     */
    public SpamRunnable() {
        super();
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
        return msg;
    }

    /**
     * @return the targetChan
     */
    public Channel getTargetChan() {
        return targetChan;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (running) {
            try {
                Thread.sleep(getDelayMs());
            }
            catch (InterruptedException e) {
                LOG.warn(e, e);
            }
            MomoBot.getBotInstance().sendMessage(getTargetChan(), getMsg());
        }
    }

    /**
     * @param delayMs1 the delayMs to set
     */
    public void setDelayMs(final long delayMs1) {
        delayMs = delayMs1;
    }

    /**
     * @param msg1 the msg to set
     */
    public void setMsg(final String msg1) {
        msg = msg1;
    }

    /**
     * @param targetChan1 the targetChan to set
     */
    public void setTargetChan(final Channel targetChan1) {
        targetChan = targetChan1;
    }
}
