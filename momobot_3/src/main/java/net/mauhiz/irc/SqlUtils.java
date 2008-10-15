package net.mauhiz.irc;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class SqlUtils {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(SqlUtils.class);
    /**
     * mes maîtres.
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
    public static boolean authIsKnown(final String auth) {
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
    public static boolean isMaster(final String steamid) {
        return MASTERS.contains(steamid);
    }
    
//    /**
//     * @throws SQLException
//     *             en cas de foirage.
//     */
//    public static void loadPlayerDB() throws SQLException {
//        final ResultSet rs = doSqlQuery("SELECT `steamid`, `qauth`, `adminlvl` FROM `players`");
//        try {
//            while (rs.next()) {
//                final String steamid = rs.getString("steamid");
//                final String qauth = rs.getString("qauth");
//                final int master = rs.getInt("adminlvl");
//                PLAYERS.put(steamid, qauth);
//                if (master > 0) {
//                    MASTERS.add(steamid);
//                }
//            }
//        } finally {
//            rs.close();
//        }
//        LOG.debug("PlayerDB loaded");
//    }
    
//    /**
//     * @param steamid
//     *            le steam_id
//     * @param qnetAuth
//     *            l'auth Qnet
//     * @return un msg
//     */
//    public static String registerPlayer(final String steamid, final String qnetAuth) {
//        PLAYERS.put(steamid, qnetAuth);
//        try {
//            doSqlUpdate("INSERT INTO players (steamid, qauth) VALUES ('" + steamid + "', '" + qnetAuth + "')");
//            return "Joueur enregistré";
//        } catch (final SQLException sqle) {
//            LOG.error(sqle, sqle);
//            return "Erreur : joueur non enregistré";
//        }
//    }
    
    /**
     * @param steamid
     *            le steam_id
     * @return si le steamid est connu
     */
    public static boolean steamidIsKnown(final String steamid) {
        return PLAYERS.containsKey(steamid);
    }
    
    /**
     * constructeur caché.
     */
    protected SqlUtils() {
        throw new UnsupportedOperationException();
    }
}
