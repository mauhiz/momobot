package net.mauhiz.util;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Une session par thread
 * 
 * @author mauhiz
 */
public class HibernateUtils {
    static class KeepAlive extends AbstractRunnable {
        private static final long SLEEP = DateUtils.MILLIS_PER_HOUR;
        private final Session session;
        
        public KeepAlive(Session s) {
            super();
            session = s;
        }
        
        @Override
        public void run() {
            while (!SESSION_FACTORY.isClosed() && session.isConnected() && isRunning()) {
                sleep(SLEEP);
                
                // FIXME fonctionne pour MySQL mais pas pour Oracle par ex. trouver un keepalive generique.
                session.createSQLQuery("SELECT 1").uniqueResult();
            }
        }
        
    }
    
    /**
     * session
     */
    public static final ThreadLocal<Session> SESSION = new ThreadLocal<Session>();
    
    /**
     * session factory
     */
    protected static final SessionFactory SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
    
    /**
     * closes Hibernate session
     */
    public static void closeSession() {
        Session s = SESSION.get();
        SESSION.set(null);
        if (s != null) {
            s.close();
        }
    }
    
    /**
     * creates session if not active.
     * 
     * @return session
     */
    public static Session currentSession() {
        Session s = SESSION.get();
        if (s == null) {
            s = SESSION_FACTORY.openSession();
            new KeepAlive(s).startAs("SQL Connection keep-alive");
            SESSION.set(s);
        }
        return s;
    }
}
