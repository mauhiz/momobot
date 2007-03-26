package momobot;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 */
public final class Main {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * @param args
     *            les arguments
     */
    public static void main(final String[] args) {
        /* init */
        try {
            DbUtils.loadDriver("com.mysql.jdbc.Driver");
            Db.loadPlayerDB();
            Db.loadMemoDB();
            Db.loadMomoBot(args[0]);
        } catch (final Exception e) {
            LOG.fatal(e, e);
        }
        // Server s = new Server(new InetSocketAddress("203.26.94.233", 27015));
        // s.getDetails();
    }

    /**
     * Constructeur caché.
     */
    protected Main() {
        throw new UnsupportedOperationException();
    }
}
