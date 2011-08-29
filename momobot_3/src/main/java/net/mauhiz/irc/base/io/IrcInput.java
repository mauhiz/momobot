package net.mauhiz.irc.base.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.FileUtil;
import net.mauhiz.util.ThreadUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcInput extends AbstractDaemon implements IIrcInput {
    private static final Logger LOG = Logger.getLogger(IrcInput.class);
    private final IIrcIO io;
    private final AsynchronousSocketChannel sclient; //

    /**
     * @param io
     * @param sclient
     */
    protected IrcInput(IIrcIO io, AsynchronousSocketChannel sclient) {
        super("IRC Input");
        this.io = io;
        this.sclient = sclient;
    }

    /**
     * @return unprocessed chunk or NULL
     */
    private String handleChunk(String previousChunk, ByteBuffer bb) {
        bb.rewind();
        CharBuffer cb = FileUtil.ISO8859_15.decode(bb);
        String[] lines = StringUtils.splitPreserveAllTokens(previousChunk + cb.toString(), "\r\n");

        for (int i = 0; i < lines.length - 1; i++) {
            LOG.debug("<< " + lines[i]);
            io.processMsg(lines[i]);
        }

        return lines[lines.length];
    }

    @Override
    public void trun() {
        for (String unprocessedChunk = ""; isRunning();) {
            ByteBuffer bb = ByteBuffer.allocate(FileUtil.BUF_SIZE);
            try {
                Integer read = sclient.read(bb).get();
                if (read.intValue() == -1) {
                    LOG.warn("disconnected");
                    break;
                }
            } catch (InterruptedException e) {
                ThreadUtils.handleInterruption(e);
            } catch (ExecutionException e) {
                LOG.warn("Disconnected?", e.getCause());
                break;
            }
            unprocessedChunk = handleChunk(unprocessedChunk, bb);
        }
        io.disconnect();
    }

    @Override
    public void tstop() {
        super.tstop();
        try {
            sclient.close();
        } catch (IOException ioe) {
            LOG.warn("Could not close input stream: " + ioe, ioe);
        }
    }
}
