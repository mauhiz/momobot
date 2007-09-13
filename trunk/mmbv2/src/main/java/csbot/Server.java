package csbot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * une sorte de gros bean. Les joueurs présents: Clé = steamId, Valeur = objet Player associé.
 * 
 * @author mauhiz
 */
public class Server {
    /**
     * indique que le serveur est dédié.
     */
    public static final char    DEDICATED        = 'd';
    /**
     * moteur de HL 1.
     */
    public static final char    GOLDSOURCE       = 'm';
    /**
     * indique que le serveur est un hltv.
     */
    public static final char    HLTV             = 'p';
    /**
     * indique que le serveur est sous linux.
     */
    public static final char    LINUX            = 'l';
    /**
     * indique que le serveur est listen.
     */
    public static final char    LISTEN           = 'l';
    /**
     * logger.
     */
    private static final Logger LOG              = Logger.getLogger(Server.class);
    /**
     * 
     */
    private static final long   serialVersionUID = 1;
    /**
     * moteur source.
     */
    public static final char    SOURCE           = 'I';
    /**
     * indique que le serveur est sous windows.
     */
    public static final char    WINDOWS          = 'w';

    /**
     * @param buffer
     *            le bytebuffer
     * @return la chaine de caracteres
     */
    private static String getNextString(final ByteBuffer buffer) {
        char temp;
        final StrBuilder retour = new StrBuilder();
        while (buffer.hasRemaining()) {
            /* et non pas buffer.getChar() */
            temp = (char) buffer.get();
            if ('\0' == temp) {
                break;
            }
            retour.append(temp);
        }
        return retour.toString();
    }
    /**
     * Ip et port du serveur.
     */
    private InetSocketAddress            ipay;
    /**
     * map actuelle.
     */
    private String                       map;
    /**
     * capacité du serveur en nombre de joueurs.
     */
    private byte                         maxPlayers;
    /**
     * hostname du serveur.
     */
    private String                       name;
    /**
     * mot de passe.
     */
    private String                       pass;
    /**
     * Temps de latence en ms.
     */
    private long                         ping    = -1;
    /**
     * Nombre de joueurs présents sur le serveur. Utilisé avant d'avoir tous les joueurs.
     */
    private byte                         playerCount;
    /**
     * 
     */
    private final Map < String, Player > players = new TreeMap < String, Player >();
    /**
     * Le client Valve UDP.
     */
    private final ValveUdpClient         vuc;

    /**
     * Constructeur.
     * 
     * @param ipay1
     *            l'IP et port du serveur.
     */
    public Server(final InetSocketAddress ipay1) {
        super();
        this.ipay = ipay1;
        this.vuc = new ValveUdpClient(this);
    }

    /**
     * @param steamid
     *            le steamId
     * @param nick
     *            le nick
     * @param frags
     *            les frags
     */
    protected final void addPlayer(final String steamid, final String nick, final int frags) {
        final Player player = new Player(steamid);
        player.setName(nick);
        player.setFrags(frags);
        this.players.put(steamid, player);
    }

    /**
     * @param cvar
     *            la cvar à demander
     */
    public final void askCvar(final String cvar) {
        this.vuc.rconCmd(cvar);
    }

    /**
     * @param newmap
     *            la nouvelle map
     * @return si on a effectivement change de map
     */
    public final boolean changelevel(final String newmap) {
        if (newmap.equals(this.map)) {
            return false;
        }
        this.vuc.rconCmd("changelevel " + newmap);
        return true;
    }

    /**
     * affiche les détails du serveur. Voir <a href="http://developer.valvesoftware.com/wiki/Server_Queries#A2S_INFO">
     * VDW </a>
     */
    public void getDetails() {
        final long start = System.nanoTime();
        ByteBuffer result = null;
        try {
            result = ByteBuffer.wrap(this.vuc.getInfo().getBytes());
        } catch (final IOException ioe) {
            LOG.error(ioe, ioe);
        }
        if (null == result) {
            return;
        }
        final long end = System.nanoTime();
        this.ping = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
        result.position(Integer.SIZE / Byte.SIZE);
        final char type = (char) result.get();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Character.toString(type));
        }
        if (type == GOLDSOURCE) {
            getDetailsGoldSource(result);
        } else if (type == SOURCE) {
            getDetailsSource(result);
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Serveur inconnu (type = " + type + ")");
            }
        }
    }

    /**
     * @param result
     */
    private void getDetailsGoldSource(final ByteBuffer result) {
        final String adresse = getNextString(result);
        LOG.debug(adresse);
        this.name = getNextString(result);
        LOG.debug(this.name);
        this.map = getNextString(result);
        LOG.debug(this.map);
        final String gameDir = getNextString(result);
        LOG.debug(gameDir);
        final String gameDesc = getNextString(result);
        LOG.debug(gameDesc);
        this.playerCount = result.get();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Integer.toString(this.playerCount));
        }
        this.maxPlayers = result.get();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Integer.toString(this.maxPlayers));
        }
        final short version = result.get();
        LOG.debug(Integer.toString(version));
        final char dedicated = (char) result.get();
        LOG.debug(Integer.toString(dedicated));
        final char operatingSystem = (char) result.get();
        LOG.debug(Character.toString(operatingSystem));
        final boolean passWord = result.get() == 0x1;
        LOG.debug(Boolean.toString(passWord));
        final boolean isMod = result.get() == 0x1;
        LOG.debug(Boolean.toString(isMod));
        if (isMod) {
            getDetailsModGoldSource(result);
        }
        final boolean secure = result.get() == 0x1;
        if (LOG.isDebugEnabled()) {
            LOG.debug(Boolean.toString(secure));
        }
        final short nbBots = result.get();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Integer.toString(nbBots));
        }
    }

    /**
     * @param result
     */
    private void getDetailsModGoldSource(final ByteBuffer result) {
        final String urlInfo = getNextString(result);
        if (LOG.isDebugEnabled()) {
            LOG.debug(urlInfo);
        }
        final String urlDl = getNextString(result);
        if (LOG.isDebugEnabled()) {
            LOG.debug(urlDl);
        }
        /* byte nul */
        result.get();
        final int modVersion = result.getInt();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Integer.toString(modVersion));
        }
        final int modSize = result.getInt();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Integer.toString(modSize));
        }
        final boolean svOnly = result.get() == 0x1;
        if (LOG.isDebugEnabled()) {
            LOG.debug(Boolean.toString(svOnly));
        }
        final boolean clDll = result.get() == 0x1;
        if (LOG.isDebugEnabled()) {
            LOG.debug(Boolean.toString(clDll));
        }
    }

    /**
     * @param result
     */
    private void getDetailsSource(final ByteBuffer result) {
        final short version = result.get();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Integer.toString(version));
        }
        this.name = getNextString(result);
        LOG.debug(this.name);
        this.map = getNextString(result);
        LOG.debug(this.map);
        final String gameDir = getNextString(result);
        LOG.debug(gameDir);
        final String gameDesc = getNextString(result);
        LOG.debug(gameDesc);
        final short appId = result.getShort();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Integer.toString(appId));
        }
        this.playerCount = result.get();
        this.maxPlayers = result.get();
        final short nbBots = result.get();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Integer.toString(nbBots));
        }
        final char dedicated = (char) result.get();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Character.toString(dedicated));
        }
        final char operatingSystem = (char) result.get();
        if (LOG.isDebugEnabled()) {
            LOG.debug(Character.toString(operatingSystem));
        }
        final boolean passWord = result.get() == 0x1;
        if (LOG.isDebugEnabled()) {
            LOG.debug(Boolean.toString(passWord));
        }
        final boolean secure = result.get() == 0x1;
        if (LOG.isDebugEnabled()) {
            LOG.debug(Boolean.toString(secure));
        }
        final String gameVersion = getNextString(result);
        LOG.debug(gameVersion);
    }

    /**
     * @return ip
     */
    public final String getIp() {
        return this.ipay.getAddress().getHostAddress();
    }

    /**
     * @return ipay
     */
    public final InetSocketAddress getIpay() {
        return this.ipay;
    }

    /**
     * @return Returns the map.
     */
    public final String getMap() {
        return this.map;
    }

    /**
     * @return maxPlayers
     */
    public final String getMaxPlayers() {
        return Byte.toString(this.maxPlayers);
    }

    /**
     * @return Returns the name.
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return Returns the pass.
     */
    public final String getPass() {
        return this.pass;
    }

    /**
     * @return Returns the ping.
     */
    public final long getPing() {
        return this.ping;
    }

    /**
     * @return playerCount
     */
    public final String getPlayerCount() {
        return Integer.toString(this.playerCount);
    }

    /**
     * @param steamid
     *            le SteamId
     * @return le nick du player
     */
    public final String getPlayerNick(final String steamid) {
        final Player player = this.players.get(steamid);
        if (null != player) {
            return player.getName();
        }
        return null;
    }

    /**
     * @return port
     */
    public final int getPort() {
        return this.ipay.getPort();
    }

    /**
     * @param cvar
     *            la cvar
     * @param value
     *            la valeur
     */
    public final void setCvar(final String cvar, final String value) {
        this.vuc.rconCmd(new StrBuilder().append('"').append(cvar).append('"').append(' ').append('"').append(value)
                .append('"').toString());
    }

    /**
     * @param ipay1
     *            the ipay to set
     */
    protected final void setIpay(final InetSocketAddress ipay1) {
        this.ipay = ipay1;
    }

    /**
     * @param map1
     *            The map to set.
     */
    public final void setMap(final String map1) {
        this.map = map1;
    }

    /**
     * @param maxPlayers1
     *            le nombre maxi de players
     */
    public final void setMaxPlayers(final byte maxPlayers1) {
        this.maxPlayers = maxPlayers1;
    }

    /**
     * @param name1
     *            The name to set.
     */
    public final void setName(final String name1) {
        this.name = name1;
    }

    /**
     * @param pass1
     *            The pass to set.
     */
    public final void setPass(final String pass1) {
        this.pass = pass1;
    }

    /**
     * @param ping1
     *            The ping to set.
     */
    public final void setPing(final long ping1) {
        this.ping = ping1;
    }

    /**
     * @param playerCount1
     *            le nombre de joueurs courants
     */
    public final void setPlayerCount(final byte playerCount1) {
        this.playerCount = playerCount1;
    }

    /**
     * Affiche le status.
     */
    public final void status() {
        this.vuc.rconCmd("status");
    }

    /**
     * récupère le password.
     */
    public final void svPassword() {
        askCvar("sv_password");
    }

    /**
     * Fixe un password.
     * 
     * @param pwd
     *            le password
     */
    public final void svPassword(final String pwd) {
        setCvar("sv_password", pwd);
    }

    /**
     * @param delay
     *            le délai avant le restart
     */
    public final void svRestart(final int delay) {
        setCvar("sv_restart", Integer.toString(delay));
    }

    /**
     * @return un affichage
     * @see Object#toString()
     */
    @Override
    public String toString() {
        getDetails();
        return getIp() + ":" + getPort() + " | " + this.name + " | " + this.ping + "ms (" + this.playerCount + "/"
                + this.maxPlayers + ")";
        /* TODO : ajouter playerCount/maxPlayers */
    }
}
