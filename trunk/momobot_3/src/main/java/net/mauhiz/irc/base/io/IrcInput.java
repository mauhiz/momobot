package net.mauhiz.irc.base.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.FileUtil;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcInput extends AbstractDaemon implements IIrcInput {
    private static final Logger LOGGER = Logger.getLogger(IrcInput.class);

    private final IIrcIO io;
    private final BufferedReader reader;

    /**
     * @param io1
     * @param socket
     * @throws IOException
     */
    protected IrcInput(IIrcIO io1, Socket socket) throws IOException {
        super("IRC Input");
        io = io1;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), FileUtil.ISO8859_15));
    }

    @Override
    public void trun() {
        while (isRunning()) {
            try {
                String nextRaw = reader.readLine();
                if (nextRaw == null) {
                    LOGGER.warn("disconnected");
                    break;
                }
                LOGGER.debug("<< " + nextRaw);
                io.processMsg(nextRaw);
            } catch (IOException e) {
                LOGGER.warn("disconnected: " + e, e);
                break;
            }
        }
        io.disconnect();
    }

    @Override
    public void tstop() {
        super.tstop();
        try {
            reader.close();
        } catch (IOException ioe) {
            LOGGER.warn("Could not close input stream: " + ioe, ioe);
        }
    }
}
