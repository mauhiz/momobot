package net.mauhiz.irc.bot.event.seek;

import java.util.TimerTask;

/**
 * @author mauhiz
 * 
 */
class Timeout extends TimerTask {

    /**
     * reference
     */
    private final SeekWar2 seekwar2;

    /**
     * @param seekwar2
     */
    Timeout(SeekWar2 seekwar2) {
        super();
        this.seekwar2 = seekwar2;
    }

    /**
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        seekwar2.setStop("TimeOut");
    }
}