package net.mauhiz.irc.bot.tournament;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * @author topper
 * 
 */
public class Tournament extends ChannelEvent {
    
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(Gather.class);
    /**
     * 
     */
    private static int numberPlayerPerTeam = 5;
    /**
     * 
     */
    private final List<String> mapList = new ArrayList<String>();
    /**
     * 
     */
    private List<Match> matchList = new ArrayList<Match>();
    /**
     * le temps où je commence.
     */
    private final StopWatch sw = new StopWatch();
    /**
     * l'ensemble de joueurs. Ne sera jamais <code>null</code>
     */
    private final List<Team> teamList = new ArrayList<Team>();
    
    /**
     * @param channel1
     * @param maps
     */
    public Tournament(final Channel channel1, final String[] maps) {
        super(channel1);
        sw.start();
        int numberTeams = power(2, maps.length); // 2^4=16
        // On crée les teams
        if (LOG.isDebugEnabled()) {
            LOG.debug("Lancement d'un tn sur: " + channel1.toString() + " maps: " + StringUtils.join(maps));
        }
        
        for (int i = 0; i < numberTeams; i++) {
            Team team = new Team(numberPlayerPerTeam, i, "Tag", "FR");
            teamList.add(team);
        }
        // On Crée la liste de map
        for (String map : maps) {
            mapList.add(map);
        }
        int phase = mapList.size(); // 3
        for (int i = 0; i < numberTeams / 2; i++) {
            Match match = new Match(phase, i, mapList.get(0), teamList.get(2 * i), teamList.get(2 * i + 1));
            matchList.add(match);
        }
        
    }
    
    public ArrayList<String> getListTeam() {
        ArrayList<String> reply = new ArrayList<String>();
        for (Team element : teamList) {
            reply.add(element.toString());
        }
        return reply;
    }
    
    /**
     * @param a
     * @param b
     * @return int
     */
    private final int power(final int a, final int b) {
        if (b < 0) {
            return -1;
        }
        int rest = 1;
        for (int i = 0; i < b; i++) {
            rest = a * rest;
        }
        return rest;
    }
    /**
     * @param oldMatch
     * @param team
     * @return
     * 
     */
    private String setNextMatch(final Match oldMatch, final Team team) {
        int newphase = oldMatch.getPhase() - 1;
        int ID = oldMatch.getID();
        // C'était la finale
        if (newphase == 0) {
            // Match match = new Match(newphase, 0, mapList.get(0), team, team);
            // matchList.add(match);
            return "Bravo, team n°" + team.getId() + "=" + team.getNom() + " gagne le tournois! o//";
            
        }
        int nombreMatchPerSide = power(2, newphase);
        int id = team.getId();
        // int newID = id / nombreMatchPerSide;
        int newID = id / power(2, mapList.size() - newphase) / power(2, mapList.size() - newphase);
        int testMatch = testMatch(newphase, newID);
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
     * @param idTeam
     *            qui a win
     * @param score1
     *            de la team winner
     * @param score2
     *            de la team qui a loose
     * @return
     */
    
    public ArrayList<String> setScore(final int idTeam, final int score1_, final int score2_) {
        ArrayList<String> reply = new ArrayList<String>();
        int score1 = score1_;
        int score2 = score2_;
        
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
     * @param index
     * @param country
     * @param tag
     * @param nicknames
     *            REM : $tn-register IDTEAM COUNTRY TAG PLAYER1 PLAYER2 PLAYER..
     * @return string
     */
    public String setTeam(final int index, final String country, final String tag, final ArrayList<String> nicknames) {
        if (index < 0 || index > teamList.size() - 1) {
            return "Erreur index invalid";
        }
        
        Team team = teamList.get(index);
        team.setCountry(country);
        team.setNom(tag);
        
        // On clean pour tout remettre
        team.clear();
        
        if (nicknames.size() < team.getCapacity()) {
            team.addAll(nicknames);
            for (int i = nicknames.size() + 1; i <= team.getCapacity(); i++) {
                team.add("Player " + i);
            }
        } else {
            team.addAll(nicknames.subList(0, team.getCapacity()));
        }
        return team.toString();
    }
    
    /**
     * @param phase
     * @param id
     * @return i <=> numéro du match ou -1 si le match n'existe pas
     */
    private int testMatch(final int phase, final int id) {
        for (int i = 0; i < matchList.size(); i++) {
            Match match = matchList.get(i);
            
            if (match.getPhase() == phase && match.getID() == id) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.ChannelEvent#toString()
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String matchEnAttente = "";
        String matchEnCour = "";
        Match finale = null;
        for (Match element : matchList) {
            if (element.getWinner() == -1) {
                if (element.isReady()) {
                    matchEnCour += element.toString();
                } else {
                    matchEnAttente += element.toString();
                }
            }
            if (element.getPhase() == 1) {
                
                finale = element;
            }
        }
        if (finale != null) {
            return "Tounois : " + teamList.size() + " teams de " + numberPlayerPerTeam + " joueurs. finale : "
                    + finale.toString();
        }
        return "Tounois : " + teamList.size() + " teams de " + numberPlayerPerTeam + " joueurs." + " Matchs en cours:"
                + matchEnCour + " Matchs en attente :" + matchEnAttente;
    }
    
}
