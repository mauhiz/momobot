package ircbot.dcc;

import ircbot.IIrcSpecialChars;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

import utils.NetUtils;

/**
 * This class is used to allow the bot to interact with a DCC Chat session.
 * @author Paul James Mutton, <a>http://www.jibble.org/</a>
 */
public class DccChat implements IIrcSpecialChars {
    /**
     * message d'erreur.
     */
    private static final String     NOT_ACCEPTABLE = "You must call the accept() method of the DccChat request before you can use it.";
    /**
     * c'est bon?
     */
    private boolean                 acceptable;
    /**
     * son ip et son port.
     */
    private final InetSocketAddress address;
    /**
     * mon lecteur.
     */
    private BufferedReader          reader;
    /**
     * ma chaussette.
     */
    private Socket                  socket;
    /**
     * mon écrivain.
     */
    private Writer                  writer;

    /**
     * This constructor is used when we are accepting a DCC CHAT request from somebody. It attempts to connect to the
     * client that issued the request.
     * @param longAddress
     *            The address to connect to.
     * @param port
     *            The port number to connect to.
     */
    public DccChat(final long longAddress, final int port) {
        this.address = new InetSocketAddress(NetUtils.longToIp(longAddress), port);
        this.acceptable = true;
    }

    /**
     * This constructor is used after we have issued a DCC CHAT request to somebody. If the client accepts the chat
     * request, then the socket we obtain is passed to this constructor.
     * @param socket1
     *            The socket which will be used for the DCC CHAT session.
     * @throws IOException
     *             If the socket cannot be read from.
     */
    public DccChat(final Socket socket1) throws IOException {
        this.socket = socket1;
        this.address = null;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())));
    }

    /**
     * Accept this DccChat connection.
     * @throws IOException
     *             en cas d'erreur
     */
    public final void accept() throws IOException {
        if (this.acceptable) {
            this.acceptable = false;
            this.socket = new Socket(this.address.getAddress(), this.address.getPort());
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())));
        }
    }

    /**
     * Closes the DCC Chat connection.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public final void close() throws IOException {
        if (this.acceptable) {
            throw new IOException(NOT_ACCEPTABLE);
        }
        IOUtils.closeQuietly(this.reader);
        IOUtils.closeQuietly(this.writer);
        this.socket.close();
    }

    /**
     * Reads the next line of text from the client at the other end of our DCC Chat connection. This method blocks until
     * something can be returned. If the connection has closed, null is returned.
     * @return The next line of text from the client. Returns null if the connection has closed normally.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public final String readLine() throws IOException {
        if (this.acceptable) {
            throw new IOException(NOT_ACCEPTABLE);
        }
        return this.reader.readLine();
    }

    /**
     * Sends a line of text to the client at the other end of our DCC Chat connection.
     * @param line
     *            The line of text to be sent. This should not include linefeed characters.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public final void sendLine(final String line) throws IOException {
        if (this.acceptable) {
            throw new IOException(NOT_ACCEPTABLE);
        }
        IOUtils.write(line + CR + LF, this.writer);
        this.writer.flush();
    }
}
