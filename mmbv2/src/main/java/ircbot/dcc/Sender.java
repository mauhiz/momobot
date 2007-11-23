package ircbot.dcc;

import ircbot.AbstractIrcBot;
import ircbot.CtcpUtils;
import ircbot.IIrcSpecialChars;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import utils.AbstractRunnable;
import utils.NetUtils;

/**
 * @author mauhiz
 */
class Sender extends AbstractRunnable implements IIrcSpecialChars, IDccSubCommands {
    /**
     * logger.
     */
    private static final Logger   LOG = Logger.getLogger(Sender.class);
    /**
     * Si j'ai le droit de resume.
     */
    private final boolean         allowResume;
    /**
     * Mon dcc file transfer.
     */
    private final DccFileTransfer dft;

    /**
     * @param dft1
     *            dft
     * @param allowResume1
     *            ar
     */
    protected Sender(final DccFileTransfer dft1, final boolean allowResume1) {
        super();
        this.dft = dft1;
        this.allowResume = allowResume1;
    }

    /**
     * @param readBuffer
     * @param readFrom
     * @param input
     * @param output
     * @throws IOException
     */
    protected void continueReading(final ByteBuffer readBuffer, final InputStream readFrom, final InputStream input,
            final OutputStream output) throws IOException {
        final int bytesRead = readFrom.read(readBuffer.array(), 0, readBuffer.capacity());
        if (bytesRead == -1) {
            return;
        }
        IOUtils.write(readBuffer.array(), output);
        output.flush();
        /* on ignore la réponse d'avancement. */
        final long skipped = input.skip(Integer.SIZE / Byte.SIZE);
        /* TODO On devrait vérifier que tout a été skippé. */
        if (LOG.isDebugEnabled()) {
            LOG.debug(Long.valueOf(skipped));
        }
        this.dft.updateProgress(bytesRead);
        this.dft.delay();
        continueReading(readBuffer, readFrom, input, output);
    }

    /**
     * @see AbstractRunnable#run()
     */
    public void run() {
        InputStream finput = null;
        try {
            final ServerSocket server = AbstractIrcBot.tryToBind(AbstractIrcBot.PORTS);
            server.setSoTimeout(this.dft.getTimeout());
            this.dft.setPort(server.getLocalPort());
            /* TODO : cas d'un NAT */
            final long ipNum = NetUtils.byteTabIpToLong(InetAddress.getLocalHost().getAddress());
            /* Rename the filename so it has no whitespace in it when we send it. */
            final String safeFilename = StringUtils.deleteWhitespace(this.dft.getFile().getName());
            if (this.allowResume) {
                this.dft.getManager().add(this.dft);
            }
            /* Send the message to the user, telling them where to connect to in order to get the file. */
            CtcpUtils.sendCtcpMsg(DCC_SEND + SPC + safeFilename + SPC + ipNum + SPC + this.dft.getPort() + SPC
                    + this.dft.getFile().length(), this.dft.getUser().getNick(), ((AbstractIrcBot) this.dft.getBot())
                    .getOutputThread());
            /* The client may now connect to us and download the file. */
            this.dft.setSocket(server.accept());
            this.dft.getSocket().setSoTimeout((int) TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES));
            this.dft.setStartTime(System.currentTimeMillis());
            /* No longer possible to resume this transfer once it's underway. */
            if (this.allowResume) {
                this.dft.getManager().remove(this.dft);
            }
            /* Might as well close the server socket now; it's finished with. */
            server.close();
            final OutputStream output = new BufferedOutputStream(this.dft.getSocket().getOutputStream());
            final InputStream input = new BufferedInputStream(this.dft.getSocket().getInputStream());
            finput = new BufferedInputStream(new FileInputStream(this.dft.getFile()));
            /* Check for resuming. */
            if (this.dft.getProgress() > 0) {
                long bytesSkipped = 0;
                while (bytesSkipped < this.dft.getProgress()) {
                    bytesSkipped += finput.skip(this.dft.getProgress() - bytesSkipped);
                }
            }
            final ByteBuffer outBuffer = ByteBuffer.allocate(DccFileTransfer.BUFFER_SIZE);
            continueReading(outBuffer, finput, input, output);
            this.dft.getBot().onFileTransferFinished(this.dft, null);
        } catch (final IOException ioe) {
            this.dft.getBot().onFileTransferFinished(this.dft, ioe);
        } finally {
            IOUtils.closeQuietly(finput);
            try {
                this.dft.getSocket().close();
            } catch (final IOException ioe) {
                LOG.error(ioe, ioe);
            }
        }
    }
}
