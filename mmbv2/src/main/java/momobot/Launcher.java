package momobot;

import java.sql.SQLException;

import momobot.memo.DbMemoUtils;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Launcher {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(Launcher.class);

    /**
     * Lance le bot.
     * @param args
     *            les arguments
     * @throws SQLException
     *             exception SQL
     */
    public static void main(final String... args) throws SQLException {
        /* init */
        if (ArrayUtils.isEmpty(args)) {
            LOG.fatal("Syntaxe : " + Launcher.class.getName() + " profil");
            return;
        }
        DbUtils.loadDriver("com.mysql.jdbc.Driver");
        SqlUtils.loadPlayerDB();
        DbMemoUtils.loadMemoDB();
        SqlUtils.loadMomoBot(args[0]);
        // Server s = new Server(new InetSocketAddress("203.26.94.233", 27015));
        // s.getDetails();
    }

    /**
     * Constructeur caché.
     */
    protected Launcher() {
        throw new UnsupportedOperationException();
    }
}
