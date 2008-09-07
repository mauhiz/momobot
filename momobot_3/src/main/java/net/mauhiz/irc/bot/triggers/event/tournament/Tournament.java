package net.mauhiz.irc.bot.triggers.event.tournament;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.mauhiz.irc.MathUtils;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.bot.event.ChannelEvent;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Topper
 */
public class Tournament extends ChannelEvent {
    /**
     * config
     */
    private static final Configuration CFG;
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(Tournament.class);
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
    static void uploadFile(final File temp) throws IOException {
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
            InputStream is = null;
            try {
                is = new FileInputStream(temp);
                boolean success = client.storeFile(ftpURI.getPath(), is);
                if (!success) {
                    LOG.warn("Could not upload to " + ftpURI);
                }
            } finally {
                IOUtils.closeQuietly(is);
            }
            
        } else {
            throw new UnsupportedOperationException("protocol not yet supported" + ftpURI.getScheme());
        }
    }
    
    /**
     * 
     */
    private boolean isLunched;
    
    /**
     * 
     */
    private final List<String> mapList = new ArrayList<String>();
    /**
     * 
     */
    private List<Match> matchList = new ArrayList<Match>();
    /**
     * 
     */
    private int numberPlayerPerTeam;
    /**
     * 
     */
    private int numberTeams;
    
    /**
     * le temps où je commence.
     */
    private final StopWatch sw = new StopWatch();
    
    /**
     * l'ensemble de joueurs. Ne sera jamais <code>null</code>
     */
    private final List<TournamentTeam> teamList = new ArrayList<TournamentTeam>();
    
    /**
     * @param chan
     * @param maps
     */
    public Tournament(final IrcChannel chan, final String[] maps) {
        super(chan);
        sw.start();
        numberPlayerPerTeam = CFG.getInt("tn.numberPlayerPerTeam");
        numberTeams = MathUtils.power(2, maps.length); // 2^4=16
        // On crée les teams
        if (LOG.isDebugEnabled()) {
            LOG.debug("Lancement d'un tn sur: " + chan.toString() + " maps: " + StringUtils.join(maps));
        }
        
        // On Crée la liste de map
        for (String map : maps) {
            mapList.add(map);
        }
        
    }
    
    /**
     * @return template File.
     * @throws Exception
     */
    File createTemplateFile() throws Exception {
        VelocityContext context = new VelocityContext();
        StringBuilder maps = new StringBuilder();
        for (String map : mapList) {
            maps.append(",'");
            maps.append(map);
            maps.append('\'');
        }
        context.put("maps", maps.substring(1));
        
        VelocityEngine ve = new VelocityEngine();
        File temp = new File(CFG.getString("tn.tempfile.name"));
        
        ve.init();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(temp);
            
            Template plate = ve.getTemplate(CFG.getString("tn.vm"));
            plate.initDocument();
            plate.merge(context, writer);
            writer.flush();
            LOG.debug(FileUtils.readFileToString(temp));
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return temp;
    }
    
    /**
     * genere et upload le template
     */
    public void generateTemplate() {
        File temp = null;
        try {
            temp = createTemplateFile();
            uploadFile(temp);
        } catch (Exception ioe) {
            LOG.error(ioe, ioe);
        } finally {
            FileUtils.deleteQuietly(temp);
        }
    }
    /**
     * @return listTeam
     */
    public List<String> getListTeam() {
        List<String> reply = new ArrayList<String>();
        for (TournamentTeam element : teamList) {
            reply.add(element.toString());
        }
        return reply;
    }
    /**
     * @param phase
     * @param id
     * @return i <=> numéro du match ou -1 si le match n'existe pas
     */
    private int getMatchId(final int phase, final int id) {
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
        
        if (!isLunched && teamList.size() == numberTeams) {
            isLunched = true;
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
        return isLunched;
    }
    
    /**
     * @param oldMatch
     * @param team
     * @return String
     */
    private String setNextMatch(final Match oldMatch, final TournamentTeam team) {
        int newphase = oldMatch.getPhase() - 1;
        // C'était la finale
        if (newphase == 0) {
            // Match match = new Match(newphase, 0, mapList.get(0), team, team);
            // matchList.add(match);
            return "Bravo, team n°" + team.getId() + "=" + team.getNom() + " gagne le tournois! o//";
            
        }
        int id = team.getId();
        // int newID = id / nombreMatchPerSide;
        int newID = id / MathUtils.power(2, mapList.size() - newphase) / MathUtils.power(2, mapList.size() - newphase);
        int testMatch = getMatchId(newphase, newID);
        if (testMatch == -1) {
            // le match n'existe pas
            Match match = new Match(newphase, newID, mapList.get(mapList.size() - newphase), team);
            matchList.add(match);
            // "La team " + team.getId() + " en attente du résultat des adversaires. next map = " + match.getMap();
            return match.toString();
            
        }
        // le match existe, on ajoute l'autre team
        Match oldmatch = matchList.remove(testMatch);
        Match match = new Match(oldmatch, team);
        matchList.add(match);
        return "Nouveau match : " + match.toString();
        
    }
    /**
     * @param ircuser
     * @param idTeam
     *            qui a win
     * @param score01
     *            de la team winner
     * @param score02
     *            de la team qui a loose
     * @return List String
     */
    
    public List<String> setScore(final IrcUser ircuser, final int idTeam, final int score01, final int score02) {
        List<String> reply = new ArrayList<String>();
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
        reply.add("La team " + teamList.get(idTeam).getId() + " est déja éléminé.");
        return reply;
    }
    /**
     * @param ircuser
     * @param loc
     * @param tag
     * @param nicknames
     *            REM : $tn-register IDTEAM COUNTRY TAG PLAYER1 PLAYER2 PLAYER..
     * @return string
     */
    public String setTeam(final IrcUser ircuser, final Locale loc, final String tag, final List<String> nicknames) {
        for (TournamentTeam element : teamList) {
            if (element.isOwner(ircuser)) {
                TournamentTeam team = element;
                team.setCountry(loc);
                team.setNom(tag);
                
                // On clean pour tout remettre
                team.clear();
                
                if (nicknames.size() < team.getCapacity()) {
                    team.addAll(nicknames);
                    for (int i = nicknames.size() + 1; i <= team.getCapacity(); i++) {
                        team.add("?");
                    }
                } else {
                    team.addAll(nicknames.subList(0, team.getCapacity()));
                }
                
                return team.toString();
            }
        }
        TournamentTeam team = new TournamentTeam(numberPlayerPerTeam, teamList.size(), tag, loc, ircuser);
        if (nicknames.size() < team.getCapacity()) {
            team.addAll(nicknames);
            for (int i = nicknames.size() + 1; i <= team.getCapacity(); i++) {
                team.add("?");
            }
        } else {
            team.addAll(nicknames.subList(0, team.getCapacity()));
        }
        teamList.add(team);
        return team.toString();
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.ChannelEvent#toString()
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        
        String matchEnAttente = " Match en cours: ";
        String matchEnCours = " Match en attente : ";
        Match finale = null;
        for (Match element : matchList) {
            if (element.getWinner() == -1) {
                if (element.isReady()) {
                    matchEnCours += element.toString();
                } else {
                    matchEnAttente += element.toString();
                }
            }
            if (element.getPhase() == 1) {
                
                finale = element;
            }
        }
        if (" Match en cours: ".equals(matchEnAttente)) {
            matchEnAttente = "";
        }
        if (" Match en attente : ".equals(matchEnCours)) {
            matchEnCours = "";
        }
        
        if (finale != null) {
            return "Tounois : " + teamList.size() + " teams de " + numberPlayerPerTeam + " joueurs. finale : "
                    + finale.toString();
        }
        return "Tounois : " + teamList.size() + " teams de " + numberPlayerPerTeam + " joueurs." + matchEnCours
                + matchEnAttente;
    }
    
}
