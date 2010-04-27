package net.mauhiz.irc.bot.triggers.cs;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import net.mauhiz.util.HibernateUtils;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;

/**
 * @author mauhiz
 */
public class PlayerDB {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(PlayerDB.class);
    /**
     * mes maîtres
     */
    private static final Set<String> MASTERS = new TreeSet<String>();
    /**
     * (steamid, qauth).
     */
    private static final Map<String, String> PLAYERS = new TreeMap<String, String>();
    
    /**
     * @param auth
     *            l'auth
     * @return si l'auth est connue
     */
    public static boolean authIsKnown(String auth) {
        return PLAYERS.containsValue(auth);
    }
    
    /**
     * @return le nbre de players
     */
    public static int countPlayers() {
        return PLAYERS.size();
    }
    
    /**
     * @return les players
     */
    public static Iterable<Entry<String, String>> getPlayers() {
        return PLAYERS.entrySet();
    }
    
    /**
     * @param steamid
     *            un steam_id
     * @return si le steam id correspon a celui d'un master
     */
    public static boolean isMaster(String steamid) {
        return MASTERS.contains(steamid);
    }
    
    /**
     * Charge la liste des joueurs CS
     */
    public static void loadPlayerDB() {
        SQLQuery sqlq = HibernateUtils.currentSession().createSQLQuery(
                "SELECT `steamid`, `qauth`, `adminlvl` FROM `players`");
        
        for (Object next : sqlq.list()) {
            String[] line = (String[]) next;
            String steamid = line[0];
            String qauth = line[1];
            int master = Integer.parseInt(line[2]);
            PLAYERS.put(steamid, qauth);
            if (master > 0) {
                MASTERS.add(steamid);
            }
        }
        LOG.debug("PlayerDB loaded");
    }
    
    /**
     * @param steamid
     *            le steam_id
     * @param qnetAuth
     *            l'auth Qnet
     * @return un msg
     */
    public static String registerPlayer(String steamid, String qnetAuth) {
        PLAYERS.put(steamid, qnetAuth);
        SQLQuery sqlq = HibernateUtils.currentSession().createSQLQuery(
                "INSERT INTO players (steamid, qauth) VALUES ('" + steamid + "', '" + qnetAuth + "')");
        sqlq.executeUpdate();
        return "Joueur enregistre";
    }
    
    /**
     * @param steamid
     *            le steam_id
     * @return si le steamid est connu
     */
    public static boolean steamidIsKnown(String steamid) {
        return PLAYERS.containsKey(steamid);
    }
}
