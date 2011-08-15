package net.mauhiz.irc.bot.triggers.cs.query;

import net.mauhiz.irc.bot.triggers.cs.IServer;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang3.time.StopWatch;

public class PingQuery extends AbstractQuery {
    /**
     * char info
     */
    private static final char A2A_PING = 'i';
    private final StopWatch sw = new StopWatch();

    public PingQuery(IServer server) {
        super(server);
    }

    public void afterReceive(byte[] resp) {
        // ignore response
        server.setPing(sw.getTime());
        sw.reset();
    }

    @Override
    public void beforeSend() {
        server.setPing(-1);
        sw.start();
    }

    public byte[] getCmd() {
        return FileUtil.getBytes(Character.toString(A2A_PING), FileUtil.ASCII);
    }
}
