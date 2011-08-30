package net.mauhiz.irc.base.ident;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.CompletionHandler;

import org.apache.log4j.Logger;

class BytesWritten implements CompletionHandler<Integer, AsynchronousServerSocketChannel> {

    private static final Logger LOG = Logger.getLogger(BytesWritten.class);
    private final int bytesToWrite;

    public BytesWritten(int bytesToWrite) {
        this.bytesToWrite = bytesToWrite;

    }

    @Override
    public void completed(Integer bytesWritten, AsynchronousServerSocketChannel attachment) {
        if (bytesToWrite != bytesWritten.intValue()) {
            LOG.warn("Could not write all (" + ")");
        }
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        LOG.warn(exc, exc);
    }

}