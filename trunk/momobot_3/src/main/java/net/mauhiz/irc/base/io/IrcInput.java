package net.mauhiz.irc.base.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.FileUtil;
import net.mauhiz.util.ThreadUtils;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcInput extends AbstractDaemon implements IIrcInput {
    private static final Pattern linePattern = Pattern.compile(".*[\r\n]+");
    private static final Logger LOGGER = Logger.getLogger(IrcInput.class);
    private final IIrcIO io;
    private final AsynchronousSocketChannel sclient; //

    /**
     * @param io1
     * @param sclient
     */
    protected IrcInput(IIrcIO io1, AsynchronousSocketChannel sclient) {
        super("IRC Input");
        io = io1;
        this.sclient = sclient;
    }

    @Override
    public void trun() {
        while (isRunning()) {
            try {
                ByteBuffer bb = ByteBuffer.allocate(FileUtil.BUF_SIZE);
                Integer read = sclient.read(bb).get();
                if (read.intValue() == -1) {
                    LOGGER.warn("disconnected");
                    break;
                }
                CharBuffer cb = FileUtil.ISO8859_15.decode(bb);
                Matcher lm = linePattern.matcher(cb); // Line matcher
                while (lm.find()) {
                    String line = lm.group();
                    LOGGER.debug("<< " + line);
                    io.processMsg(line);
                }
            } catch (InterruptedException e) {
                ThreadUtils.handleInterruption(e);
            } catch (ExecutionException e) {
                LOGGER.warn("Disconnected?", e.getCause());
                break;
            }
        }
        io.disconnect();
    }

    @Override
    public void tstop() {
        super.tstop();
        try {
            sclient.close();
        } catch (IOException ioe) {
            LOGGER.warn("Could not close input stream: " + ioe, ioe);
        }
    }
}
