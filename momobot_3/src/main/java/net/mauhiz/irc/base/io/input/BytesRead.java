package net.mauhiz.irc.base.io.input;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.CompletionHandler;

import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

class BytesRead implements CompletionHandler<Integer, ByteBuffer> {
    private static final Logger LOG = Logger.getLogger(BytesRead.class);
    private final IIrcIO io;
    private final IIrcInput ircInput;
    private String unprocessedChunk;

    public BytesRead(IIrcInput ircInput, IIrcIO io) {
        this.ircInput = ircInput;
        this.io = io;
    }

    @Override
    public void completed(Integer bytesRead, ByteBuffer bb) {
        if (bytesRead.intValue() == -1) {
            LOG.warn("disconnected");
            ircInput.tstop();
        } else {
            handleChunks(bb);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer bb) {
        LOG.error(exc, exc);
    }

    private void handleChunks(ByteBuffer bb) {
        int limit = bb.position();
        bb.rewind();
        bb.limit(limit);
        CharBuffer cb = FileUtil.ISO8859_15.decode(bb);
        String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(unprocessedChunk + cb, "\r\n");

        for (int i = 0; i < lines.length - 1; i++) {
            LOG.debug("<< " + lines[i]);
            io.processMsg(lines[i]);
        }
        unprocessedChunk = lines[lines.length - 1];
    }
}