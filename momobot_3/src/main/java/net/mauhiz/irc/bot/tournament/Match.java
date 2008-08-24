package net.mauhiz.irc.bot.tournament;

import java.util.ArrayList;

/**
 * @author Topper
 * 
 */
public class Match extends ArrayList<Team> {
    /**
     * serial
     */
    private static final long serialVersionUID = 1L;
    /**
     * Id du match
     */
    private final int id;
    /**
     * Map
     */
    private final String map;
    /**
     * 
     */
    private final int phase;
    /**
     * Tableau du score du match
     */
    private final int[] score = new int[2];
    // Team1 vs Team2
    /**
     * Team 1
     */
    private Team team1;
    /**
     * Team 2
     */
    private Team team2;
    /**
     * Team winner
     */
    private Team winner;
    
    /**
     * @param phase1
     * @param id1
     * @param map1
     * @param team1_
     */
    public Match(final int phase1, final int id1, final String map1, final Team team1_) {
        team1 = team1_;
        phase = phase1;
        map = map1;
        score[0] = 0;
        score[1] = 0;
        id = id1;
        if (team1.equals(team2)) {
            score[0] = -1;
            score[1] = -1;
        }
    }
    /**
     * @param phase1
     * @param id1
     *            ID du match
     * @param map1
     * @param team1_
     *            team 1
     * @param team2_
     *            team 2
     */
    public Match(final int phase1, final int id1, final String map1, final Team team1_, final Team team2_) {
        // on met tjs les teams dans le bon sens
        if (team1_.getId() > team2_.getId()) {
            team2 = team1_;
            team1 = team2_;
        } else {
            team1 = team1_;
            team2 = team2_;
        }
        
        phase = phase1;
        map = map1;
        score[0] = 0;
        score[1] = 0;
        id = id1;
        if (team1.getId() == team2.getId()) {
            score[0] = -1;
            score[1] = -1;
        }
    }
    /**
     * 
     * @param oldmatch
     * @param team2_
     */
    public Match(final Match oldmatch, final Team team2_) {
        if (oldmatch.team1.getId() > team2_.getId()) {
            // on switch team1 et team2
            team2 = oldmatch.team1;
            team1 = team2_;
        } else {
            team1 = oldmatch.team1;
            team2 = team2_;
        }
        phase = oldmatch.phase;
        map = oldmatch.map;
        score[0] = 0;
        score[1] = 0;
        id = oldmatch.id;
        if (team1.equals(team2)) {
            score[0] = -1;
            score[1] = -1;
        }
    }
    
    /**
     * @return {@link #id}
     */
    public final int getId() {
        return id;
    }
    /**
     * @return {@link #map}
     */
    public final String getMap() {
        return map;
    }
    /**
     * @return {@link #phase}
     */
    public final int getPhase() {
        return phase;
    }
    
    /**
     * @return 2 == team 2 win; 1 == team 1 win ; 0 == draw
     */
    public final int getWinner() {
        if (winner != null) {
            if (score[0] > score[1]) {
                return 2;
            } else if (score[0] < score[1]) {
                return 1;
            }
        }
        return -1;
    }
    /**
     * @return true si
     */
    public boolean isReady() {
        if (team2 == null) {
            return false;
        }
        return true;
    }
    
    /**
     * @param team
     * @return
     * 
     */
    public final boolean isTeamIn(final Team team) {
        if (winner == null) {
            if (team1.getId() == team.getId()) {
                return true;
            }
            if (team2 != null && team2.getId() == team.getId()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param team
     * @param scoreTeam1
     * @param scoreTeam2
     */
    public final String setScore(final Team team, final int scoreTeam1, final int scoreTeam2) {
        if (team2 == null) {
            return "Erreur : Impossible de mettre le score, la team " + team.getId() + " n'a pas d'adversaire.";
        }
        
        if (team1.getId() == team.getId()) {
            winner = team1;
            score[0] = scoreTeam1;
            score[1] = scoreTeam2;
            return toString();
        } else if (team2.getId() == team.getId()) {
            winner = team2;
            score[0] = scoreTeam2;
            score[1] = scoreTeam1;
            return toString();
        }
        return "Erreur : Match incompatible.";
        
    }
    /**
     * @see java.util.AbstractCollection#toString()
     */
    @Override
    public final String toString() {
        // on a un gagnant
        if (winner != null) {
            // on a gagner le tn
            String gagnant = "";
            if (phase == 1) {
                gagnant = " " + winner.toString() + " gagne le tournois.";
            }
            
            if (team1.getId() == winner.getId()) {
                return "Team " + team1.getId() + " (WINNER): " + score[0] + " vs Team " + team2.getId() + ":"
                        + score[1] + "." + gagnant;
            }
            if (team2.getId() == winner.getId()) {
                return "Team " + team1.getId() + ": " + score[0] + " vs Team " + team2.getId() + " (WINNER):"
                        + score[1] + "." + gagnant;
            }
            
        }
        // on a pas de gagnant
        if (team2 == null) {
            // pas d'adversaire
            // return "La team " + team1.getId() + "=" + team1.getNom()
            // + " en attente du résultat des adversaires. next map=" + map;
            return team1.getNom() + " en attente du résultat des adversaires. Next map=" + map;
        }
        // le match est en attente de résultat
        // return "Team n°" + team1.getId() + "=" + team1.getNom() + " vs Team n°" + team2.getId() + "=" +
        // team2.getNom()
        // + ".";
        return team1.getNom() + " vs " + team2.getNom() + ".";
    }
    
}
