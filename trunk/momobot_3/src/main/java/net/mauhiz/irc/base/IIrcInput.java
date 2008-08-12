package net.mauhiz.irc.base;

import java.io.IOException;
import java.net.Socket;

/**
 * @author mauhiz
 */
public interface IIrcInput extends Runnable {
    
    /**
     * @param sock
     * @throws IOException
     */
    void connect(Socket sock) throws IOException;
}
