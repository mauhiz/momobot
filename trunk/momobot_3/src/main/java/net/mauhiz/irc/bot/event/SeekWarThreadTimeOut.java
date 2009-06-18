package net.mauhiz.irc.bot.event;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author mauhiz
 */
public class SeekWarThreadTimeOut extends Timer {
    
    /**
     * @author mauhiz
     * 
     */
    static class Timeout extends TimerTask {
        
        /**
         * reference
         */
        private final SeekWar2 seekwar;
        
        /**
         * @param seekwar2
         */
        Timeout(SeekWar2 seekwar2) {
            seekwar = seekwar2;
        }
        
        /**
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run() {
            seekwar.setStop("TimeOut");
        }
    }
    
    /**
     * @param seekwar1
     * @param timeOutMinutes
     */
    SeekWarThreadTimeOut(SeekWar2 seekwar1, int timeOutMinutes) {
        super("SeekWar TimeOut");
        long timeOut = TimeUnit.MILLISECONDS.convert(timeOutMinutes, TimeUnit.MINUTES);
        schedule(new Timeout(seekwar1), timeOut);
    }
}
