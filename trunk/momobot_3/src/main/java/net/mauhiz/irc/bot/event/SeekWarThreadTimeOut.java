package net.mauhiz.irc.bot.event;

import java.util.concurrent.TimeUnit;

public class SeekWarThreadTimeOut extends Thread {
    
    private final SeekWar2 seekwar;
    // private final StopWatch sw = new StopWatch();
    private long timeOut;
    
    SeekWarThreadTimeOut(final SeekWar2 seekwar1, final int timeOut1) {
        timeOut = TimeUnit.MILLISECONDS.convert(timeOut1, TimeUnit.MINUTES);
        seekwar = seekwar1;
        // sw.start();
    }
    @Override
    public void run() {
        
        boolean isRunning = SeekWarSelecter.isRunnable;
        // while (sw.getTime() < timeOut && isRunning) {
        // }
        // On demande de leave
        try {
            Thread.sleep(timeOut);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        isRunning = true;
        seekwar.setStop("TimeOut");
        // sw.stop();
    }
    
}
