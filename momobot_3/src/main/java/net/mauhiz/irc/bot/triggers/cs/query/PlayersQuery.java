package net.mauhiz.irc.bot.triggers.cs.query;

import java.nio.ByteBuffer;

import net.mauhiz.irc.bot.triggers.cs.IServer;
import net.mauhiz.irc.bot.triggers.cs.Player;
import net.mauhiz.irc.bot.triggers.cs.ServerFlags;
import net.mauhiz.util.FileUtil;

public class PlayersQuery extends AbstractQuery implements ServerFlags {
    /**
     * char info
     */
    private static final char A2S_PLAYERS = 'U';

    public PlayersQuery(IServer server) {
        super(server);
    }

    public void afterReceive(byte[] resp) {
        ByteBuffer result = ByteBuffer.wrap(resp);
        result.getInt(); // skip int -1
        char state = (char) result.get(); // skip char 'D'
        if (state == S2C_CHALLENGE) { // response is not players, but challenge
            ChallengeQuery.processChallenge(server, result);
            return;
        }
        int nbPlayers = result.get();
        server.setPlayerCount(nbPlayers);
        for (int i = 0; i < nbPlayers; i++) {
            if (result.remaining() == 0) {
                break;
            }
            int index = result.get(); // index du player
            LOG.debug("index: " + index);
            String name = getNextString(result);
            LOG.debug("name: " + name);
            int frags = Integer.reverseBytes(result.getInt());
            LOG.debug("frags: " + frags);
            float timeConnected = result.getFloat(); // skip float time connected
            LOG.debug("time connected: " + timeConnected);

            Player player = new Player(null);
            player.setName(name);
            player.setFrags(frags);
            server.setPlayer(index, player);
        }
    }

    @Override
    public void beforeSend() {
        server.resetPlayers();
    }

    public byte[] getCmd() {
        byte[] cmd = FileUtil.getBytes(Character.toString(A2S_PLAYERS), FileUtil.ASCII);
        ByteBuffer buf = ByteBuffer.allocate(cmd.length + 4);
        buf.put(cmd);
        buf.putInt(server.getChallenge());
        return buf.array();
    }
}
