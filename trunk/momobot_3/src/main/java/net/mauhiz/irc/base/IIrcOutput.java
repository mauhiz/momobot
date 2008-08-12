package net.mauhiz.irc.base;

/**
 * @author mauhiz
 */
public interface IIrcOutput extends Runnable {
    boolean isReady();

    void sendRawMsg(String raw);
}
