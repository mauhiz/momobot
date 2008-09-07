package net.mauhiz.irc.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import net.mauhiz.irc.AbstractRunnable;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcInput extends AbstractRunnable implements IIrcInput {
    private static final Logger LOG = Logger.getLogger(IrcInput.class);
    private final IIrcIO io;
    private final BufferedReader reader;
    
    /**
     * @param io1
     * @param socket
     * @throws IOException
     */
    IrcInput(final IIrcIO io1, final Socket socket) throws IOException {
        io = io1;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        if (reader == null) {
            throw new IllegalStateException("use connect() first");
        }
        while (isRunning()) {
            try {
                String next = reader.readLine();
                LOG.info("<< " + next);
                if (next == null) {
                    LOG.warn("disconnected");
                    break;
                }
                io.processMsg(next);
            } catch (IOException e) {
                setRunning(false);
            }
        }
        io.disconnect();
        setRunning(false);
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcInput#start()
     */
    @Override
    public void start() {
        startAs("Input Thread");
    }
}
