package net.mauhiz.irc;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import net.mauhiz.irc.bot.triggers.dispo.Dispo;

import org.apache.commons.beanutils.ConstructorUtils;
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
     * nom du profil en cours.
     */
    private static String profil;
    /**
     * root package.
     */
    private static final String TRIG_CLS_ROOT = "momobot.";
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
     * @param channel
     *            le channel
     * @param date
     *            la date
     * @return la map
     * @throws SQLException
     *             en cas d'erreur.
     */
    public static Iterable<Dispo> getDispos(final String channel, final long date) throws SQLException {
        final Date datesql = new Date(date);
        final ResultSet rs = doSqlQuery("SELECT `qauth`, `heure1`, `heure2` FROM `dispo` WHERE `channel` = '" + channel
                + "' AND `date`= '" + datesql.toString() + "'");
        final Collection<Dispo> dispos = new HashSet<Dispo>();
        try {
            while (rs.next()) {
                final String qauth = rs.getString("qauth");
                final int heure1 = rs.getInt("heure1");
                final int heure2 = rs.getInt("heure2");
                final Dispo dispo = new Dispo(qauth, heure1, heure2);
                dispos.add(dispo);
            }
        } finally {
            rs.close();
        }
        return dispos;
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
     * @param className
     * @param triggerText
     */
    static void loadTrigger(final String className, final String triggerText) {
        try {
            final Class<?> toLoad = Class.forName(className);
            ConstructorUtils.invokeConstructor(toLoad, triggerText);
        } catch (final NoSuchMethodException nsme) {
            LOG.error("Invalid trigger class: " + className, nsme);
        } catch (final ClassNotFoundException cnfe) {
            LOG.warn("Trigger not found: " + className, cnfe);
        } catch (final InstantiationException ie) {
            LOG.error("Could not create trigger: " + className, ie);
        } catch (final IllegalAccessException iae) {
            LOG.error("Invalid trigger class: " + className, iae);
        } catch (final InvocationTargetException ite) {
            LOG.error("Could not create trigger: " + className, ite.getTargetException());
        }
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
            doSqlUpdate("INSERT INTO `players`(`steamid`, `qauth`) VALUES('" + steamid + "', '" + qnetAuth + "')");
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
     * @param channel
     *            le channel
     * @param qauth
     *            l'auth Qnet
     * @param time
     *            La date
     * @param heure1
     *            indicateur
     * @param heure2
     *            indicateur
     * @throws SQLException
     *             en cas de galère.
     */
    public static void updateDispo(final String channel, final String qauth, final long time, final byte heure1,
            final byte heure2) throws SQLException {
        final String date = new java.sql.Date(time).toString();
        doSqlUpdate("DELETE FROM `dispo` WHERE `channel` = '" + channel + "' AND `qauth` = '" + qauth
                + "' AND `date` = '" + date + "'");
        doSqlUpdate("INSERT INTO `dispo`(`channel`, `qauth`, `date`, `heure1`, `heure2`) VALUES('" + channel + "', '"
                + qauth + "', '" + date + "', " + heure1 + ", " + heure2 + ")");
    }
    
    /**
     * constructeur caché.
     */
    protected SqlUtils() {
        throw new UnsupportedOperationException();
    }
}
