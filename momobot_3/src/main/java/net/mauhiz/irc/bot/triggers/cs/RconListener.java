package net.mauhiz.irc.bot.triggers.cs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import net.mauhiz.util.AbstractRunnable;

import org.apache.commons.lang.math.RandomUtils;

/**
 * @author mauhiz
 */
class RconListener extends AbstractRunnable {
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
    private final RconClient rc;
    
    /**
     * @param vuc1
     *            mon maitre!
     */
    protected RconListener(RconClient vuc1) throws IOException {
        super();
        rc = vuc1;
        int localPort = generateRandomPort();
        rc.getRconChallenge();
        rc.rconCmd("logaddress " + InetAddress.getLocalHost().getHostAddress() + ' ' + localPort);
    }
    
    /**
     * @param receivePacket
     */
    private void processLine(DatagramPacket receivePacket) {
        rc.processLine(new String(receivePacket.getData(), 0, receivePacket.getLength()));
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        DatagramPacket receivePacket = ValveUdpClient.createDatagramPacket();
        while (isRunning()) {
            try {
                rc.getSocket().receive(receivePacket);
                processLine(receivePacket);
            } catch (IOException ioe) {
                LOG.error(ioe, ioe);
                break;
            }
        }
    }
}
