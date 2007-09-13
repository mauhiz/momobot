package csbot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import utils.AbstractRunnable;

/**
 * @author mauhiz
 */
class ValveUdpClientListener extends AbstractRunnable {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(ValveUdpClientListener.class);

    /**
     * @return un port au hasard, > 1024.
     */
    static int generateRandomPort() {
        return RandomUtils.nextInt(5000) + 0x400;
    }
    /**
     * my mastah.
     */
    private final ValveUdpClient vuc;

    /**
     * @param vuc1
     *            mon maitre!
     */
    protected ValveUdpClientListener(final ValveUdpClient vuc1) {
        super();
        this.vuc = vuc1;
        final int localPort = generateRandomPort();
        try {
            this.vuc.getChallenge();
            this.vuc.rconCmd("logaddress " + InetAddress.getLocalHost().getHostAddress() + ' ' + localPort);
        } catch (final IOException ioe) {
            LOG.error(ioe, ioe);
        }
    }

    /**
     * @param receivePacket
     */
    private void processLine(final DatagramPacket receivePacket) {
        this.vuc.processLine(new String(receivePacket.getData(), 0, receivePacket.getLength()));
    }

    /**
     * @see utils.AbstractRunnable#run()
     */
    @Override
    public void run() {
        final DatagramPacket receivePacket = this.vuc.createDatagramPacket();
        setRunning(true);
        while (isRunning()) {
            try {
                this.vuc.getSocket().receive(receivePacket);
                processLine(receivePacket);
            } catch (final IOException ioe) {
                LOG.error(ioe, ioe);
                break;
            }
        }
        setRunning(false);
    }
}
