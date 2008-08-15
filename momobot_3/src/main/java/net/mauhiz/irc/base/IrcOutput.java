package net.mauhiz.irc.base;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcOutput implements IIrcOutput {
    /**
     * antiflood en ms
     */
    private static final long DELAY = 100;
    private static final Logger LOG = Logger.getLogger(IrcOutput.class);
    private static final int MAX_SIZE = 50;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>(MAX_SIZE);
    boolean running;
    
    private PrintWriter writer;
    
    /**
     * @param socket
     * @throws IOException
     */
    IrcOutput(final Socket socket) throws IOException {
        connect(socket.getOutputStream());
    }
    
    /**
     * @param outStream
     */
    void connect(final OutputStream outStream) {
        writer = new PrintWriter(outStream);
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
        running = true;
        while (running) {
            try {
                String toWrite = queue.poll();
                Thread.sleep(DELAY);
                if (toWrite == null) {
                    continue;
                }
                LOG.debug(">> " + toWrite);
                writer.println(toWrite);
                writer.flush();
            } catch (InterruptedException e) {
                LOG.error(e);
                running = false;
                Thread.currentThread().interrupt();
                break;
            }
        }
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
            running = false;
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcOutput#setRunning(boolean)
     */
    @Override
    public void setRunning(final boolean run) {
        running = run;
    }
}
