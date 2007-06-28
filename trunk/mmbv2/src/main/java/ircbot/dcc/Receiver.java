package ircbot.dcc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import utils.AbstractRunnable;
import utils.NetUtils;

/**
 * @author mauhiz
 */
class Receiver extends AbstractRunnable {
    /**
     * logger.
     */
    private static final Logger   LOG = Logger.getLogger(Receiver.class);
    /**
     * mon dft.
     */
    private final DccFileTransfer dft;
    /**
     * mon fichier.
     */
    private final File            myFile;
    /**
     * mon resume.
     */
    private final boolean         resume;

    /**
     * @param dft1
     *            le dcc file transfer
     * @param file1
     *            le fichier
     * @param resume1
     *            si c'est un resume
     */
    Receiver(final DccFileTransfer dft1, final File file1, final boolean resume1) {
        super();
        this.dft = dft1;
        this.myFile = file1;
        this.resume = resume1;
    }

    /**
     * @param readBuffer
     * @param readFrom
     * @param ackTo
     * @param copyTo
     * @throws IOException
     */
    protected void continueReading(final ByteBuffer readBuffer, final InputStream readFrom, final OutputStream ackTo,
            final OutputStream copyTo) throws IOException {
        final int bytesRead = readFrom.read(readBuffer.array(), 0, readBuffer.capacity());
        if (bytesRead == -1) {
            return;
        }
        IOUtils.write(readBuffer.array(), copyTo);
        this.dft.updateProgress(bytesRead);
        /*
         * Send back an acknowledgement of how many bytes we have got so far.
         */
        IOUtils.write(NetUtils.unsignedIntegerToByteArray(this.dft.getProgress()), ackTo);
        ackTo.flush();
        this.dft.delay();
        continueReading(readBuffer, readFrom, ackTo, copyTo);
    }

    /**
     * @see utils.AbstractRunnable#run()
     */
    @Override
    public final void run() {
        OutputStream foutput = null;
        try {
            /* Convert the integer address to a proper IP address. */
            final InetAddress ipay = NetUtils.longToIp(this.dft.getAddress());
            /* Connect the socket and set a timeout. */
            this.dft.setSocket(new Socket(ipay, this.dft.getPort()));
            this.dft.getSocket().setSoTimeout((int) TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
            this.dft.setStartTime(System.currentTimeMillis());
            /* No longer possible to resume this transfer once it's underway. */
            this.dft.getManager().remove(this.dft);
            final InputStream input = new BufferedInputStream(this.dft.getSocket().getInputStream());
            final OutputStream output = new BufferedOutputStream(this.dft.getSocket().getOutputStream());
            foutput = new BufferedOutputStream(new FileOutputStream(this.myFile.getCanonicalPath(), this.resume));
            final ByteBuffer inBuffer = ByteBuffer.allocate(DccFileTransfer.BUFFER_SIZE);
            continueReading(inBuffer, input, output, foutput);
            foutput.flush();
            this.dft.getBot().onFileTransferFinished(this.dft, null);
        } catch (final IOException ioe) {
            this.dft.getBot().onFileTransferFinished(this.dft, ioe);
        } finally {
            IOUtils.closeQuietly(foutput);
            try {
                this.dft.getSocket().close();
            } catch (final IOException ioe) {
                LOG.error(ioe, ioe);
            }
        }
    }
}
