package net.mauhiz.irc.bot.event;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;

public class SeekWarThreadTimeOut extends Thread {
    
    static boolean isRunning;
    private final StopWatch sw = new StopWatch();
    private long timeOut;
    
    SeekWarThreadTimeOut(final int timeOut1) {
        timeOut = TimeUnit.MILLISECONDS.convert(timeOut1, TimeUnit.MINUTES);
        sw.start();
    }
    @Override
    public void run() {
        isRunning = true;
        while (sw.getTime() < timeOut && isRunning) {
        }
        sw.stop();
    }
}
