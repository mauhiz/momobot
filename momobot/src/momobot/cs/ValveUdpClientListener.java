package momobot.cs;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Random;
import org.apache.log4j.Logger;
import utils.MyRunnable;

/**
 * @author viper
 */
class ValveUdpClientListener extends MyRunnable {
    /**
     * logger.
     */
    private static final Logger LOG = Logger
                                            .getLogger(ValveUdpClientListener.class);
    /**
     * my mastah.
     */
    private ValveUdpClient      vuc = null;

    /**
     * @param vuc1
     *            mon maitre!
     */
    ValveUdpClientListener(final ValveUdpClient vuc1) {
        this.vuc = vuc1;
        final Random r = new Random();
        final int localPort = r.nextInt(5000) + 0x400;
        try {
            this.vuc.getChallenge();
            this.vuc.rconCmd("logaddress "
                    + InetAddress.getLocalHost().getHostAddress() + ' '
                    + localPort);
        } catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e, e);
            }
        }
    }

    /**
     * @see utils.MyRunnable#run()
     */
    @Override
    public void run() {
        final DatagramPacket receivePacket = new DatagramPacket(this.vuc
                .getReceiveBuf(), this.vuc.getReceiveBuf().length);
        while (isRunning()) {
            try {
                this.vuc.getSocket().receive(receivePacket);
                this.vuc.processLine(new String(receivePacket.getData(), 0,
                        receivePacket.getLength()));
            } catch (final Exception e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e, e);
                }
                setRunning(false);
            }
        }
    }
}
