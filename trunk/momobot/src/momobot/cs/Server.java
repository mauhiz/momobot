package momobot.cs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 */
public class Server {
    /**
     * indique que le serveur est dédié.
     */
    static final char           DEDICATED  = 'd';
    /**
     * moteur de HL 1.
     */
    static final char           GOLDSOURCE = 'm';
    /**
     * indique que le serveur est un hltv.
     */
    static final char           HLTV       = 'p';
    /**
     * indique que le serveur est sous linux.
     */
    static final char           LINUX      = 'l';
    /**
     * indique que le serveur est listen.
     */
    static final char           LISTEN     = 'l';
    /**
     * logger.
     */
    private static final Logger LOG        = Logger.getLogger(Server.class);
    /**
     * moteur source.
     */
    static final char           SOURCE     = 'I';
    /**
     * indique que le serveur est sous windows.
     */
    static final char           WINDOWS    = 'w';

    /**
     * @param bb
     *            le bytebuffer
     * @return la chaine de caracteres
     */
    static String getNextString(final ByteBuffer bb) {
        char temp = 0x1;
        final StringBuffer retour = new StringBuffer();
        while (true) {
            if (!bb.hasRemaining()) {
                break;
            }
            temp = (char) bb.get();
            if (temp == '\0') {
                break;
            }
            retour.append(temp);
        }
        return retour.toString();
    }
    /**
     * Ip et port du serveur.
     */
    private InetSocketAddress            ipay        = null;
    /**
     * map actuelle.
     */
    private String                       map         = "";
    /**
     * capacité du serveur en nombre de joueurs.
     */
    private short                        maxPlayers  = 0;
    /**
     * hostname du serveur.
     */
    private String                       name        = "";
    /**
     * mot de passe.
     */
    private String                       pass        = "";
    /**
     * Temps de latence en ms.
     */
    private long                         ping        = -1;
    /**
     * Nombre de joueurs présents sur le serveur. Utilisé avant d'avoir tous les
     * joueurs dans {@link #players}
     */
    private short                        playerCount = 0;
    /**
     * Les joueurs présents. Clé = steamId, Valeur = objet Player associé.
     */
    private final Map < String, Player > players     = new TreeMap < String, Player >();
    /**
     * Le client Valve UDP.
     */
    private ValveUdpClient               vuc         = null;

    /**
     * Constructeur.
     * @param ipay1
     *            l'IP et port du serveur.
     */
    public Server(final InetSocketAddress ipay1) {
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
    final void addPlayer(final String steamid, final String nick,
            final int frags) {
        final Player p = new Player(steamid);
        p.setName(nick);
        p.setFrags(frags);
        this.players.put(steamid, p);
    }

    /**
     * @return un affichage
     */
    public final String affiche() {
        getDetails();
        return getIp() + ":" + getPort() + " | " + this.name + " | "
                + this.ping + "ms";
        // TODO : ajouter playerCount/maxPlayers
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
     * affiche les détails du serveur. Voir <a
     * href="http://developer.valvesoftware.com/wiki/Server_Queries#A2S_INFO">
     * VDW </a>
     */
    public final void getDetails() {
        final long start = System.nanoTime();
        try {
            final ByteBuffer result = ByteBuffer.wrap(this.vuc.getInfo()
                    .getBytes());
            final long end = System.nanoTime();
            this.ping = TimeUnit.MILLISECONDS.convert(end - start,
                    TimeUnit.NANOSECONDS);
            result.position(Integer.SIZE / Byte.SIZE);
            final char type = (char) result.get();
            if (LOG.isDebugEnabled()) {
                LOG.debug(Character.toString(type));
            }
            if (type == GOLDSOURCE) {
                final String ip = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(ip);
                }
                this.name = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(this.name);
                }
                this.map = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(this.map);
                }
                final String gameDir = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(gameDir);
                }
                final String gameDesc = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(gameDesc);
                }
                this.playerCount = result.get();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Integer.toString(this.playerCount));
                }
                this.maxPlayers = result.get();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Integer.toString(this.maxPlayers));
                }
                final short version = result.get();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Integer.toString(version));
                }
                final char dedicated = (char) result.get();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Integer.toString(dedicated));
                }
                final char os = (char) result.get();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Character.toString(os));
                }
                final boolean pw = result.get() == 0x1;
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Boolean.toString(pw));
                }
                final boolean isMod = result.get() == 0x1;
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Boolean.toString(isMod));
                }
                if (isMod) {
                    final String urlInfo = getNextString(result);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(urlInfo);
                    }
                    final String urlDl = getNextString(result);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(urlDl);
                    }
                    result.get(); /* byte nul */
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
                final boolean secure = result.get() == 0x1;
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Boolean.toString(secure));
                }
                final short nbBots = result.get();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Integer.toString(nbBots));
                }
            } else if (type == SOURCE) {
                final short version = result.get();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Integer.toString(version));
                }
                this.name = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(this.name);
                }
                this.map = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(this.map);
                }
                final String gameDir = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(gameDir);
                }
                final String gameDesc = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(gameDesc);
                }
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
                final char os = (char) result.get();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Character.toString(os));
                }
                final boolean pw = result.get() == 0x1;
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Boolean.toString(pw));
                }
                final boolean secure = result.get() == 0x1;
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Boolean.toString(secure));
                }
                final String gameVersion = getNextString(result);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(gameVersion);
                }
            } else {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Serveur non Goldsource (type = " + type + ")");
                }
            }
        } catch (final IOException ioe) {
            if (LOG.isErrorEnabled()) {
                LOG.error(ioe, ioe);
            }
            return;
        }
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
        return Short.toString(this.maxPlayers);
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
        final Player p = this.players.get(steamid);
        if (p == null) {
            return "";
        }
        return p.getName();
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
        this.vuc.rconCmd('"' + cvar + "\" \"" + value + '"');
    }

    /**
     * @param ipay1
     *            the ipay to set
     */
    final void setIpay(final InetSocketAddress ipay1) {
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
     * @param i
     *            le nombre maxi de players
     */
    public final void setMaxPlayers(final short i) {
        this.maxPlayers = i;
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
     * @param i
     *            le nombre de joueurs courants
     */
    public final void setPlayerCount(final short i) {
        this.playerCount = i;
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
     * @param pwd
     *            le password
     */
    public final void svPassword(final String pwd) {
        setCvar("sv_password", pwd);
    }

    /**
     * @param t
     *            le délai avant le restart
     */
    public final void svRestart(final int t) {
        setCvar("sv_restart", Integer.toString(t));
    }
}
