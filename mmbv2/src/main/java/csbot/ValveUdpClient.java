package csbot;

import static org.apache.commons.lang.StringUtils.substringAfter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import momobot.SqlUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
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
     * requete!
     */
    private static final String    A2S_INFO         = "Source Engine Query";
    /**
     * challenger!
     */
    private static final String    CHALLENGE        = "challenge rcon";
    /**
     * 
     */
    private static final String    CMD_MAP          = "!map ";
    /**
     * logger.
     */
    private static final Logger    LOG              = Logger.getLogger(ValveUdpClient.class);
    /**
     * moins 1.
     */
    protected static final String  MOINS_UN         =
                                                            new String(ByteBuffer.allocate(Integer.SIZE / Byte.SIZE)
                                                                    .putInt(-1).array());
    /**
     * 
     */
    private static final long      serialVersionUID = 1;
    /**
     * mon challenge.
     */
    private String                 challenge;
    /**
     * Mon rcon.
     */
    private String                 rcon;
    /**
     * le tampon d'envoi.
     */
    private ByteBuffer             sendBuf;
    /**
     * Serveur associé.
     */
    private final Server           server;
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
        this.server = server1;
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
        this.challenge = substringAfter(sendValveCmd(CHALLENGE), "challenge rcon ");
    }

    /**
     * @return les infos
     * @throws IOException
     *             houla, ça va mal
     */
    public String getInfo() throws IOException {
        return sendValveCmd(MOINS_UN + 'T' + A2S_INFO);
    }

    /**
     * @return la socket
     */
    protected DatagramSocket getSocket() {
        return this._socket_;
    }

    /**
     */
    public void initLogThread() {
        if (StringUtils.isEmpty(this.rcon)) {
            throw new UnsupportedOperationException("pas de rcon défini");
        }
        this.vucl = new ValveUdpClientListener(this);
        this.vucl.execute();
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
            this.challenge = null;
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
                this.server.svRestart(1);
            } else if ("!3rs".equals(action)) {
                this.server.svRestart(1);
                this.server.svRestart(1);
                this.server.svRestart(3);
            } else {
                if (action.startsWith(CMD_MAP)) {
                    action = action.substring(CMD_MAP.length());
                    this.server.changelevel(action);
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
        this.server.setName(substringAfter(lignes.nextToken(), ":").trim());
        // 2 - version (osef)
        // version : 47/1.1.2.5/2.0.0.0 2834 insecure
        lignes.nextToken();
        // 3 - adresse (on l'a déjà...)
        // tcp/ip : 192.168.1.2:27015
        lignes.nextToken();
        // 4 - map
        // map : de_airstrip at: 0 x, 0 y, 0 z
        final String map = substringAfter(lignes.nextToken(), ":").trim();
        this.server.setMap(StringUtils.substringBefore(map, " "));
        // 5 - player count
        // players : 0 active (24 max)
        String players = substringAfter(lignes.nextToken(), ":").trim();
        this.server.setPlayerCount(Byte.parseByte(StringUtils.substringBefore(players, " ")));
        players = substringAfter(players, "(");
        this.server.setMaxPlayers(Byte.parseByte(StringUtils.substringBefore(players, " ")));
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
        if (StringUtils.isEmpty(this.rcon)) {
            LOG.warn("Pas de rcon défini");
        } else if (StringUtils.isEmpty(this.challenge)) {
            LOG.warn("Pas de challenge défini");
        } else if (null == this._socket_) {
            try {
                this._socket_ = new DefaultDatagramSocketFactory().createDatagramSocket();
            } catch (final SocketException se) {
                LOG.error(se, se);
                return;
            }
        } else {
            try {
                final StrBuilder builder = new StrBuilder(MOINS_UN);
                builder.append("rcon ").append(this.challenge).append(' ').append('"').append(this.rcon).append('"')
                        .append(' ').append(cmd);
                this.sendBuf = ByteBuffer.wrap(builder.toString().getBytes());
                final DatagramPacket sendPacket =
                        new DatagramPacket(this.sendBuf.array(), this.sendBuf.capacity(), this.server.getIpay());
                this._socket_.send(sendPacket);
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
    private String sendValveCmd(final String cmd) throws IOException {
        if (null == this._socket_) {
            this._socket_ = new DefaultDatagramSocketFactory().createDatagramSocket();
        }
        this.sendBuf = ByteBuffer.wrap((MOINS_UN + cmd + '\n').getBytes());
        final DatagramPacket sendPacket =
                new DatagramPacket(this.sendBuf.array(), this.sendBuf.capacity(), this.server.getIpay());
        final DatagramPacket recPacket = createDatagramPacket();
        this._socket_.send(sendPacket);
        this._socket_.receive(recPacket);
        return new String(recPacket.getData());
    }

    /**
     * @param rcon1
     *            le rcon
     */
    protected void setRcon(final String rcon1) {
        if (StringUtils.isEmpty(rcon1)) {
            return;
        }
        this.rcon = rcon1;
        if (!this.vucl.isRunning()) {
            this.vucl.execute();
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
        this.server.addPlayer(steamid, playernick, frags);
    }

    /**
     * efface le rcon.
     */
    private void unsetRcon() {
        this.rcon = null;
        this.vucl.setRunning(false);
    }
}
