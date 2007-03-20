package momobot;

import ircbot.IrcServerBean;
import ircbot.ATrigger;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import utils.Utils;

/**
 * @author Administrator
 */
public abstract class Db {
    /**
     * mes maîtres.
     */
    private static final Collection < String >  MASTERS = new TreeSet < String >();
    /**
     * (cle, memo).
     */
    private static final Map < String, String > MEMOS   = new TreeMap < String, String >();
    /**
     * (steamid, qauth).
     */
    private static final Map < String, String > PLAYERS = new TreeMap < String, String >();
    /**
     * nom du profil en cours.
     */
    private static String                       profil  = "";
    /**
     * url du serveur mysql.
     */
    private static final String                 URL     = "jdbc:mysql://mauhiz.net/momobot";

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
    public static int countMemos() {
        return MEMOS.size();
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
        final ResultSet rs = con.createStatement().executeQuery(sql);
        return rs;
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
        final int i = con.createStatement().executeUpdate(sql);
        return i;
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
    public static Iterator < Dispo > getDispos(final String channel,
            final long date) throws SQLException {
        final Date datesql = new Date(date);
        final ResultSet rs = doSqlQuery("SELECT `qauth`, `heure1`, `heure2` FROM `dispo` WHERE `channel` = '"
                + channel + "' AND `date`= '" + datesql.toString() + "'");
        final Set < Dispo > dispos = new HashSet < Dispo >();
        while (rs.next()) {
            final String qauth = rs.getString("qauth");
            final int heure1 = rs.getInt("heure1");
            final int heure2 = rs.getInt("heure2");
            final Dispo dispo = new Dispo(qauth, heure1, heure2);
            dispos.add(dispo);
        }
        return dispos.iterator();
    }

    /**
     * @param cle
     *            la clé
     * @return le mémo associé
     */
    public static String getMemo(final String cle) {
        if (!MEMOS.containsKey(cle)) {
            return "Pas de mémo pour " + cle;
        }
        return MEMOS.get(cle);
    }

    /**
     * @return la liste des memos
     */
    public static String getMemos() {
        String msg = "mémos :";
        for (final String string : MEMOS.keySet()) {
            msg += " " + string;
        }
        return msg;
    }

    /**
     * @return les players
     */
    public static Iterator < Map.Entry < String, String > > getPlayers() {
        return PLAYERS.entrySet().iterator();
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
     * @param string
     *            le nom du profil
     */
    public static void loadMomoBot(final String string) {
        // init le bot
        MomoBot.getInstance();
        String server = "";
        boolean identServer = false;
        profil = string;
        try {
            final ResultSet rs = doSqlQuery("SELECT `paramName`, `param` FROM `profil` WHERE `nom` = '"
                    + profil + "'");
            while (rs.next()) {
                final String paramName = rs.getString("paramName")
                        .toLowerCase();
                final String param = rs.getString("param");
                if (paramName.equals("login")) {
                    MomoBot.getInstance().setLogin(param);
                } else if (paramName.equals("nick")) {
                    MomoBot.getInstance().setMyName(param);
                } else if (paramName.equals("autojoin")) {
                    MomoBot.getInstance().addToAutoJoin(param);
                } else if (paramName.equals("trigger")) {
                    final int i = param.indexOf(" ");
                    final String className = "ircbot.trigger."
                            + param.substring(0, i);
                    final String triggerName = param.substring(i + 1);
                    final Class < ? > toLoad = Class.forName(className);
                    final Constructor < ? > c = toLoad
                            .getConstructor(String.class);
                    c.newInstance(triggerName);
                } else if (paramName.equals("identserver")) {
                    if (param.startsWith("1")) {
                        // on attend avant de le lancer, il y a peut etre 50
                        // params a venir la
                        identServer = true;
                    }
                } else if (paramName.equals("server")) {
                    // limitation volontaire a un seul serveur
                    server = param;
                }
            }
            // connect le bot
            if (server != null) {
                if (identServer) {
                    MomoBot.getInstance().startIdentServer();
                }
                MomoBot.getInstance().connect(new IrcServerBean(server));
                return;
            }
            Utils.logError(Db.class, new Exception("pas de serveur"));
        } catch (final Exception e) {
            Utils.logError(Db.class, e);
        }
    }

    /**
     * @throws Exception
     *             en cas de foirage.
     */
    public static void loadMemoDB() throws Exception {
        final ResultSet rs = doSqlQuery("SELECT `cle`, `memo` FROM `memos`");
        while (rs.next()) {
            final String cle = rs.getString("cle");
            final String memo = rs.getString("memo");
            MEMOS.put(cle, memo);
        }
        Utils.log(Db.class, "MemoDB loaded");
    }

    /**
     * @throws Exception
     *             en cas de foirage.
     */
    public static void loadPlayerDB() throws Exception {
        final ResultSet rs = doSqlQuery("SELECT `steamid`, `qauth`, `adminlvl` FROM `players`");
        while (rs.next()) {
            final String steamid = rs.getString("steamid");
            final String qauth = rs.getString("qauth");
            final int master = rs.getInt("adminlvl");
            PLAYERS.put(steamid, qauth);
            if (master > 0) {
                MASTERS.add(steamid);
            }
        }
        Utils.log(Db.class, "PlayerDB loaded");
    }

    /**
     * @param steamid
     *            le steam_id
     * @param qnetAuth
     *            l'auth Qnet
     * @return un msg
     */
    public static String registerPlayer(final String steamid,
            final String qnetAuth) {
        PLAYERS.put(steamid, qnetAuth);
        try {
            doSqlUpdate("INSERT INTO `players`(`steamid`, `qauth`) VALUES('"
                    + steamid + "', '" + qnetAuth + "')");
            return "Joueur enregistre";
        } catch (final Exception e) {
            Utils.logError(Db.class, e);
            return "Erreur : joueur non enregistre";
        }
    }

    /**
     * @throws Exception
     *             il peut se passer tout un tas de choses...
     */
    public static void reloadTriggers() throws Exception {
        final ResultSet rs = doSqlQuery("SELECT `param` FROM `profil` WHERE `nom` = '"
                + profil + "' AND `paramName` = 'trigger'");
        ATrigger.clearTriggers();
        while (rs.next()) {
            final String param = rs.getString("param");
            final int i = param.indexOf(" ");
            final String className = "ircbot.trigger." + param.substring(0, i);
            final String triggerName = param.substring(i + 1);
            final Class < ? > toLoad = Class.forName(className);
            final Constructor < ? > c = toLoad.getConstructor(String.class);
            c.newInstance(triggerName);
        }
    }

    /**
     * @param cle
     *            la clé
     * @param memo
     *            le mémo à set
     * @return un msg
     */
    public static String setMemo(final String cle, final String memo) {
        String msg = "";
        if (MEMOS.containsKey(cle)) {
            msg = "Memo " + cle + " mis a jour";
        } else {
            msg = "Memo " + cle + " enregistre";
        }
        MEMOS.put(cle, memo);
        try {
            doSqlUpdate("INSERT INTO `memos`(`cle`, `memo`) VALUES('" + cle
                    + "', '" + memo + "')");
            return msg;
        } catch (final Exception e) {
            Utils.logError(Db.class, e);
            return "Erreur, memo non enregistre";
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
     * @param l
     *            La date
     * @param i
     *            indicateur
     * @param j
     *            indicateur
     * @throws SQLException
     *             en cas de galère.
     */
    public static void updateDispo(final String channel, final String qauth,
            final long l, final int i, final int j) throws SQLException {
        final String date = (new java.sql.Date(l)).toString();
        doSqlUpdate("DELETE FROM `dispo` WHERE `channel` = '" + channel
                + "' AND `qauth` = '" + qauth + "' AND `date` = '" + date + "'");
        doSqlUpdate("INSERT INTO `dispo`(`channel`, `qauth`, `date`, `heure1`, `heure2`) VALUES('"
                + channel
                + "', '"
                + qauth
                + "', '"
                + date
                + "', "
                + i
                + ", "
                + j + ")");
    }

    /**
     * constructeur caché.
     */
    private Db() {
        // not for your ears
    }
}
