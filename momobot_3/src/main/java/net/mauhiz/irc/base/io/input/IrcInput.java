package net.mauhiz.irc.base.io.input;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.util.AbstractThread;
import net.mauhiz.util.ExecutionType;
import net.mauhiz.util.FileUtil;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcInput extends AbstractThread implements IIrcInput, Closeable {

    private static final Logger LOG = Logger.getLogger(IrcInput.class);
    private final IIrcIO io;
    private final AsynchronousSocketChannel sclient;

    /**
     * @param io
     * @param sclient
     */
    public IrcInput(IIrcIO io, AsynchronousSocketChannel sclient) {
        super("IRC Input");
        this.io = io;
        this.sclient = sclient;
    }

    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.PARALLEL_CACHED;
    }

    @Override
    public void trun() {
        BytesRead handler = new BytesRead(this, io);
        while (isRunning()) {
            // allocate a new buffer each time
            ByteBuffer bb = ByteBuffer.allocate(FileUtil.BUF_SIZE);
            sclient.read(bb, bb, handler);
        }
    }

    @Override
    public void tstop() {
        super.tstop();
        try {
            close();
        } catch (IOException ioe) {
            LOG.warn(ioe, ioe);
        }
    }

    @Override
    public void close() throws IOException {
        try (AsynchronousSocketChannel toClose = sclient) {
            io.disconnect();
        }
    }
}
