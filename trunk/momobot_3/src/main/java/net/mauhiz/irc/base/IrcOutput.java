package net.mauhiz.irc.base;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.mauhiz.util.AbstractRunnable;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcOutput extends AbstractRunnable implements IIrcOutput {
    /**
     * antiflood en ms
     */
    private static final long DELAY = 100;
    private static final Logger LOGGER = Logger.getLogger(IrcOutput.class);
    private static final int MAX_SIZE = 50;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>(MAX_SIZE);
    private final PrintWriter writer;
    
    /**
     * @param socket
     * @throws IOException
     */
    IrcOutput(Socket socket) throws IOException {
        super();
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), FileUtil.ISO8859_15), true);
    }
    /**
     * @see net.mauhiz.irc.base.IIrcOutput#isReady()
     */
    public boolean isReady() {
        return isRunning() && queue.size() <= MAX_SIZE;
    }
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (isRunning()) {
            String toWrite = queue.poll();
            pause(DELAY);
            if (toWrite == null) {
                continue;
            }
            LOGGER.debug(">> " + toWrite);
            writer.println(toWrite);
        }
        writer.close();
    }
    /**
     * @see net.mauhiz.irc.base.IIrcOutput#sendRawMsg(java.lang.String)
     */
    public void sendRawMsg(String raw) {
        if (raw == null) {
            return;
        } else if (StringUtils.isBlank(raw)) {
            LOGGER.warn("Tried to send empty msg", new IllegalArgumentException());
        }
        
        try {
            queue.put(raw);
        } catch (InterruptedException e) {
            stop();
            AbstractRunnable.handleInterruption(e);
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcOutput#start()
     */
    @Override
    public void start() {
        startAs("Output Thread");
    }
}
