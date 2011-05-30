package net.mauhiz.irc.base.ident;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.FileUtil;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IdentServer extends AbstractDaemon implements IIdentServer {

    private static final Logger LOGGER = Logger.getLogger(IdentServer.class);
    /**
     * le port sur lequel on ecoute.
     */
    private static final int PORT = 113;
    /**
     * Timeout en millisecondes.
     */
    private static final int SO_TIMEOUT = 60 * 1000;
    /**
     * le serversocket.
     */
    private ServerSocket ss;
    private final String user;

    /**
     * @param user1
     */
    public IdentServer(IrcUser user1) {
        super("Ident Server");
        user = user1.getMask().getUser();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void trun() {

        try {
            ss = new ServerSocket(PORT);
            ss.setSoTimeout(SO_TIMEOUT);

            Socket socket = ss.accept();
            socket.setSoTimeout(SO_TIMEOUT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            try {
                String line = new BufferedReader(new InputStreamReader(socket.getInputStream(), FileUtil.ASCII))
                        .readLine();
                if (line != null) {
                    writer.println(line + " : USERID : UNIX : " + user);
                }
            } finally {
                writer.close();
            }

        } catch (BindException be) {
            LOGGER.error("Could not bind on port: " + PORT, be);

        } catch (SocketTimeoutException ste) {
            LOGGER.debug(ste);
            // nevermind
        } catch (IOException ioe) {
            LOGGER.error(ioe, ioe);
        }
        tstop();
    }

    @Override
    public void tstop() {
        super.tstop();
        if (ss != null) {
            try {
                ss.close();

            } catch (IOException ioe) {
                LOGGER.warn(ioe, ioe);
            }
        }
    }
}
