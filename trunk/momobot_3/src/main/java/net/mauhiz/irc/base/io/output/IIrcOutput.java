package net.mauhiz.irc.base.io.output;

import net.mauhiz.util.IDaemon;

/**
 * @author mauhiz
 */
public interface IIrcOutput extends IDaemon {
    boolean isReady();

    /**
     * @param raw
     */
    void sendRawMsg(String raw);
}
