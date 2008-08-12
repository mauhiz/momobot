package net.mauhiz.irc.base.ident;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IdentServer implements IIdentServer {
    /**
     * le port sur lequel on écoute.
     */
    private static final char PORT       = 113;
    /**
     * Timeout en millisecondes.
     */
    static final int          SO_TIMEOUT = (int) TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
    /**
     * le serversocket.
     */
    ServerSocket              ss;

    /**
     * @see net.mauhiz.irc.base.ident.IIdentServer#start(java.lang.String)
     */
    public void start(final String user) throws IOException {
        this.ss = new ServerSocket(PORT);
        this.ss.setSoTimeout(SO_TIMEOUT);
        Runnable accept = new Runnable() {
            public void run() {
                try {
                    final Socket socket = IdentServer.this.ss.accept();
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
            }
        };
        new Thread(accept).start();
    }

    /**
     * @see net.mauhiz.irc.base.ident.IIdentServer#stop()
     */
    public void stop() throws IOException {
        this.ss.close();
    }
}
