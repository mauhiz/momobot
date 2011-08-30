package net.mauhiz.irc.base.io.output;

import java.nio.channels.CompletionHandler;

import org.apache.log4j.Logger;

class BytesWritten implements CompletionHandler<Integer, Void> {
    private static final Logger LOG = Logger.getLogger(BytesWritten.class);

    private final int bytesToWriteCount;
    private final IIrcOutput ircOutput;

    public BytesWritten(int bytesToWriteCount, IIrcOutput ircOutput) {
        this.bytesToWriteCount = bytesToWriteCount;
        this.ircOutput = ircOutput;
    }

    @Override
    public void completed(Integer fWritten, Void attachment) {
        int written = fWritten.intValue();
        if (written == -1) {
            LOG.warn("Disconnected");
            ircOutput.tstop();

        } else if (written != bytesToWriteCount) {
            LOG.error("Could not write all (" + written + "/" + bytesToWriteCount + ")");
        }
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        LOG.error("Could not write to socket", exc);
        ircOutput.tstop();
    }

}