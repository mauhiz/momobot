package net.mauhiz.irc.bot.event;

import java.util.ArrayList;

/**
 * @author Topper
 * 
 */
public class Match extends ArrayList<Team> {
    /**
     * Id du match
     */
    private final int Id;
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
    private Team team1 = null;
    /**
     * Team 2
     */
    private Team team2 = null;
    /**
     * Team winner
     */
    private Team winner = null;
    
    /**
     * @param phase1
     * @param id1
     * @param team1_
     */
    public Match(final int phase1, final int id1, final String map1, final Team team1_) {
        team1 = team1_;
        phase = phase1;
        map = map1;
        score[0] = 0;
        score[1] = 0;
        Id = id1;
        if (team1.equals(team2)) {
            score[0] = -1;
            score[1] = -1;
        }
    }
    /**
     * @param phase1
     * @param id1
     *            ID du match
     * @param team1_
     *            team 1
     * @param team2_
     *            team 2
     */
    public Match(final int phase1, final int id1, final String map1, final Team team1_, final Team team2_) {
        team1 = team1_;
        team2 = team2_;
        phase = phase1;
        map = map1;
        score[0] = 0;
        score[1] = 0;
        Id = id1;
        if (team1.equals(team2)) {
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
        team1 = oldmatch.team1;
        team2 = team2_;
        phase = oldmatch.phase;
        map = oldmatch.map;
        score[0] = 0;
        score[1] = 0;
        Id = oldmatch.Id;
        if (team1.equals(team2)) {
            score[0] = -1;
            score[1] = -1;
        }
    }
    
    public final int getID() {
        return Id;
    }
    /**
     * 
     */
    public final String getMap() {
        return map;
    }
    /**
     * @return phase
     */
    public final int getPhase() {
        return phase;
    }
    
    /**
     * @return 2 == team 2 win; 1 == team 1 win ; 0 == draw
     */
    public final int getWinner() {
        if (score[0] > score[1]) {
            return 2;
        } else if (score[0] < score[1]) {
            return 1;
        } else {
            return 0;
        }
    }
    /**
     * 
     */
    public final boolean isTeamIn(final Team team) {
        if (winner == null) {
            if (team1.equals(team) || team1.equals(team)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param scoreTeam1
     * @param scoreTeam2
     */
    public final void setScore(final int scoreTeam1, final int scoreTeam2) {
        score[0] = scoreTeam1;
        score[1] = scoreTeam2;
        if (scoreTeam1 > scoreTeam2) {
            winner = team1;
        } else if (scoreTeam1 < scoreTeam2) {
            winner = team2;
        }
    }
    /**
     * @see java.util.AbstractCollection#toString()
     */
    @Override
    public final String toString() {
        if (team1.equals(winner)) {
            return "Team " + team1.getId() + " (WINNER): " + score[0] + " vs Team " + team2.getId() + ":" + score[1]
                    + ".";
        }
        if (team2.equals(winner)) {
            return "Team " + team1.getId() + ": " + score[0] + " vs Team " + team2.getId() + " (WINNER):" + score[1]
                    + ".";
        }
        return "Team " + team1.getId() + ": " + score[0] + " vs Team " + team2.getId() + ":" + score[1] + ".";
    }
}
