package momobot;

import org.apache.commons.dbutils.DbUtils;
import utils.Utils;

/**
 * @author Administrator
 */
public final class Main {
    /**
     * @param args
     *            les arguments
     */
    public static void main(final String[] args) {
        // init
        try {
            DbUtils.loadDriver("com.mysql.jdbc.Driver");
            Db.loadPlayerDB();
            Db.loadMemoDB();
            Db.loadBot(args[0]);
        } catch (final Exception e) {
            Utils.logError(Main.class, e);
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
