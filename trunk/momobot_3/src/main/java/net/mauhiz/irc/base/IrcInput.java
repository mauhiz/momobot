package net.mauhiz.irc.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcInput implements IIrcInput {
    private static final Logger LOG = Logger.getLogger(IrcInput.class);
    IIrcIO io;
    BufferedReader reader;
    
    /**
     * @param io1
     */
    IrcInput(final IIrcIO io1) {
        io = io1;
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcInput#connect(java.net.Socket)
     */
    public void connect(final Socket connection) throws IOException {
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        if (reader == null) {
            throw new IllegalStateException("use connect() first");
        }
        while (true) {
            try {
                String next = reader.readLine();
                LOG.info("<< " + next);
                if (next == null) {
                    LOG.warn("disconnected");
                    break;
                }
                io.processMsg(next);
            } catch (IOException e) {
                break;
            }
        }
        try {
            io.disconnect();
        } catch (IOException e) {
            LOG.error(e, e);
        }
    }
}
