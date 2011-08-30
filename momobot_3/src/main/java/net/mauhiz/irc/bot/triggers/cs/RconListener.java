package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Random;

import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.FileUtil;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
class RconListener extends AbstractDaemon implements IRconListener {
    private static final Logger LOG = Logger.getLogger(RconListener.class);
    /**
     * port mini
     */
    private static final int MIN_PORT = 0x400;
    private static final Random RANDOM = new Random();

    /**
     * @return un port au hasard, > {@link #MIN_PORT}.
     */
    static int generateRandomPort() {
        return MIN_PORT + RANDOM.nextInt(5_000); // TODO ensure port availability
    }

    /**
     * my mastah.
     */
    private final IRconClient rc;

    /**
     * @param vuc1
     *            mon maitre!
     */
    protected RconListener(IRconClient vuc1) throws IOException {
        super("Rcon Listener");
        rc = vuc1;
        int localPort = generateRandomPort();
        rc.getRconChallenge();
        rc.rconCmd("logaddress " + InetAddress.getLocalHost().getHostAddress() + ' ' + localPort);
    }

    /**
     * @param receivePacket
     */
    private void processLine(ByteBuffer receivePacket) {
        rc.processLine(FileUtil.ASCII.decode(receivePacket).toString());
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void trun() {
        while (isRunning()) {
            ByteBuffer receivePacket = ByteBuffer.allocate(FileUtil.BUF_SIZE);
            try {
                rc.receive(receivePacket);
                processLine(receivePacket);
            } catch (IOException ioe) {
                LOG.error(ioe, ioe);
                break;
            }
        }
    }
}
