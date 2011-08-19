package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Random;

import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.FileUtil;
import net.mauhiz.util.NetUtils;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
class RconListener extends AbstractDaemon implements IRconListener {
    private static final Logger LOGGER = Logger.getLogger(RconListener.class);
    /**
     * port mini
     */
    private static final int MIN_PORT = 1024;
    private static final Random RANDOM = new Random();

    /**
     * @return un port au hasard, > {@link #MIN_PORT}.
     */
    static int generateRandomPort() {
        return MIN_PORT + RANDOM.nextInt(5000); // TODO ensure port availability
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
    private void processLine(DatagramPacket receivePacket) {
        rc.processLine(new String(receivePacket.getData(), 0, receivePacket.getLength(), FileUtil.ASCII));
    }

    private void runLoop(DatagramPacket receivePacket) {
        while (isRunning()) {
            try {
                rc.receive(receivePacket);
                processLine(receivePacket);
            } catch (IOException ioe) {
                LOGGER.error(ioe, ioe);
                break;
            }
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void trun() {
        DatagramPacket receivePacket = NetUtils.createDatagramPacket();
        runLoop(receivePacket);
    }
}
