package net.mauhiz.irc.base;

/**
 * @author mauhiz
 */
public interface IIrcOutput {
    boolean isReady();
    
    void sendRawMsg(String raw);
}
