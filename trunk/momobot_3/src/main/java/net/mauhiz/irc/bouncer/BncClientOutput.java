package net.mauhiz.irc.bouncer;

import java.io.IOException;
import java.net.Socket;

import net.mauhiz.irc.base.io.IrcOutput;

/**
 * @author mauhiz
 */
public class BncClientOutput extends IrcOutput {
    
    /**
     * @throws IOException
     */
    public BncClientOutput(Socket socket) throws IOException {
        super(socket);
    }
    
    @Override
    public void start() {
        startAs("Bnc Client Output Thread");
    }
}
