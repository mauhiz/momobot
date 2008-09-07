package net.mauhiz.irc.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.mauhiz.irc.AbstractRunnable;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcOutput extends AbstractRunnable implements IIrcOutput {
    /**
     * antiflood en ms
     */
    private static final long DELAY = 100;
    private static final Logger LOG = Logger.getLogger(IrcOutput.class);
    private static final int MAX_SIZE = 50;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>(MAX_SIZE);
    private final PrintWriter writer;
    
    /**
     * @param socket
     * @throws IOException
     */
    IrcOutput(final Socket socket) throws IOException {
        writer = new PrintWriter(socket.getOutputStream());
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcOutput#isReady()
     */
    public boolean isReady() {
        return queue.size() > MAX_SIZE;
    }
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        setRunning(true);
        while (isRunning()) {
            String toWrite = queue.poll();
            pause(DELAY);
            if (toWrite == null) {
                continue;
            }
            LOG.info(">> " + toWrite);
            writer.println(toWrite);
            writer.flush();
        }
        writer.close();
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcOutput#sendRawMsg(java.lang.String)
     */
    public void sendRawMsg(final String raw) {
        if (raw == null) {
            return;
        }
        try {
            queue.put(raw);
        } catch (InterruptedException e) {
            setRunning(false);
            Thread.currentThread().interrupt();
        }
    }
}
