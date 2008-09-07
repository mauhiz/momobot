package net.mauhiz.irc.base.ident;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.AbstractRunnable;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IdentServer extends AbstractRunnable implements IIdentServer {
    /**
     * le port sur lequel on écoute.
     */
    private static final char PORT = 113;
    /**
     * Timeout en millisecondes.
     */
    private static final int SO_TIMEOUT = (int) TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
    /**
     * le serversocket.
     */
    private final ServerSocket ss;
    private final String user;
    /**
     * @param user1
     * @throws IOException
     */
    public IdentServer(final String user1) throws IOException {
        user = user1;
        ss = new ServerSocket(PORT);
        ss.setSoTimeout(SO_TIMEOUT);
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        setRunning(true);
        try {
            final Socket socket = ss.accept();
            socket.setSoTimeout(SO_TIMEOUT);
            final PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            final String line = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            if (line != null) {
                writer.println(line + " : USERID : UNIX : " + user);
            }
            IOUtils.closeQuietly(writer);
            stop();
        } catch (IOException e) {
            Logger.getLogger(IdentServer.class).error(e);
        }
        setRunning(false);
    }
    
    /**
     * @see net.mauhiz.irc.base.ident.IIdentServer#start()
     */
    public void start() {
        startAs("ident server");
    }
    
    /**
     * @see net.mauhiz.irc.base.ident.IIdentServer#stop()
     */
    public void stop() throws IOException {
        ss.close();
        setRunning(false);
    }
}
