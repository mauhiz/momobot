package net.mauhiz.irc.base.ident;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.mauhiz.util.FileUtil;

import org.apache.log4j.Logger;

class SocketAccepted implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private static final Logger LOG = Logger.getLogger(SocketAccepted.class);
    private final String user;

    public SocketAccepted(String user) {
        this.user = user;
    }

    @Override
    public void completed(AsynchronousSocketChannel socket, AsynchronousServerSocketChannel ss) {
        ByteBuffer bb = ByteBuffer.allocate(FileUtil.BUF_SIZE);
        socket.read(bb, 5, TimeUnit.SECONDS, ss, new BytesRead(socket, user, bb));
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        if (exc instanceof TimeoutException) {
            LOG.info("Server did not connect to me in time");
        } else {
            LOG.warn(exc, exc);
        }
    }
}