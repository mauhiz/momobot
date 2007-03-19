package momobot.cs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import momobot.Db;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.net.DatagramSocketClient;
import org.apache.commons.net.DefaultDatagramSocketFactory;
import org.apache.commons.net.io.Util;

import utils.Utils;

/**
 * @author viper
 */
class ValveUdpClient extends DatagramSocketClient {
    /**
     * requete!
     */
    private static final String    A2S_INFO   = "Source Engine Query";

    /**
     * challenger!
     */
    private static final String    CHALLENGE  = "challenge rcon";

    /**
     * moins 1.
     */
    static final String            MOINS_UN   = new String(ByteBuffer.allocate(
                                                      Integer.SIZE / Byte.SIZE)
                                                      .putInt(-1).array());

    /**
     * mon challenge.
     */
    private String                 challenge  = "";

    /**
     * Mon rcon.
     */
    private String                 rcon       = "";

    /**
     * le tampon sur lequel je vais travailler.
     */
    private final byte[]           receiveBuf = new byte[Util.DEFAULT_COPY_BUFFER_SIZE];

    /**
     * le tampon d'envoi.
     */
    private byte[]                 sendBuf;

    /**
     * Serveur associé.
     */
    private Server                 server     = null;

    /**
     * ah, mon écouteur préféré.
     */
    private ValveUpdClientListener vucl       = null;

    /**
     * @param server1
     *            le serveur
     */
    public ValveUdpClient(final Server server1) {
        this.server = server1;
    }

    /**
     * @throws IOException
     *             en cas de pépin!
     */
    public void getChallenge() throws IOException {
        this.challenge = StringUtils.substringAfter(sendValveCmd(CHALLENGE),
                "challenge rcon ");
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
     * @return the receiveBuf
     */
    byte[] getReceiveBuf() {
        return this.receiveBuf;
    }

    /**
     * @return la socket
     */
    protected DatagramSocket getSocket() {
        return this._socket_;
    }

    /**
     * @throws Exception
     *             si j'ai pas de rcon.
     */
    public void initLogThread() throws Exception {
        if (StringUtils.isEmpty(this.rcon)) {
            throw new Exception("pas de rcon défini");
        }
        this.vucl = new ValveUpdClientListener(this);
        this.vucl.execute();
    }

    /**
     * @param string
     *            la ligne à étudier
     * @throws Exception
     *             en cas de problème rcon
     */
    void processLine(final String string) throws Exception {
        String s = string.substring(Integer.SIZE / Byte.SIZE);
        s = StringUtils.trim(s);
        if (s.startsWith("log ")) {
            processLog(StringUtils.substringAfter(s, ": "));
            return;
        }
        if (s.equals("1Bad rcon_password.")) {
            Utils.logError(getClass(), new Exception("mauvais rcon"));
            unsetRcon();
            return;
        }
        if (s.equals("9Bad challenge.")) {
            Utils.logError(getClass(), new Exception("mauvais challenge"));
            this.challenge = "";
            return;
        }
        s = s.substring(1);
        // reponse au status
        if (s.startsWith("hostname")) {
            processStatus(s);
            return;
        }
        // cvar quelconque. Forme : "sv_restart" "1"
        if (s.startsWith("\"")) {
            final String[] pair = StringUtils.split(s, '"');
            Utils.log(getClass(), "CVAR : " + pair[0] + " = " + pair[2]);
        }
    }

    /**
     * @param s
     *            le log à étudier
     */
    private void processLog(final String s) {
        Utils.log(getClass(), s);
        if (s.charAt(0) != '"') {
            return;
        }
        String temp = StringUtils.removeStart(s, "\"");
        final String playerName = StringUtils.substringBefore(temp, "<");
        temp = StringUtils.substringAfter(temp, "<");
        String steamid = StringUtils.substringAfter(temp, "<");
        steamid = StringUtils.substringAfter(temp, "<");
        steamid = steamid.substring(0, steamid.indexOf('>'));
        String action = temp.substring(temp.indexOf('"') + 2);
        if (action.startsWith("say_team") && Db.isMaster(steamid)) {
            action = action.substring(10, action.lastIndexOf('"'));
            Utils.log(getClass(), playerName + " (" + steamid + ") orders me :"
                    + action);
            if (action.equals("!rs")) {
                this.server.svRestart(1);
                return;
            }
            if (action.equals("!3rs")) {
                this.server.svRestart(1);
                this.server.svRestart(1);
                this.server.svRestart(3);
                return;
            }
            final String cmdmap = "!map ";
            if (action.startsWith(cmdmap)) {
                action = action.substring(cmdmap.length());
                this.server.changelevel(action);
                return;
            }
        }
    }

    /**
     * @param status
     *            le message de status
     */
    private void processStatus(final String status) {
        Utils.log(getClass(), "Status");
        if (status.length() < 25) {
            Utils.logError(getClass(),
                    new Exception("erreur status trop court"));
            return;
        }
        final StrTokenizer lignes = new StrTokenizer(status);
        // 1 - hostname
        // 1hostname: Counter-Strike dedicated server
        this.server.setName(StringUtils.substringAfter(lignes.nextToken(), ":")
                .trim());
        // 2 - version (osef)
        // version : 47/1.1.2.5/2.0.0.0 2834 insecure
        lignes.nextToken();
        // 3 - adresse (on l'a déjà...)
        // tcp/ip : 192.168.1.2:27015
        lignes.nextToken();
        // 4 - map
        // map : de_airstrip at: 0 x, 0 y, 0 z
        final String map = StringUtils.substringAfter(lignes.nextToken(), ":")
                .trim();
        this.server.setMap(StringUtils.substringBefore(map, " "));
        // 5 - player count
        // players : 0 active (24 max)
        String players = StringUtils.substringAfter(lignes.nextToken(), ":")
                .trim();
        this.server.setPlayerCount(Short.parseShort(StringUtils
                .substringBefore(players, " ")));
        players = StringUtils.substringAfter(players, "(");
        this.server.setMaxPlayers(Short.parseShort(StringUtils.substringBefore(
                players, " ")));
        // 7 - ligne inutile
        // # name userid uniqueid frag time ping loss adr
        lignes.nextToken();
        while (lignes.hasNext()) {
            String line = lignes.nextToken();
            if (!line.startsWith("#")) {
                break;
            }
            // TODO : ajouter le traitement des joueurs.
            line = line.substring(2);
            final StringTokenizer st3 = new StringTokenizer(line);
            st3.nextToken(); // numero de joueur
            String playernick = st3.nextToken(); // nom du joueur
            playernick = playernick.substring(1, playernick.length() - 1);
            // virer les guillemets
            st3.nextToken(); // numero de joueur (bis)
            final String steamid = st3.nextToken(); // steam_id
            final int frags = Integer.parseInt(st3.nextToken());
            // le reste osef pour l'instant
            this.server.addPlayer(steamid, playernick, frags);
        }
    }

    /**
     * @param cmd
     *            la commande rcon
     */
    void rconCmd(final String cmd) {
        // antiban
        if (this.rcon.equals("")) {
            Utils.logError(getClass(), new Exception("Pas de rcon défini"));
            return;
        }
        if (this.challenge.equals("")) {
            Utils
                    .logError(getClass(), new Exception(
                            "Pas de challenge défini"));
            return;
        }
        if (this._socket_ == null) {
            try {
                this._socket_ = new DefaultDatagramSocketFactory()
                        .createDatagramSocket();
            } catch (final SocketException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            this.sendBuf = (MOINS_UN + "rcon " + this.challenge + " \""
                    + this.rcon + "\" " + cmd).getBytes();
            final DatagramPacket sendPacket = new DatagramPacket(this.sendBuf,
                    this.sendBuf.length, this.server.getIpay());
            this._socket_.send(sendPacket);
        } catch (final IOException e) {
            Utils.logError(getClass(), e);
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
        if (this._socket_ == null) {
            this._socket_ = new DefaultDatagramSocketFactory()
                    .createDatagramSocket();
        }
        this.sendBuf = (MOINS_UN + cmd + '\n').getBytes();

        final DatagramPacket sendPacket = new DatagramPacket(this.sendBuf,
                this.sendBuf.length, this.server.getIpay());
        final DatagramPacket recPacket = new DatagramPacket(this.receiveBuf,
                this.receiveBuf.length);

        this._socket_.send(sendPacket);
        this._socket_.receive(recPacket);

        return new String(recPacket.getData());
    }

    /**
     * @param rcon1
     *            le rcon
     */
    void setRcon(final String rcon1) {
        if (rcon1.equals("")) {
            return;
        }
        this.rcon = rcon1;
        if (!this.vucl.isRunning()) {
            this.vucl.execute();
        }
    }

    /**
     * efface le rcon.
     */
    private void unsetRcon() {
        this.rcon = "";
        this.vucl.setRunning(false);
    }
}
