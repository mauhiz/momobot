package net.mauhiz.irc.bouncer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author viper
 */
public class BncClient {
    Socket connection;
    long connectionTime;
    PrintWriter writer;

    /**
     * @param connection1
     * @throws IOException
     */
    public BncClient(final Socket connection1) throws IOException {
        connection = connection1;
        connectionTime = System.currentTimeMillis();
        writer = new PrintWriter(connection.getOutputStream());
    }

    /**
     * @throws IOException
     */
    void close() throws IOException {
        writer.close();
        connection.close();
    }

    /**
     * @param data
     */
    void sendData(final String data) {
        writer.write(data);
        writer.write("\r\n");
        writer.flush();
    }
}
