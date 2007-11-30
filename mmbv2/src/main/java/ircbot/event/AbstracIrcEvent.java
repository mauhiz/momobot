package ircbot.event;

/**
 * @author mauhiz
 */
public abstract class AbstracIrcEvent implements IIrcEvent {
    /**
     * 
     */
    private String raw;

    /**
     * 
     */
    public AbstracIrcEvent() {
        super();
    }

    /**
     * @param raw1
     */
    public AbstracIrcEvent(final String raw1) {
        this.raw = raw1;
    }

    /**
     * @see ircbot.event.IIrcEvent#getRaw()
     */
    public String getRaw() {
        return this.raw;
    }
}
