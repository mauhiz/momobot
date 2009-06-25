package net.mauhiz.irc.bot.triggers.cs.query;

import net.mauhiz.irc.bot.triggers.cs.Server;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.time.StopWatch;

public class PingQuery extends AbstractQuery {
    /**
     * char info
     */
    private static final char A2A_PING = 'i';
    private final StopWatch sw = new StopWatch();
    public PingQuery(Server server) {
        super(server);
    }
    @Override
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
    
    @Override
    public byte[] getCmd() {
        return Character.toString(A2A_PING).getBytes(FileUtil.ASCII);
    }
}
