package net.mauhiz.irc.base.ident;

import java.io.IOException;

/**
 * @author mauhiz
 */
public interface IIdentServer {
    void start();
    
    /**
     * @throws IOException
     */
    void stop() throws IOException;
}
