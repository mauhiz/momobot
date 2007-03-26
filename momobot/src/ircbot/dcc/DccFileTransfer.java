package ircbot.dcc;

import ircbot.ACtcp;
import ircbot.AIrcBot;
import ircbot.IrcUser;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import utils.MyRunnable;
import utils.NetUtils;

/**
 * This class is used to administer a DCC file transfer.
 * @since 1.2.0
 * @author Paul James Mutton, <a
 *         href="http://www.jibble.org/">http://www.jibble.org/</a>
 * @version 1.4.4 (Build time: Tue Mar 29 20:58:46 2005)
 */
public class DccFileTransfer implements IDccSubCommands {
    /**
     * @author viper
     */
    class Receiver extends MyRunnable {
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
        Receiver(final DccFileTransfer dft1, final File file1,
                final boolean resume1) {
            super();
            this.dft = dft1;
            this.myFile = file1;
            this.resume = resume1;
        }

        /**
         * @see utils.MyRunnable#run()
         */
        @Override
        public final void run() {
            OutputStream foutput = null;
            Exception exception = null;
            try {
                // Convert the integer address to a proper IP address.
                final InetAddress ip = NetUtils.longToIp(this.dft.getAddress());
                // Connect the socket and set a timeout.
                this.dft.setSocket(new Socket(ip, this.dft.getPort()));
                this.dft.getSocket().setSoTimeout(
                        (int) TimeUnit.MILLISECONDS.convert(30,
                                TimeUnit.SECONDS));
                this.dft.setStartTime(System.currentTimeMillis());
                // No longer possible to resume this transfer once it's
                // underway.
                getManager().removeAwaitingResume(this.dft);
                final InputStream input = new BufferedInputStream(this.dft
                        .getSocket().getInputStream());
                final OutputStream output = new BufferedOutputStream(this.dft
                        .getSocket().getOutputStream());
                foutput = new BufferedOutputStream(new FileOutputStream(
                        this.myFile.getCanonicalPath(), this.resume));
                final byte[] inBuffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while (true) {
                    bytesRead = input.read(inBuffer, 0, inBuffer.length);
                    if (bytesRead == -1) {
                        break;
                    }
                    IOUtils.write(inBuffer, foutput);
                    updateProgress(bytesRead);
                    /*
                     * Send back an acknowledgement of how many bytes we have
                     * got so far.
                     */
                    IOUtils.write(NetUtils
                            .unsignedIntegerToByteArray(getProgress()), output);
                    output.flush();
                    delay();
                }
                foutput.flush();
            } catch (final Exception e) {
                exception = e;
            } finally {
                IOUtils.closeQuietly(foutput);
                try {
                    this.dft.getSocket().close();
                } catch (final IOException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(e, e);
                    }
                }
            }
            this.dft.getBot().onFileTransferFinished(this.dft, exception);
        }
    }
    /**
     * @author viper
     */
    class Sender extends MyRunnable {
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
        Sender(final DccFileTransfer dft1, final boolean allowResume1) {
            super();
            this.dft = dft1;
            this.allowResume = allowResume1;
        }

        /**
         * @see utils.MyRunnable#run()
         */
        @Override
        public void run() {
            BufferedInputStream finput = null;
            Exception exception = null;
            try {
                ServerSocket ss = null;
                if (AIrcBot.PORTS.isEmpty()) {
                    /* Use any free port. */
                    ss = new ServerSocket(0);
                } else {
                    for (final short element : AIrcBot.PORTS) {
                        try {
                            ss = new ServerSocket(element);
                            /* Found a port number we could use. */
                            break;
                        } catch (final IOException e) {
                            continue;
                        }
                    }
                    if (ss == null) {
                        /* No ports could be used. */
                        throw new IOException(
                                "All ports returned by getDccPorts() are in use.");
                    }
                }
                ss.setSoTimeout(this.dft.getTimeout());
                this.dft.setPort(ss.getLocalPort());
                // TODO : cas d'un NAT
                final InetAddress inetAddress = InetAddress.getLocalHost();
                final long ipNum = NetUtils.byteTabIpToLong(inetAddress
                        .getAddress());
                /*
                 * Rename the filename so it has no whitespace in it when we
                 * send it.
                 */
                final String safeFilename = StringUtils
                        .deleteWhitespace(this.dft.getFile().getName());
                if (this.allowResume) {
                    this.dft.getManager().addAwaitingResume(
                            DccFileTransfer.this);
                }
                /*
                 * Send the message to the user, telling them where to connect
                 * to in order to get the file.
                 */
                ACtcp.sendCtcpMsg(DCC_SEND + SPC + safeFilename + SPC + ipNum
                        + SPC + this.dft.getPort() + SPC
                        + this.dft.getFile().length(), this.dft.getUser()
                        .getNick(), this.dft.getBot().getOutputThread());
                // The client may now connect to us and download the file.
                this.dft.setSocket(ss.accept());
                this.dft.getSocket().setSoTimeout(
                        (int) TimeUnit.MILLISECONDS
                                .convert(5, TimeUnit.MINUTES));
                this.dft.setStartTime(System.currentTimeMillis());
                // No longer possible to resume this transfer once it's
                // underway.
                if (this.allowResume) {
                    getManager().removeAwaitingResume(DccFileTransfer.this);
                }
                // Might as well close the server socket now; it's finished
                // with.
                ss.close();
                final OutputStream output = new BufferedOutputStream(this.dft
                        .getSocket().getOutputStream());
                final InputStream input = new BufferedInputStream(this.dft
                        .getSocket().getInputStream());
                finput = new BufferedInputStream(new FileInputStream(this.dft
                        .getFile()));
                // Check for resuming.
                if (this.dft.getProgress() > 0) {
                    long bytesSkipped = 0;
                    while (bytesSkipped < this.dft.getProgress()) {
                        bytesSkipped += finput.skip(this.dft.getProgress()
                                - bytesSkipped);
                    }
                }
                final byte[] outBuffer = new byte[BUFFER_SIZE];
                final byte[] inBuffer = new byte[Integer.SIZE / Byte.SIZE];
                int bytesRead = -1;
                while (true) {
                    bytesRead = finput.read(outBuffer, 0, outBuffer.length);
                    if (bytesRead == -1) {
                        break;
                    }
                    IOUtils.write(outBuffer, output);
                    output.flush();
                    input.read(inBuffer, 0, inBuffer.length);
                    updateProgress(bytesRead);
                    delay();
                }
            } catch (final Exception e) {
                exception = e;
            } finally {
                IOUtils.closeQuietly(finput);
                try {
                    this.dft.getSocket().close();
                } catch (final IOException e) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(e, e);
                    }
                }
            }
            getBot().onFileTransferFinished(DccFileTransfer.this, exception);
        }
    }
    /**
     * The default buffer size to use when sending and receiving files.
     */
    public static final int BUFFER_SIZE = 1024;
    /**
     * logger.
     */
    static final Logger     LOG         = Logger
                                                .getLogger(DccFileTransfer.class);
    /**
     * son adresse.
     */
    private long            address     = 0;
    /**
     * mon mastah.
     */
    private AIrcBot         bot         = null;
    /**
     * mon fichier local.
     */
    private File            file        = null;
    /**
     * si je recois.
     */
    private boolean         incoming    = false;
    /**
     * le délai.
     */
    private long            packetDelay = 0;
    /**
     * le port utilisé.
     */
    private int             port        = 0;
    /**
     * l'avancement.
     */
    private long            progress    = 0;
    /**
     * si j'ai fini.
     */
    private boolean         received    = false;
    /**
     * la taille.
     */
    private long            size        = 0;
    /**
     * la socket.
     */
    private Socket          socket      = null;
    /**
     * le moment de départ.
     */
    private long            startTime   = 0;
    /**
     * le timeout.
     */
    private int             timeout     = 0;
    /**
     * le type.
     */
    private String          type;
    /**
     * le user.
     */
    private IrcUser         user        = null;

    /**
     * Constructor used for sending files.
     * @param bot1
     *            le bot
     * @param file1
     *            le fichier
     * @param user1
     *            le user
     * @param timeout1
     *            le temps de timeout
     */
    public DccFileTransfer(final AIrcBot bot1, final File file1,
            final IrcUser user1, final int timeout1) {
        this.bot = bot1;
        this.user = user1;
        this.file = file1;
        this.size = this.file.length();
        this.timeout = timeout1;
        this.received = true;
        this.incoming = false;
    }

    /**
     * Constructor used for receiving files.
     * @param bot1
     *            le bot
     * @param user1
     *            le user
     * @param type1
     *            le type
     * @param filename
     *            le nom de fichier
     * @param address1
     *            l'addresse
     * @param port1
     *            le port
     * @param size1
     *            la taille
     */
    DccFileTransfer(final AIrcBot bot1, final IrcUser user1,
            final String type1, final String filename, final long address1,
            final int port1, final long size1) {
        this.bot = bot1;
        this.user = user1;
        this.type = type1;
        this.file = new File(filename);
        this.address = address1;
        this.port = port1;
        this.size = size1;
        this.received = false;
        this.incoming = true;
    }

    /**
     * Stops the DCC file transfer by closing the connection.
     * @throws IOException
     *             en cas d'erreur
     */
    public final void close() throws IOException {
        this.socket.close();
    }

    /**
     * Delay between packets.
     */
    final void delay() {
        if (this.packetDelay > 0) {
            try {
                Thread.sleep(this.packetDelay);
            } catch (final InterruptedException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e, e);
                }
            }
        }
    }

    /**
     * Receive the file in a new thread.
     * @param file1
     *            le fichier
     * @param resume1
     *            si c'est resume
     */
    final void doReceive(final File file1, final boolean resume1) {
        new Receiver(this, file1, resume1).execute();
    }

    /**
     * Method to send the file inside a new thread.
     * @param allowResume
     *            si on a le droit de resume.
     */
    public final void doSend(final boolean allowResume) {
        new Sender(this, allowResume).execute();
    }

    /**
     * @return le address
     */
    final long getAddress() {
        return this.address;
    }

    /**
     * @return le bot
     */
    final AIrcBot getBot() {
        return this.bot;
    }

    /**
     * @return the suggested file to be used.
     */
    public final File getFile() {
        return this.file;
    }

    /**
     * @return le dcc manager du bot
     */
    protected final DccManager getManager() {
        return this.bot.getDccManager();
    }

    /**
     * returns the delay time between each packet that is send or received.
     * @return the delay between each packet.
     */
    public final long getPacketDelay() {
        return this.packetDelay;
    }

    /**
     * Returns the port number to be used when making the connection.
     * @return the port number.
     */
    public final int getPort() {
        return this.port;
    }

    /**
     * Returns the progress (in bytes) of the current file transfer. When
     * resuming, this represents the total number of bytes in the file, which
     * may be greater than the amount of bytes resumed in just this transfer.
     * @return the progress of the transfer.
     */
    public final long getProgress() {
        return this.progress;
    }

    /**
     * Returns the progress of the file transfer as a percentage. Note that this
     * should never be negative, but could become greater than 100% if you
     * attempt to resume a larger file onto a partially downloaded file that was
     * smaller.
     * @return the progress of the transfer as a percentage.
     */
    public final double getProgressPercentage() {
        final char cent = 100;
        return cent * this.progress / (double) getSize();
    }

    /**
     * Returns the size (in bytes) of the file being transfered.
     * @return the size of the file. Returns -1 if the sender did not specify
     *         this value.
     */
    public final long getSize() {
        return this.size;
    }

    /**
     * @return le socket
     */
    final Socket getSocket() {
        return this.socket;
    }

    /**
     * @return le timeout
     */
    final int getTimeout() {
        return this.timeout;
    }

    /**
     * Returns the rate of data transfer in bytes per second. This value is an
     * estimate based on the number of bytes transfered since the connection was
     * established.
     * @return data transfer rate in bytes per second.
     */
    public final long getTransferRate() {
        final long time = TimeUnit.SECONDS.convert(System.currentTimeMillis()
                - this.startTime, TimeUnit.MILLISECONDS);
        if (time <= 0) {
            return 0;
        }
        return getProgress() / time;
    }

    /**
     * @return le user
     */
    public final IrcUser getUser() {
        return this.user;
    }

    /**
     * Returns true if the file transfer is incoming (somebody is sending the
     * file to us).
     * @return true if the file transfer is incoming.
     */
    public final boolean isIncoming() {
        return this.incoming;
    }

    /**
     * Receives a DccFileTransfer and writes it to the specified file. Resuming
     * allows a partial download to be continue from the end of the current file
     * contents.
     * @param file1
     *            The file to write to.
     * @param resume
     *            True if you wish to try and resume the download instead of
     *            overwriting an existing file.
     */
    public final synchronized void receive(final File file1,
            final boolean resume) {
        if (!this.received) {
            this.received = true;
            this.file = file1;
            if (this.type.equals(DCC_SEND) && resume) {
                this.progress = file1.length();
                if (this.progress == 0) {
                    doReceive(file1, false);
                } else {
                    ACtcp.sendCtcpMsg(DCC_RESUME + SPC + "file.ext" + SPC
                            + this.port + SPC + this.progress, this.user
                            .getNick(), this.bot.getOutputThread());
                    this.bot.getDccManager().addAwaitingResume(this);
                }
            } else {
                this.progress = file1.length();
                doReceive(file1, resume);
            }
        }
    }

    /**
     * Remet à zéro la barre d'avancement.
     */
    final void resetProgress() {
        this.progress = 0;
    }

    /**
     * Sets the delay time between sending or receiving each packet. Default is
     * 0. This is useful for throttling the speed of file transfers to maintain
     * a good quality of service for other things on the machine or network.
     * @param millis
     *            The number of milliseconds to wait between packets.
     */
    final void setPacketDelay(final long millis) {
        this.packetDelay = millis;
    }

    /**
     * @param port1
     *            le port à régler
     */
    final void setPort(final int port1) {
        this.port = port1;
    }

    /**
     * @param socket1
     *            le socket à régler
     */
    final void setSocket(final Socket socket1) {
        this.socket = socket1;
    }

    /**
     * @param startTime1
     *            le startTime à régler
     */
    final void setStartTime(final long startTime1) {
        this.startTime = startTime1;
    }

    /**
     * @param bytesRead
     *            le nombre d'octets lus
     */
    final void updateProgress(final long bytesRead) {
        this.progress += bytesRead;
    }
}
