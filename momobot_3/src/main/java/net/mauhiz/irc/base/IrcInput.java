package net.mauhiz.irc.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import net.mauhiz.util.AbstractRunnable;
import net.mauhiz.util.FileUtil;

/**
 * @author mauhiz
 */
public class IrcInput extends AbstractRunnable implements IIrcInput {
    private final IIrcIO io;
    private final BufferedReader reader;
    
    /**
     * @param io1
     * @param socket
     * @throws IOException
     */
    IrcInput(IIrcIO io1, Socket socket) throws IOException {
        super();
        io = io1;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), FileUtil.ISO8859_15));
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
                if (next == null) {
                    LOG.warn("disconnected");
                    break;
                }
                LOG.info("<< " + next);
                io.processMsg(next);
            } catch (IOException e) {
                LOG.warn("disconnected", e);
                break;
            }
        }
        io.disconnect();
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcInput#start()
     */
    @Override
    public void start() {
        startAs("Input Thread");
    }
}
