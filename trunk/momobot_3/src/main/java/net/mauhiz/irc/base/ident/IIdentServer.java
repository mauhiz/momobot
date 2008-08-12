package net.mauhiz.irc.base.ident;

import java.io.IOException;

/**
 * @author mauhiz
 */
public interface IIdentServer {
    void start(String user) throws IOException;

    void stop() throws IOException;
}
