package net.mauhiz.irc.bot.triggers.cs.query;

import java.nio.ByteBuffer;

import net.mauhiz.irc.bot.triggers.cs.IServer;
import net.mauhiz.irc.bot.triggers.cs.ServerFlags;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.time.StopWatch;

public class InfoQuery extends AbstractQuery implements ServerFlags {

    /**
     * char info
     */
    private static final char A2S_INFO = 'T';

    private final StopWatch sw = new StopWatch();

    public InfoQuery(IServer server) {
        super(server);
    }

    @Override
    public void afterReceive(byte[] resp) {
        // ignore response
        server.setPing(sw.getTime());
        sw.reset();
        ByteBuffer result = ByteBuffer.wrap(resp);

        result.getInt(); // ignore that
        char type = (char) result.get();
        LOG.debug("Type: " + Character.toString(type));
        if (type == GOLDSOURCE) {
            getDetailsGoldSource(result);
        } else if (type == SOURCE) {
            getDetailsSource(result);
        } else {
            LOG.warn("Serveur inconnu (type = " + type + ")");
        }
    }

    @Override
    public void beforeSend() {
        server.setPing(-1);
        sw.start();
    }

    @Override
    public byte[] getCmd() {
        return (A2S_INFO + QUERY).getBytes(FileUtil.ASCII);
    }

    /**
     * @param result
     */
    private void getDetailsGoldSource(ByteBuffer result) {
        String adresse = getNextString(result);
        LOG.debug("adresse: " + adresse);
        server.setName(getNextString(result));
        LOG.debug("name: " + server.getName());
        server.setMap(getNextString(result));
        LOG.debug("map: " + server.getMap());
        String gameDir = getNextString(result);
        LOG.debug("gameDir: " + gameDir);
        String gameDesc = getNextString(result);
        LOG.debug("gameDesc: " + gameDesc);
        int playerCount = result.get();
        server.setPlayerCount(playerCount);
        LOG.debug("players: " + playerCount);
        int maxPlayers = result.get();
        server.setMaxPlayers(maxPlayers);
        LOG.debug("max players: " + maxPlayers);
        short version = result.get();
        LOG.debug("version: " + version);
        char dedicated = (char) result.get();
        LOG.debug("dedicated: " + dedicated);
        char operatingSystem = (char) result.get();
        LOG.debug("OS: " + operatingSystem);
        boolean passWord = result.get() == 0x1;
        LOG.debug("passWord: " + passWord);
        boolean isMod = result.get() == 0x1;
        LOG.debug("isMod: " + isMod);
        if (isMod) {
            getDetailsModGoldSource(result);
        }
        boolean secure = result.get() == 0x1;
        LOG.debug("secure: " + secure);
        short nbBots = result.get();
        LOG.debug("nbBots: " + nbBots);

        int remaining = result.remaining();
        if (remaining > 0) {
            LOG.warn("remaining bytes: " + remaining);
            byte[] remain = new byte[remaining];
            System.arraycopy(result.array(), result.position(), remain, 0, remaining);
            LOG.warn("remaining text: " + new String(remain));
        }
    }

    /**
     * @param result
     */
    private void getDetailsModGoldSource(ByteBuffer result) {
        String urlInfo = getNextString(result);
        LOG.debug("urlInfo? " + urlInfo);

        String urlDl = getNextString(result);
        LOG.debug("urlDl? " + urlDl);

        /* byte nul */
        result.get();
        int modVersion = result.getInt();
        LOG.debug("modVersion? " + Integer.toString(modVersion));
        int modSize = result.getInt();
        LOG.debug("modSize: " + modSize);
        boolean svOnly = result.get() == 0x1;
        LOG.debug("svOnly? " + svOnly);
        boolean clDll = result.get() == 0x1;
        LOG.debug("clDll? " + clDll);

    }

    /**
     * @param result
     */
    private void getDetailsSource(ByteBuffer result) {
        short version = result.get();
        LOG.debug("protocol: " + version);
        server.setName(getNextString(result));
        LOG.debug("name: " + server.getName());
        server.setMap(getNextString(result));
        LOG.debug("map: " + server.getMap());
        String gameDir = getNextString(result);
        LOG.debug("gameDir: " + gameDir);
        String gameDesc = getNextString(result);
        LOG.debug("gameDesc: " + gameDesc);
        short appId = result.getShort();
        LOG.debug("appId: " + appId);
        int playerCount = result.get();
        server.setPlayerCount(playerCount);
        LOG.debug("players: " + playerCount);
        int maxPlayers = result.get();
        server.setMaxPlayers(maxPlayers);
        LOG.debug("max players: " + maxPlayers);
        short nbBots = result.get();
        LOG.debug("nbBots: " + nbBots);
        char dedicated = (char) result.get();
        LOG.debug("dedicated: " + dedicated);
        char operatingSystem = (char) result.get();
        LOG.debug("OS: " + operatingSystem);
        boolean passWord = result.get() == 0x1;
        LOG.debug("password? " + passWord);
        boolean secure = result.get() == 0x1;
        LOG.debug("secure? " + secure);
        String gameVersion = getNextString(result);
        LOG.debug("version: " + gameVersion);
    }
}
