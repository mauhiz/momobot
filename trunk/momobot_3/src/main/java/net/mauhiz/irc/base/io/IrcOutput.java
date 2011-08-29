package net.mauhiz.irc.base.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.FileUtil;
import net.mauhiz.util.ThreadUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcOutput extends AbstractDaemon implements IIrcOutput {
    /**
     * antiflood en ms
     */
    private static final long DELAY = 100;
    private static final Logger LOGGER = Logger.getLogger(IrcOutput.class);
    private static final int MAX_SIZE = 50;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>(MAX_SIZE);
    private final AsynchronousSocketChannel socket;

    /**
     * @param socket
     */
    protected IrcOutput(AsynchronousSocketChannel socket) {
        super("IRC Output");
        this.socket = socket;
    }

    /**
     * @see net.mauhiz.irc.base.io.IIrcOutput#isReady()
     */
    public boolean isReady() {
        return isRunning() && queue.size() <= MAX_SIZE;
    }

    /**
     * @see net.mauhiz.irc.base.io.IIrcOutput#sendRawMsg(java.lang.String)
     */
    public void sendRawMsg(String raw) {
        if (raw == null) {
            return;
        } else if (StringUtils.isBlank(raw)) {
            LOGGER.warn("Tried to send empty msg", new IllegalArgumentException());
        }
        try {
            queue.put(raw);
        } catch (InterruptedException ie) {
            handleInterruption(ie);
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void trun() {
        while (isRunning()) {
            String toWrite = queue.poll();
            pause(DELAY);
            if (toWrite == null) {
                continue;
            }
            LOGGER.debug(">> " + toWrite);
            ByteBuffer bytesToWrite = FileUtil.ISO8859_15.encode(CharBuffer.wrap(toWrite + "\r\n"));
            Future<Integer> fWritten = socket.write(bytesToWrite);
            try {
                int written = fWritten.get().intValue();
                if (written == -1) {
                    LOGGER.warn("Disconnected");
                    break;
                } else if (written != bytesToWrite.limit()) {
                    LOGGER.error("Could not write all (" + written + "/" + bytesToWrite.limit() + ")");
                }
            } catch (InterruptedException e) {
                ThreadUtils.handleInterruption(e);
            } catch (ExecutionException e) {
                LOGGER.error("Could not write to socket", e.getCause());
                break;
            }
        }
        tstop();
    }

    @Override
    public void tstop() {
        super.tstop();
        try {
            socket.close();
        } catch (IOException ioe) {
            LOGGER.warn("Could not close output stream: " + ioe, ioe);
        }
    }
}
