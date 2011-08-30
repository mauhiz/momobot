package net.mauhiz.irc.base.ident;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.InterruptedByTimeoutException;

import net.mauhiz.util.FileUtil;

import org.apache.log4j.Logger;

class BytesRead implements CompletionHandler<Integer, AsynchronousServerSocketChannel> {
    private static final Logger LOG = Logger.getLogger(BytesRead.class);
    private final ByteBuffer bb;
    private final AsynchronousSocketChannel socket;
    private final String user;

    public BytesRead(AsynchronousSocketChannel socket, String user, ByteBuffer bb) {
        this.socket = socket;
        this.user = user;
        this.bb = bb;
    }

    @Override
    public void completed(Integer bytesRead, AsynchronousServerSocketChannel ss) {
        if (bytesRead.intValue() == -1) {
            LOG.warn("Disconnected");
            return;
        }

        CharBuffer in = FileUtil.ASCII.decode(bb);
        CharBuffer appendix = CharBuffer.wrap(" : USERID : UNIX : " + user);
        CharBuffer out = CharBuffer.allocate(in.limit() + appendix.limit());
        out.put(in);
        out.put(appendix);
        socket.write(FileUtil.ASCII.encode(out), ss, new BytesWritten(out.capacity()));
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        if (exc instanceof InterruptedByTimeoutException) {
            LOG.info("Server did not send data to me in time");
        } else {
            LOG.error(exc, exc);
        }
    }
}