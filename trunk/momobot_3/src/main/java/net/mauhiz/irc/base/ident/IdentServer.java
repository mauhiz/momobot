package net.mauhiz.irc.base.ident;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.util.AbstractRunnable;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IdentServer extends AbstractRunnable implements IIdentServer {
    
    private static final Logger LOGGER = Logger.getLogger(IdentServer.class);
    /**
     * le port sur lequel on ecoute.
     */
    private static final int PORT = 113;
    /**
     * Timeout en millisecondes.
     */
    private static final int SO_TIMEOUT = (int) TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
    /**
     * le serversocket.
     */
    private ServerSocket ss;
    private final String user;
    
    /**
     * @param user1
     * @throws IOException
     */
    public IdentServer(IrcUser user1) throws IOException {
        super();
        user = user1.getUser();
        
        try {
            ss = new ServerSocket(PORT);
            ss.setSoTimeout(SO_TIMEOUT);
            
        } catch (BindException be) {
            if (SystemUtils.IS_OS_LINUX) { // not root => bind denied
                LOGGER.info("Could not bind on port: " + PORT + ". Maybe I should be rootz?");
            } else {
                throw be;
            }
        }
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        if (ss == null) {
            stop();
            return;
        }
        try {
            Socket socket = ss.accept();
            socket.setSoTimeout(SO_TIMEOUT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            String line = new BufferedReader(new InputStreamReader(socket.getInputStream(), FileUtil.ISO8859_15))
                    .readLine();
            if (line != null) {
                writer.println(line + " : USERID : UNIX : " + user);
            }
            writer.close();
        } catch (SocketTimeoutException ste) {
            // nevermind
        } catch (IOException ioe) {
            LOGGER.error(ioe, ioe);
        }
        stop();
    }
    
    /**
     * @see net.mauhiz.irc.base.ident.IIdentServer#start()
     */
    public void start() {
        startAs("ident server");
    }
    
    /**
     * @see net.mauhiz.util.AbstractRunnable#stop()
     */
    @Override
    public void stop() {
        if (ss != null) {
            try {
                ss.close();
                
            } catch (IOException ioe) {
                LOGGER.warn(ioe, ioe);
            }
        }
        super.stop();
    }
}
