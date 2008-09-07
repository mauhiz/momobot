package net.mauhiz.irc.bot.triggers.cs;

import static org.apache.commons.lang.StringUtils.substringAfter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import net.mauhiz.irc.SqlUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.net.DatagramSocketClient;
import org.apache.commons.net.DefaultDatagramSocketFactory;
import org.apache.commons.net.io.Util;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
class ValveUdpClient extends DatagramSocketClient {
    /**
     * char info
     */
    private static final char A2S_INFO = 'T';
    /**
     * char info
     */
    private static final char A2S_PLAYERS = 'U';
    /**
     * char info
     */
    private static final char A2S_RULES = 'V';
    /**
     * autre char...
     */
    private static final char A2S_SERVERQUERY_GETCHALLENGE = 'W';
    /**
     * challenger!
     */
    private static final String CHALLENGE = "challenge rcon";
    
    /**
     * 
     */
    private static final String CMD_MAP = "!map ";
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(ValveUdpClient.class);
    /**
     * moins 1.
     */
    protected static final String MOINS_UN = new String(ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(-1)
            .array());
    /**
     * requete!
     */
    private static final String QUERY = "Source Engine Query";
    /**
     * 
     */
    private static final long serialVersionUID = 1;
    /**
     * mon challenge.
     */
    private String challenge;
    /**
     * Mon rcon.
     */
    private String rcon;
    /**
     * le tampon d'envoi.
     */
    private ByteBuffer sendBuf;
    /**
     * Serveur associé.
     */
    private final Server server;
    /**
     * ah, mon écouteur préféré.
     */
    private ValveUdpClientListener vucl;
    
    /**
     * @param server1
     *            le serveur
     */
    public ValveUdpClient(final Server server1) {
        super();
        server = server1;
    }
    
    /**
     * @return un nouveau paquet UDP
     */
    protected DatagramPacket createDatagramPacket() {
        final ByteBuffer receiveBuf = ByteBuffer.allocate(Util.DEFAULT_COPY_BUFFER_SIZE);
        return new DatagramPacket(receiveBuf.array(), receiveBuf.capacity());
    }
    
    /**
     * @throws IOException
     *             en cas de pépin!
     */
    public void getChallenge() throws IOException {
        challenge = substringAfter(new String(sendValveCmd(CHALLENGE)), CHALLENGE + " ");
    }
    
    /**
     * @return les infos
     * @throws IOException
     *             houla, ça va mal
     */
    public byte[] getInfo() throws IOException {
        return sendValveCmd(A2S_INFO + QUERY);
    }
    
    /**
     * @return les infos
     * @throws IOException
     *             houla, ça va mal
     */
    public byte[] getPlayers() throws IOException {
        return sendValveCmd(A2S_PLAYERS + QUERY);
    }
    
    /**
     * @return les infos
     * @throws IOException
     *             houla, ça va mal
     */
    public byte[] getRules() throws IOException {
        return sendValveCmd(A2S_RULES + QUERY);
    }
    
    /**
     * @return la socket
     */
    protected DatagramSocket getSocket() {
        return _socket_;
    }
    
    /**
     */
    public void initLogThread() {
        if (StringUtils.isEmpty(rcon)) {
            throw new UnsupportedOperationException("pas de rcon défini");
        }
        vucl = new ValveUdpClientListener(this);
        vucl.execute();
    }
    
    /**
     * @param string
     *            la ligne à étudier
     */
    protected void processLine(final String string) {
        String line = StringUtils.trim(string.substring(Integer.SIZE / Byte.SIZE));
        if (line.startsWith("log ")) {
            processLog(substringAfter(line, ": "));
        } else if ("1Bad rcon_password.".equals(line)) {
            LOG.warn("mauvais rcon");
            unsetRcon();
        } else if ("9Bad challenge.".equals(line)) {
            LOG.warn("mauvais challenge");
            challenge = null;
        } else {
            line = line.substring(1);
            /* réponse au status */
            if (line.startsWith("hostname")) {
                processStatus(line);
                return;
            }
            /* cvar quelconque. Forme : "sv_restart" "1" */
            if (line.charAt(0) == '"') {
                final String[] pair = StringUtils.split(line, '"');
                if (LOG.isDebugEnabled()) {
                    LOG.debug("CVAR : " + pair[0] + " = " + pair[2]);
                }
            }
        }
    }
    
    /**
     * @param log
     *            le log à étudier
     */
    private void processLog(final String log) {
        LOG.debug(log);
        if (log.charAt(0) != '"') {
            return;
        }
        String temp = StringUtils.stripStart(log, "\"");
        final String playerName = StringUtils.substringBefore(temp, "<");
        temp = substringAfter(temp, "<");
        String steamid = substringAfter(temp, "<");
        steamid = steamid.substring(0, steamid.indexOf('>'));
        String action = temp.substring(temp.indexOf('"') + 2);
        if (action.startsWith("say_team") && SqlUtils.isMaster(steamid)) {
            action = action.substring(10, action.lastIndexOf('"'));
            if (LOG.isInfoEnabled()) {
                LOG.info(playerName + " (" + steamid + ") orders me :" + action);
            }
            if ("!rs".equals(action)) {
                server.svRestart(1);
            } else if ("!3rs".equals(action)) {
                server.svRestart(1);
                server.svRestart(1);
                server.svRestart(3);
            } else {
                if (action.startsWith(CMD_MAP)) {
                    action = action.substring(CMD_MAP.length());
                    server.changelevel(action);
                }
            }
        }
    }
    
    /**
     * @param status
     *            le message de status
     */
    private void processStatus(final String status) {
        LOG.debug("Status");
        if (status.length() < 25) {
            throw new IllegalArgumentException("erreur status trop court");
        }
        final StrTokenizer lignes = new StrTokenizer(status);
        // 1 - hostname
        // 1hostname: Counter-Strike dedicated server
        server.setName(substringAfter(lignes.nextToken(), ":").trim());
        // 2 - version (osef)
        // version : 47/1.1.2.5/2.0.0.0 2834 insecure
        lignes.nextToken();
        // 3 - adresse (on l'a déjà...)
        // tcp/ip : 192.168.1.2:27015
        lignes.nextToken();
        // 4 - map
        // map : de_airstrip at: 0 x, 0 y, 0 z
        final String map = substringAfter(lignes.nextToken(), ":").trim();
        server.setMap(StringUtils.substringBefore(map, " "));
        // 5 - player count
        // players : 0 active (24 max)
        String players = substringAfter(lignes.nextToken(), ":").trim();
        server.setPlayerCount(Byte.parseByte(StringUtils.substringBefore(players, " ")));
        players = substringAfter(players, "(");
        server.setMaxPlayers(Byte.parseByte(StringUtils.substringBefore(players, " ")));
        // 7 - ligne inutile
        // # name userid uniqueid frag time ping loss adr
        lignes.nextToken();
        String line;
        while (lignes.hasNext()) {
            line = lignes.nextToken();
            if (line.charAt(0) != '#') {
                break;
            }
            traiteLigneJoueur(line.substring(2));
        }
    }
    
    /**
     * @param cmd
     *            la commande rcon
     */
    protected void rconCmd(final String cmd) {
        /* antiban */
        if (StringUtils.isEmpty(rcon)) {
            LOG.warn("Pas de rcon défini");
        } else if (StringUtils.isEmpty(challenge)) {
            LOG.warn("Pas de challenge défini");
        } else if (null == _socket_) {
            try {
                _socket_ = new DefaultDatagramSocketFactory().createDatagramSocket();
            } catch (final SocketException se) {
                LOG.error(se, se);
                return;
            }
        } else {
            try {
                final StringBuilder builder = new StringBuilder();
                builder.append(MOINS_UN).append("rcon ").append(challenge).append(' ').append('"').append(rcon).append(
                        '"').append(' ').append(cmd);
                sendBuf = ByteBuffer.wrap(builder.toString().getBytes());
                final DatagramPacket sendPacket = new DatagramPacket(sendBuf.array(), sendBuf.capacity(), server
                        .getIpay());
                _socket_.send(sendPacket);
            } catch (final IOException ioe) {
                LOG.error(ioe, ioe);
            }
        }
    }
    
    /**
     * @param cmd
     *            la commande
     * @return un résultat
     * @throws IOException
     *             en cas de prob rezo
     */
    private byte[] sendValveCmd(final String cmd) throws IOException {
        if (null == _socket_) {
            _socket_ = new DefaultDatagramSocketFactory().createDatagramSocket();
            _socket_.setSoTimeout(60);
        }
        sendBuf = ByteBuffer.wrap((MOINS_UN + cmd + Server.NUL).getBytes());
        LOG.debug("Sending : " + cmd);
        final DatagramPacket sendPacket = new DatagramPacket(sendBuf.array(), sendBuf.capacity(), server.getIpay());
        final DatagramPacket recPacket = createDatagramPacket();
        _socket_.send(sendPacket);
        _socket_.receive(recPacket);
        return recPacket.getData();
    }
    
    /**
     * @param rcon1
     *            le rcon
     */
    protected void setRcon(final String rcon1) {
        if (StringUtils.isEmpty(rcon1)) {
            return;
        }
        rcon = rcon1;
        if (!vucl.isRunning()) {
            vucl.execute();
        }
    }
    
    /**
     * @param line
     */
    private void traiteLigneJoueur(final String line) {
        final StrTokenizer st3 = new StrTokenizer(line);
        /* numero de joueur */
        st3.nextToken();
        /* nom du joueur : virer les guillemets */
        final String playernick = StringUtils.strip(st3.nextToken(), "\"");
        /* numero de joueur (bis) */
        st3.nextToken();
        /* steam_id */
        final String steamid = st3.nextToken();
        final int frags = Integer.parseInt(st3.nextToken());
        /* le reste osef pour l'instant */
        server.addPlayer(steamid, playernick, frags);
    }
    
    /**
     * efface le rcon.
     */
    private void unsetRcon() {
        rcon = null;
        vucl.setRunning(false);
    }
}
