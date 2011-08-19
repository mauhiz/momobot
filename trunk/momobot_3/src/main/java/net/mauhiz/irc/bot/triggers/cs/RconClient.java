package net.mauhiz.irc.bot.triggers.cs;

import static org.apache.commons.lang3.StringUtils.substringAfter;

import java.io.IOException;

import net.mauhiz.util.FileUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;

/**
 * @author mauhiz
 */
class RconClient extends ValveUdpClient implements IRconClient {

    /**
     * challenger!
     */
    private static final String CHALLENGE = "challenge rcon";

    /**
     * 
     */
    private static final String CMD_MAP = "!map ";

    /**
     * Mon rcon.
     */
    private String rcon;
    /**
     * mon challenge.
     */
    private String rconChallenge;

    /**
     * ah, mon ecouteur prefere.
     */
    private final IRconListener rl;

    /**
     * @param server1
     *            le serveur
     */
    public RconClient(IRconServer server1, String rcon) throws IOException {
        super(server1);
        this.rcon = rcon;
        rl = new RconListener(this);
    }

    /**
     * @param rcon1
     *            le rcon
     */
    public void changeRcon(String rcon1) {
        if (StringUtils.isEmpty(rcon1)) {
            return;
        }
        rcon = rcon1;
        if (!rl.isRunning()) {
            initLogThread();
        }
    }

    /**
     * @throws IOException
     *             en cas de pepin!
     */
    public void getRconChallenge() throws IOException {
        rconChallenge = substringAfter(new String(sendAndRcvValveCmd(CHALLENGE), FileUtil.ASCII), CHALLENGE + " ");
    }

    IRconServer getServer() {
        return (IRconServer) server;
    }

    /**
     */
    public void initLogThread() {
        assert StringUtils.isNotEmpty(rcon) : "pas de rcon defini";
        rl.setName("Valve Udp Listener on " + server.getIp());
        rl.tstart();
    }

    /**
     * @param string
     *            la ligne e etudier
     */
    public void processLine(String string) {
        String line = StringUtils.trim(string.substring(Integer.SIZE / Byte.SIZE));
        if (line.startsWith("log ")) {
            try {
                processLog(substringAfter(line, ": "));
            } catch (IOException ioe) {
                LOG.warn(ioe, ioe);
            }
        } else if ("1Bad rcon_password.".equals(line)) {
            LOG.warn("mauvais rcon");
            unsetRcon();
        } else if ("9Bad challenge.".equals(line)) {
            LOG.warn("mauvais challenge");
            rconChallenge = null;
        } else {
            line = line.substring(1);
            /* reponse au status */
            if (line.startsWith("hostname")) {
                processStatus(line);
                return;
            }
            /* cvar quelconque. Forme : "sv_restart" "1" */
            if (line.charAt(0) == '"') {
                String[] pair = StringUtils.split(line, '"');
                LOG.debug("CVAR : " + pair[0] + " = " + pair[2]);
            }
        }
    }

    /**
     * @param log
     *            le log e etudier
     */
    private void processLog(String log) throws IOException {
        LOG.debug(log);
        if (log.charAt(0) != '"') {
            return;
        }
        String temp = StringUtils.stripStart(log, "\"");
        String playerName = StringUtils.substringBefore(temp, "<");
        temp = substringAfter(temp, "<");
        String steamid = substringAfter(temp, "<");
        steamid = steamid.substring(0, steamid.indexOf('>'));
        String action = temp.substring(temp.indexOf('"') + 2);
        if (action.startsWith("say_team") && PlayerDB.isMaster(steamid)) {
            IRconServer rcs = getServer();
            action = action.substring(10, action.lastIndexOf('"'));
            LOG.info(playerName + " (" + steamid + ") orders me :" + action);
            if ("!rs".equals(action)) {
                rcs.svRestart(1);
            } else if ("!3rs".equals(action)) {
                rcs.svRestart(1);
                rcs.svRestart(1);
                rcs.svRestart(3);
            } else {
                if (action.startsWith(CMD_MAP)) {
                    action = action.substring(CMD_MAP.length());
                    rcs.changelevel(action);
                }
            }
        }
    }

    /**
     * @param status
     *            le message de status
     */
    private void processStatus(String status) {
        LOG.debug("Status: " + status);
        if (status.length() < 25) {
            throw new IllegalArgumentException("status trop court: " + status);
        }
        StrTokenizer lignes = new StrTokenizer(status);
        // 1 - hostname
        // 1hostname: Counter-Strike dedicated server
        server.setName(substringAfter(lignes.nextToken(), ":").trim());
        // 2 - version (osef)
        // version : 47/1.1.2.5/2.0.0.0 2834 insecure
        lignes.nextToken();
        // 3 - adresse (on l'a deja...)
        // tcp/ip : 192.168.1.2:27015
        lignes.nextToken();
        // 4 - map
        // map : de_airstrip at: 0 x, 0 y, 0 z
        String map = substringAfter(lignes.nextToken(), ":").trim();
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

        server.resetPlayers();
        while (lignes.hasNext()) {
            String line = lignes.nextToken();
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
    public void rconCmd(String cmd) throws IOException {
        /* antiban */
        if (StringUtils.isEmpty(rcon)) {
            LOG.warn("Pas de rcon defini");
            return;
        } else if (StringUtils.isEmpty(rconChallenge)) {
            LOG.warn("Pas de challenge defini");
            return;
        }
        sendValveCmd("rcon " + rconChallenge + " \"" + rcon + "\" " + cmd);
    }

    /**
     * @param line
     */
    private void traiteLigneJoueur(String line) {
        StrTokenizer st3 = new StrTokenizer(line);
        /* numero de joueur */
        int index = Integer.parseInt(st3.nextToken());
        /* nom du joueur : virer les guillemets */
        String playernick = StringUtils.strip(st3.nextToken(), "\"");
        /* numero de joueur (bis) */
        st3.nextToken();
        /* steam_id */
        String steamid = st3.nextToken();
        int frags = Integer.parseInt(st3.nextToken());
        /* le reste osef pour l'instant */
        Player player = new Player(steamid);
        player.setName(playernick);
        player.setFrags(frags);
        server.setPlayer(index, player);
    }

    /**
     * efface le rcon.
     */
    private void unsetRcon() {
        rcon = null;
        rl.tstop();
    }
}
