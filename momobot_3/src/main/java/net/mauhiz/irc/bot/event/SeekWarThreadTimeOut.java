package net.mauhiz.irc.bot.event;

import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.AbstractRunnable;

public class SeekWarThreadTimeOut extends AbstractRunnable {
    
    private final SeekWar2 seekwar;
    // private final StopWatch sw = new StopWatch();
    private long timeOut;
    
    /**
     * @param seekwar1
     * @param timeOut1
     */
    SeekWarThreadTimeOut(final SeekWar2 seekwar1, final int timeOut1) {
        timeOut = TimeUnit.MILLISECONDS.convert(timeOut1, TimeUnit.MINUTES);
        seekwar = seekwar1;
        // sw.start();
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        
        setRunning(SeekWarSelecter.isRunnable);
        // while (sw.getTime() < timeOut && isRunning) {
        // }
        // On demande de leave
        sleep(timeOut);
        
        setRunning(true);
        seekwar.setStop("TimeOut");
        // sw.stop();
    }
}
