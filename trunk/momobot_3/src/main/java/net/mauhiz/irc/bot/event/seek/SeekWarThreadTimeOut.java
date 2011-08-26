package net.mauhiz.irc.bot.event.seek;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * @author mauhiz
 */
class SeekWarThreadTimeOut extends Timer {

    /**
     * @param seekwar2
     * @param timeOutMinutes
     */
    public SeekWarThreadTimeOut(SeekWar2 seekwar2, int timeOutMinutes) {
        super("SeekWar TimeOut");
        long timeOut = TimeUnit.MILLISECONDS.convert(timeOutMinutes, TimeUnit.MINUTES);
        schedule(new Timeout(seekwar2), timeOut);
    }
}
