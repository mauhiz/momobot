package net.mauhiz.irc.bot.event;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcUser;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
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
            Team team = new Team(numberPlayerPerTeam, "Team n°:" + i, i);
            teamList.add(team);
        }
        // On Crée la liste de map
        for (String map : maps) {
            mapList.add(map);
        }
        int reste = numberTeams % 2;
        int phase = maps.length - 1; // 3
        for (int i = 0; i < numberTeams / 2; i++) {
            Match match = new Match(phase, i, mapList.get(0), teamList.get(0), teamList.get(i + 1));
            matchList.add(match);
        }
        if (reste == 1) {
            Match match = new Match(0, numberTeams / 2, mapList.get(0), teamList.get(teamList.size() - 1), teamList
                    .get(teamList.size() - 1));
            matchList.add(match);
        }
        
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
            Match match = new Match(newphase, 0, mapList.get(0), team, team);
            matchList.add(match);
            return "Bravo, team n°" + team.getId() + " gagne le tn! o//";
            
        }
        int nombreMatchPerSide = power(2, newphase);
        int newID = team.getId() / nombreMatchPerSide;
        int testMatch = testMatch(newphase, newID);
        if (testMatch == -1) {
            // le match n'existe pas
            Match match = new Match(newphase, newID, mapList.get(mapList.size() - newID), team);
            matchList.add(match);
            return "La team " + team.getId() + " en attente du résultat des adversaires. next map = " + match.getMap();
        }
        // le match existe, on ajoute l'autre team
        Match oldmatch = matchList.remove(testMatch);
        Match match = new Match(oldmatch, team);
        matchList.add(match);
        return "Nouveau match : " + match.toString() + " map = " + match.getMap();
        
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
    
    public String setScore(final int idTeam, final int score1, final int score2) {
        if (idTeam < 0 || idTeam > teamList.size() - 1) {
            return "Erreur : Id team invalide";
        }
        
        if (score1 < score2) {
            return "Erreur : vous etes sur de la team qui a gagne?";
        }
        for (Match match : matchList) {
            
            if (match.isTeamIn(teamList.get(idTeam))) {
                match.setScore(score1, score2);
                
                return match.toString();
            }
        }
        
        return "La team " + teamList.get(idTeam).getId() + " est déja éléminé.";
    }
    
    /**
     * @param index
     * @param nicknames
     * @return
     */
    public String setTeam(final int index, final String country, final String[] nicknames) {
        if (index < 0 || index > teamList.size() - 1 || nicknames.length > numberPlayerPerTeam) {
            return "Erreur";
        }
        
        Team team = teamList.get(index);
        List<IrcUser> ircuserList = new ArrayList<IrcUser>();
        for (String element : nicknames) {
            IrcUser user = new IrcUser(element);
            ircuserList.add(user);
        }
        // On clean pour tout remettre
        team.clear();
        if (team.addAll(ircuserList)) {
            String listPlayer = "";
            if (!team.isEmpty()) {
                for (IrcUser element : team) {
                    listPlayer += element + " ";
                }
            }
            
            return "Team n°" + index + " Tag :" + team.toString() + " Pays :" + team.getCountry() + " Joueur(s) :"
                    + listPlayer;
        }
        return "erreur";
        
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
        return "Tounois : " + teamList.size() + " teams de " + numberPlayerPerTeam + " joueurs.";
    }
    
}
