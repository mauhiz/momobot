package net.mauhiz.irc.base;

/**
 * @author mauhiz
 */
public interface IIrcOutput {
    boolean isReady();
    
    /**
     * @param raw
     */
    void sendRawMsg(String raw);
    
    /**
     * go!
     */
    void start();
    
    void stop();
}
