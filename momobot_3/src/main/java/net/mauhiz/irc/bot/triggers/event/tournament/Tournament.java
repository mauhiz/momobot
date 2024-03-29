package net.mauhiz.irc.bot.triggers.event.tournament;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.bot.event.AbstractChannelEvent;
import net.mauhiz.util.FileUtil;
import net.mauhiz.util.Power;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Topper
 */
public class Tournament extends AbstractChannelEvent {
    /**
     * config
     */
    private static final Configuration CFG;
    static {
        try {
            CFG = new PropertiesConfiguration("tournament/tn.properties");
        } catch (ConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * @param temp
     * @throws IOException
     */
    static void uploadFile(File temp) throws IOException {
        String ftp = CFG.getString("tn.upload.to");
        URI ftpURI = URI.create(ftp);
        if ("ftp".equals(ftpURI.getScheme())) {
            FTPClient client = new FTPClient();
            int port = ftpURI.getPort();
            if (port <= 0) {
                port = FTP.DEFAULT_PORT;
            }
            client.connect(ftpURI.getHost(), port);
            client.enterLocalPassiveMode();
            String userNfo = ftpURI.getUserInfo();
            String user = StringUtils.substringBefore(userNfo, ":");
            String password = StringUtils.substringAfter(userNfo, ":");
            client.login(user, password);
            String remotePath = ftpURI.getPath();
            int slash = remotePath.lastIndexOf('/');
            String remoteDir = remotePath.substring(0, slash);
            LOG.debug("cwd to " + remoteDir);
            client.changeWorkingDirectory(remoteDir);

            String remoteFileName = remotePath.substring(slash);
            LOG.debug("storing to " + remoteFileName);

            try (InputStream is = new FileInputStream(temp);) {
                boolean success = client.storeFile(ftpURI.getPath(), is);
                if (!success) {
                    LOG.warn("Could not upload to " + ftpURI);
                }
            }

        } else {
            throw new UnsupportedOperationException("protocol not yet supported" + ftpURI.getScheme());
        }
    }

    /**
     * 
     */
    private boolean isLaunched;

    /**
     * 
     */
    private final List<String> mapList = new ArrayList<>();
    /**
     * 
     */
    private final List<Match> matchList = new ArrayList<>();
    /**
     * 
     */
    private final int numberPlayerPerTeam;
    /**
     * 
     */
    private final int numberTeams;

    /**
     * le temps ou je commence.
     */
    private final StopWatch sw = new StopWatch();

    /**
     * l'ensemble de joueurs.
     */
    private final List<TournamentTeam> teamList = new ArrayList<>();

    /**
     * @param chan
     * @param maps
     */
    public Tournament(IrcChannel chan, Collection<String> maps) {
        super(chan);
        sw.start();
        numberPlayerPerTeam = CFG.getInt("tn.numberPlayerPerTeam");
        numberTeams = Power.power(2, maps.size()); // 2^4=16
        // On cree les teams
        if (LOG.isDebugEnabled()) {
            LOG.debug("Lancement d'un tn sur: " + chan.fullName() + " maps: " + StringUtils.join(maps, ' '));
        }

        // On cree la liste de map
        for (String map : maps) {
            mapList.add(map);
        }
    }

    /**
     * @return template File.
     */
    File createTemplateFile() throws IOException {
        VelocityContext context = new VelocityContext();
        StringBuilder maps = new StringBuilder();
        for (String map : mapList) {
            maps.append(",'");
            maps.append(map);
            maps.append('\'');
        }
        context.put("maps", maps.substring(1));

        VelocityEngine ve = new VelocityEngine();
        ve.init();

        File temp = File.createTempFile(CFG.getString("tn.tempfile.name"), null);

        try (Writer writer = new FileWriterWithEncoding(temp, FileUtil.UTF8)) {
            Template plate = ve.getTemplate(CFG.getString("tn.vm"));
            plate.initDocument();
            plate.merge(context, writer);
            writer.flush();
            if (LOG.isDebugEnabled()) {
                LOG.debug(FileUtils.readFileToString(temp));
            }
        }
        return temp;
    }

    /**
     * genere et upload le template
     */
    public void generateTemplate() {
        try {
            File temp = createTemplateFile();
            uploadFile(temp);
        } catch (IOException ioe) {
            LOG.error(ioe, ioe);
        }
    }

    /**
     * @return listTeam
     */
    public List<String> getListTeam() {
        List<String> reply = new ArrayList<>();
        for (TournamentTeam element : teamList) {
            reply.add(element.toString());
        }
        return reply;
    }

    /**
     * @param phase
     * @param id
     * @return i <=> numero du match ou -1 si le match n'existe pas
     */
    private int getMatchId(int phase, int id) {
        for (int i = 0; i < matchList.size(); i++) {
            Match match = matchList.get(i);

            if (match.getPhase() == phase && match.getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return status
     */
    public String getStatus() {

        if (!isLaunched && teamList.size() == numberTeams) {
            isLaunched = true;
            int phase = mapList.size(); // 3
            for (int i = 0; i < numberTeams / 2; i++) {
                Match match = new Match(phase, i, mapList.get(0), teamList.get(2 * i), teamList.get(2 * i + 1));
                matchList.add(match);
            }
            return "Attention le Tournois commence. A vos marques! Pret? GO!!";

        }
        return toString();
    }

    /**
     * @return 0== pas complet ; 1 complet ; -1 == erreur (trop plein)
     * 
     */
    public boolean isReady() {
        return isLaunched;
    }

    /**
     * @param oldMatch
     * @param team
     * @return String
     */
    private String setNextMatch(Match oldMatch, TournamentTeam team) {
        int newphase = oldMatch.getPhase() - 1;
        // C'etait la finale
        if (newphase == 0) {
            // Match match = new Match(newphase, 0, mapList.get(0), team, team);
            // matchList.add(match);
            return "Bravo, team #" + team.getId() + "=" + team.getNom() + " gagne le tournois! o//";

        }
        int id = team.getId();
        // int newID = id / nombreMatchPerSide;
        int newID = id / Power.power(2, mapList.size() - newphase) / Power.power(2, mapList.size() - newphase);
        int testMatch = getMatchId(newphase, newID);
        if (testMatch == -1) {
            // le match n'existe pas
            Match match = new Match(newphase, newID, mapList.get(mapList.size() - newphase), team);
            matchList.add(match);
            // "La team " + team.getId() + " en attente du resultat des adversaires. next map = " + match.getMap();
            return match.toString();

        }
        // le match existe, on ajoute l'autre team
        Match oldmatch = matchList.remove(testMatch);
        Match match = new Match(oldmatch, team);
        matchList.add(match);
        return "Nouveau match : " + match.toString();

    }

    /**
     * @param idTeam
     *            qui a win
     * @param score01
     *            de la team winner
     * @param score02
     *            de la team qui a loose
     * @return List String
     */

    public List<String> setScore(int idTeam, int score01, int score02) {
        List<String> reply = new ArrayList<>();
        int score1 = score01;
        int score2 = score02;

        if (idTeam < 0 || idTeam > teamList.size() - 1) {
            reply.add("Erreur : Id team invalide");
            return reply;
        }

        if (score1 <= score2) {
            int score3 = score1;
            score1 = score2;
            score2 = score3;
        }

        for (Match match : matchList) {
            // on regarde si le match est complet ou pas
            if (match.isTeamIn(teamList.get(idTeam)) && match.getWinner() == -1) {
                reply.add(match.setScore(teamList.get(idTeam), score1, score2));
                // si le match est bien mise a jour
                if (match.isReady()) {
                    reply.add(setNextMatch(match, teamList.get(idTeam)));
                }
                return reply;
            }
        }
        reply.add("La team " + teamList.get(idTeam).getId() + " est deja eliminee.");
        return reply;
    }

    /**
     * @param ircuser
     * @param loc
     * @param tag
     * @param args
     *            REM : $tn-register IDTEAM COUNTRY TAG PLAYER1 PLAYER2 PLAYER..
     * @return string
     */
    public String setTeam(IrcUser ircuser, Locale loc, String tag, Collection<String> args) {
        for (TournamentTeam element : teamList) {
            if (element.isOwner(ircuser)) {
                TournamentTeam team = element;
                team.setCountry(loc);
                team.setNom(tag);

                // On clean pour tout remettre
                team.clear();
                team.addAll(args);

                while (team.size() < team.getCapacity()) {
                    team.add("?");
                }

                return team.toString();
            }
        }
        TournamentTeam team = new TournamentTeam(numberPlayerPerTeam, teamList.size(), tag, loc, ircuser);
        team.addAll(args);

        while (team.size() < team.getCapacity()) {
            team.add("?");
        }
        teamList.add(team);
        return team.toString();
    }

    /**
     * @see net.mauhiz.irc.bot.event.AbstractChannelEvent#toString()
     */
    @Override
    public String toString() {

        Match finale = null;
        List<Match> enCours = new ArrayList<>();
        List<Match> enAttente = new ArrayList<>();
        for (Match element : matchList) {
            if (element.getWinner() == -1) {
                if (element.isReady()) {
                    enCours.add(element);
                } else {
                    enAttente.add(element);
                }
            }
            if (element.getPhase() == 1) {
                finale = element;
            }
        }

        String matchEnCours;
        if (enCours.isEmpty()) {
            matchEnCours = "";
        } else {
            matchEnCours = " Match en attente : " + StringUtils.join(enCours, "");
        }

        String matchEnAttente;
        if (enCours.isEmpty()) {
            matchEnAttente = "";
        } else {
            matchEnAttente = " Match en cours: " + StringUtils.join(enAttente, "");
        }

        if (finale == null) {
            return "Tounois : " + teamList.size() + " teams de " + numberPlayerPerTeam + " joueurs." + matchEnCours
                    + matchEnAttente;
        }

        return "Tounois : " + teamList.size() + " teams de " + numberPlayerPerTeam + " joueurs. finale : " + finale;
    }

}
