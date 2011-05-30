package net.mauhiz.irc.bot.triggers.cs.query;

import java.nio.ByteBuffer;

import net.mauhiz.irc.bot.triggers.cs.IServer;
import net.mauhiz.util.FileUtil;

public class ChallengeQuery extends AbstractQuery {
    private static final char A2S_SERVERQUERY_GETCHALLENGE = 'W';

    static void processChallenge(IServer server, ByteBuffer result) {
        server.setChallenge(result.getInt());
        LOG.debug("updated query challenge : " + server.getChallenge());
    }

    public ChallengeQuery(IServer server) {
        super(server);
    }

    public void afterReceive(byte[] resp) {
        ByteBuffer result = ByteBuffer.wrap(resp);
        result.getInt(); // skip int -1
        result.get(); // skip char 'A'
        processChallenge(server, result);
    }

    public byte[] getCmd() {
        return FileUtil.getBytes(Character.toString(A2S_SERVERQUERY_GETCHALLENGE), FileUtil.ASCII);
    }
}
