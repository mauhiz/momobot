package net.mauhiz.irc.base;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcOutput implements IIrcOutput {
    private static final int MAX_SIZE = 50;
    BlockingQueue < String > queue    = new LinkedBlockingQueue < String >(MAX_SIZE);
    PrintWriter              writer;

    IrcOutput(final Socket socket) throws IOException {
        connect(socket.getOutputStream());
    }

    void connect(final OutputStream outStream) {
        this.writer = new PrintWriter(outStream);
    }

    /**
     * @see net.mauhiz.irc.base.IIrcOutput#isReady()
     */
    public boolean isReady() {
        return this.queue.size() > MAX_SIZE;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (true) {
            try {
                String toWrite = this.queue.take();
                Logger.getLogger(IrcOutput.class).info(">> " + toWrite);
                this.writer.println(toWrite);
                this.writer.flush();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * @see net.mauhiz.irc.base.IIrcOutput#sendRawMsg(java.lang.String)
     */
    public void sendRawMsg(final String raw) {
        try {
            this.queue.put(raw);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
