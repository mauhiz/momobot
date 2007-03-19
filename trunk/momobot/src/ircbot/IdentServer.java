package ircbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import utils.MyRunnable;
import utils.Utils;

/**
 * A simple IdentServer (also know as "The Identification Protocol"). An ident
 * server provides a means to determine the identity of a user of a particular
 * TCP connection.
 * <p>
 * Most IRC servers attempt to contact the ident server on connecting hosts in
 * order to determine the user's identity. A few IRC servers will not allow you
 * to connect unless this information is provided.
 * <p>
 * So when a PircBot is run on a machine that does not run an ident server, it
 * may be necessary to provide a "faked" response by starting up its own ident
 * server and sending out apparently correct responses.
 * <p>
 * An instance of this class can be used to start up an ident server only if it
 * is possible to do so. Reasons for not being able to do so are if there is
 * already an ident server running on port 113, or if you are running as an
 * unprivileged user who is unable to create a server socket on that port
 * number.
 * @since 0.9c
 * @author Paul James Mutton, <a
 *         href="http://www.jibble.org/">http://www.jibble.org/</a>
 * @version 1.4.4 (Build time: Tue Mar 29 20:58:46 2005)
 */
public class IdentServer extends MyRunnable {
    /**
     * le port sur lequel on écoute.
     */
    private static final int PORT = 113;

    /**
     * le login à faire passer.
     */
    private final String     login;

    /**
     * le serversocket.
     */
    private ServerSocket     ss   = null;

    /**
     * Constructs and starts an instance of an IdentServer that will respond to
     * a client with the provided login. Rather than calling this constructor
     * explicitly from your code, it is recommended that you use the
     * startIdentServer method in the PircBot class.
     * <p>
     * The ident server will wait for up to 60 seconds before shutting down.
     * Otherwise, it will shut down as soon as it has responded to an ident
     * request.
     * @param login1
     *            The login that the ident server will respond with.
     */
    IdentServer(final String login1) {
        this.login = login1;
        try {
            this.ss = new ServerSocket(PORT);
            this.ss.setSoTimeout((int) TimeUnit.MILLISECONDS.convert(1,
                    TimeUnit.MINUTES));
        } catch (final Exception e) {
            Utils.logError(getClass(), e);
            return;
        }
    }

    /**
     * Waits for a client to connect to the ident server before making an
     * appropriate response. Note that this method is started by the class
     * constructor.
     */
    @Override
    public final void run() {
        setRunning(true);
        Utils.log(getClass(), "server running on port " + PORT
                + " for the next 60 seconds...");
        try {
            final Socket socket = this.ss.accept();
            socket.setSoTimeout((int) TimeUnit.MILLISECONDS.convert(1,
                    TimeUnit.MINUTES));
            final PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream()), true);
            final String line = new BufferedReader(new InputStreamReader(socket
                    .getInputStream())).readLine();
            if (line != null) {
                Utils.log(getClass(), "request received: " + line);
                IOUtils.write(line + " : USERID : UNIX : " + this.login
                        + "\r\n", writer);
                Utils.log(getClass(), ">> " + line);
            }
            this.ss.close();
            IOUtils.closeQuietly(writer);
        } catch (final IOException e) {
            Utils.logError(getClass(), e);
        }
        setRunning(false);
        Utils.log(getClass(), "server has been shut down.");
    }
}
