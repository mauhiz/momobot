package ircbot.dcc;

import ircbot.AbstractIrcBot;
import ircbot.CtcpUtils;
import ircbot.IIrcBot;
import ircbot.IrcUser;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import utils.AbstractRunnable;

/**
 * This class is used to administer a DCC file transfer.
 * @author mauhiz
 */
public class DccFileTransfer implements IDccSubCommands {
    /**
     * The default buffer size to use when sending and receiving files.
     */
    public static final char BUFFER_SIZE = 1024;
    /**
     * son adresse.
     */
    private long             address;
    /**
     * mon mastah.
     */
    private final IIrcBot    bot;
    /**
     * mon fichier local.
     */
    private File             file;
    /**
     * si je recois.
     */
    private final boolean    incoming;
    /**
     * le délai.
     */
    private long             packetDelay;
    /**
     * le port utilisé.
     */
    private int              port;
    /**
     * l'avancement.
     */
    private long             progress;
    /**
     * si j'ai fini.
     */
    private boolean          received;
    /**
     * la taille.
     */
    private final long       size;
    /**
     * la socket.
     */
    private Socket           socket;
    /**
     * le moment de départ.
     */
    private long             startTime;
    /**
     * le timeout.
     */
    private int              timeout;
    /**
     * le type.
     */
    private String           type;
    /**
     * le user.
     */
    private final IrcUser    user;

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
    public DccFileTransfer(final IIrcBot bot1, final File file1, final IrcUser user1, final int timeout1) {
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
    DccFileTransfer(final IIrcBot bot1, final IrcUser user1, final String type1, final String filename,
            final long address1, final int port1, final long size1) {
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
    public final void cancel() throws IOException {
        this.socket.close();
    }

    /**
     * Delay between packets.
     */
    protected final void delay() {
        if (this.packetDelay > 0) {
            AbstractRunnable.sleep(this.packetDelay);
        }
    }

    /**
     * Receive the file in a new thread.
     * @param file1
     *            le fichier
     * @param resume1
     *            si c'est resume
     */
    protected final void doReceive(final File file1, final boolean resume1) {
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
    protected final long getAddress() {
        return this.address;
    }

    /**
     * @return le bot
     */
    protected final IIrcBot getBot() {
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
        return ((AbstractIrcBot) this.bot).getDccManager();
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
     * Returns the progress (in bytes) of the current file transfer. When resuming, this represents the total number of
     * bytes in the file, which may be greater than the amount of bytes resumed in just this transfer.
     * @return the progress of the transfer.
     */
    public final long getProgress() {
        return this.progress;
    }

    /**
     * Returns the progress of the file transfer as a percentage. Note that this should never be negative, but could
     * become greater than 100% if you attempt to resume a larger file onto a partially downloaded file that was
     * smaller.
     * @return the progress of the transfer as a percentage.
     */
    public final double getProgressPercentage() {
        final char cent = 100;
        return cent * this.progress / (double) getSize();
    }

    /**
     * Returns the size (in bytes) of the file being transfered.
     * @return the size of the file. Returns -1 if the sender did not specify this value.
     */
    public final long getSize() {
        return this.size;
    }

    /**
     * @return le socket
     */
    protected final Socket getSocket() {
        return this.socket;
    }

    /**
     * @return le timeout
     */
    protected final int getTimeout() {
        return this.timeout;
    }

    /**
     * Returns the rate of data transfer in bytes per second. This value is an estimate based on the number of bytes
     * transfered since the connection was established.
     * @return data transfer rate in bytes per second.
     */
    public final long getTransferRate() {
        final long time = TimeUnit.SECONDS.convert(System.currentTimeMillis() - this.startTime, TimeUnit.MILLISECONDS);
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
     * Returns true if the file transfer is incoming (somebody is sending the file to us).
     * @return true if the file transfer is incoming.
     */
    public final boolean isIncoming() {
        return this.incoming;
    }

    /**
     * Receives a DccFileTransfer and writes it to the specified file. Resuming allows a partial download to be continue
     * from the end of the current file contents.
     * @param file1
     *            The file to write to.
     * @param resume
     *            True if you wish to try and resume the download instead of overwriting an existing file.
     */
    public final void receive(final File file1, final boolean resume) {
        if (this.received) {
            return;
        }
        this.received = true;
        this.file = file1;
        if (this.type.equals(DCC_SEND) && resume) {
            this.progress = file1.length();
            if (0 == this.progress) {
                doReceive(file1, false);
            } else {
                CtcpUtils.sendCtcpMsg(DCC_RESUME + SPC + "file.ext" + SPC + this.port + SPC + this.progress, this.user
                        .getNick(), ((AbstractIrcBot) this.bot).getOutputThread());
                ((AbstractIrcBot) this.bot).getDccManager().add(this);
            }
        } else {
            this.progress = file1.length();
            doReceive(file1, resume);
        }
    }

    /**
     * Remet à zéro la barre d'avancement.
     */
    protected final void resetProgress() {
        this.progress = 0;
    }

    /**
     * Sets the delay time between sending or receiving each packet. Default is 0. This is useful for throttling the
     * speed of file transfers to maintain a good quality of service for other things on the machine or network.
     * @param millis
     *            The number of milliseconds to wait between packets.
     */
    protected final void setPacketDelay(final long millis) {
        this.packetDelay = millis;
    }

    /**
     * @param port1
     *            le port à régler
     */
    protected final void setPort(final int port1) {
        this.port = port1;
    }

    /**
     * @param socket1
     *            le socket à régler
     */
    protected final void setSocket(final Socket socket1) {
        this.socket = socket1;
    }

    /**
     * @param startTime1
     *            le startTime à régler
     */
    protected final void setStartTime(final long startTime1) {
        this.startTime = startTime1;
    }

    /**
     * @param bytesRead
     *            le nombre d'octets lus
     */
    protected final void updateProgress(final long bytesRead) {
        this.progress += bytesRead;
    }
}
