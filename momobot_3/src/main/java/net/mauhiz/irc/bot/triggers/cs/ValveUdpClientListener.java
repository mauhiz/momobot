package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import net.mauhiz.irc.AbstractRunnable;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
class ValveUdpClientListener extends AbstractRunnable {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(ValveUdpClientListener.class);
    /**
     * port mini
     */
    private static final int MIN_PORT = 1024;
    /**
     * @return un port au hasard, > {@link #MIN_PORT}.
     */
    static int generateRandomPort() {
        return MIN_PORT + RandomUtils.nextInt(5000);
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
        vuc = vuc1;
        final int localPort = generateRandomPort();
        try {
            vuc.getChallenge();
            vuc.rconCmd("logaddress " + InetAddress.getLocalHost().getHostAddress() + ' ' + localPort);
        } catch (final IOException ioe) {
            LOG.error(ioe, ioe);
        }
    }
    
    /**
     * @param receivePacket
     */
    private void processLine(final DatagramPacket receivePacket) {
        vuc.processLine(new String(receivePacket.getData(), 0, receivePacket.getLength()));
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        final DatagramPacket receivePacket = vuc.createDatagramPacket();
        setRunning(true);
        while (isRunning()) {
            try {
                vuc.getSocket().receive(receivePacket);
                processLine(receivePacket);
            } catch (final IOException ioe) {
                LOG.error(ioe, ioe);
                break;
            }
        }
        setRunning(false);
    }
}
