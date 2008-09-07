package net.mauhiz.irc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
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
    private static final Collection<String> MASTERS = new TreeSet<String>();
    /**
     * (steamid, qauth).
     */
    private static final Map<String, String> PLAYERS = new TreeMap<String, String>();
    /**
     * url du serveur mysql.
     */
    private static final String URL = "jdbc:mysql://mysql.mauhiz.net/momobot";
    
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
     * @param sql
     *            la requête sql.
     * @return un resultset
     * @throws SQLException
     *             en cas de problème!
     */
    public static ResultSet doSqlQuery(final String sql) throws SQLException {
        final Connection con = getConnection();
        try {
            return con.createStatement().executeQuery(sql);
        } finally {
            con.close();
        }
    }
    
    /**
     * @param sql
     *            la requête SQL.
     * @return si l'update a reussi
     * @throws SQLException
     *             en cas de problème!
     */
    public static int doSqlUpdate(final String sql) throws SQLException {
        final Connection con = getConnection();
        try {
            return con.createStatement().executeUpdate(sql);
        } finally {
            con.close();
        }
    }
    
    /**
     * @return une connection vers mysql
     * @throws SQLException
     *             en cas de connection impossible
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, "momobot", "tobomom");
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
    
    /**
     * @throws SQLException
     *             en cas de foirage.
     */
    public static void loadPlayerDB() throws SQLException {
        final ResultSet rs = doSqlQuery("SELECT `steamid`, `qauth`, `adminlvl` FROM `players`");
        try {
            while (rs.next()) {
                final String steamid = rs.getString("steamid");
                final String qauth = rs.getString("qauth");
                final int master = rs.getInt("adminlvl");
                PLAYERS.put(steamid, qauth);
                if (master > 0) {
                    MASTERS.add(steamid);
                }
            }
        } finally {
            rs.close();
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
    public static String registerPlayer(final String steamid, final String qnetAuth) {
        PLAYERS.put(steamid, qnetAuth);
        try {
            doSqlUpdate("INSERT INTO players (steamid, qauth) VALUES ('" + steamid + "', '" + qnetAuth + "')");
            return "Joueur enregistré";
        } catch (final SQLException sqle) {
            LOG.error(sqle, sqle);
            return "Erreur : joueur non enregistré";
        }
    }
    
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
