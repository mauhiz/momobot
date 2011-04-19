package net.mauhiz.irc.bot.triggers.cs.query;

import java.nio.ByteBuffer;

import net.mauhiz.irc.bot.triggers.cs.IServer;
import net.mauhiz.irc.bot.triggers.cs.ServerFlags;
import net.mauhiz.util.FileUtil;

public class RulesQuery extends AbstractQuery implements ServerFlags {
    /**
     * char info
     */
    private static final char A2S_RULES = 'V';

    public RulesQuery(IServer server) {
        super(server);
    }

    @Override
    public void afterReceive(byte[] resp) {
        ByteBuffer result = ByteBuffer.wrap(resp);
        result.getInt(); // skip int -1
        char state = (char) result.get(); // skip char 'D'
        if (state == S2C_CHALLENGE) { // response is not rules, but challenge
            ChallengeQuery.processChallenge(server, result);
            return;
        }
        int nbRules = result.getShort(); // skip short number of rules
        for (int i = 0; i < nbRules; i++) {
            if (result.remaining() == 0) {
                break;
            }
            String name = getNextString(result);
            String value = getNextString(result);
            LOG.debug(name + "=" + value);
        }
    }

    @Override
    public byte[] getCmd() {
        byte[] cmd = Character.toString(A2S_RULES).getBytes(FileUtil.ASCII);
        ByteBuffer buf = ByteBuffer.allocate(cmd.length + 4);
        buf.put(cmd);
        buf.putInt(server.getChallenge());
        return buf.array();

    }
}
