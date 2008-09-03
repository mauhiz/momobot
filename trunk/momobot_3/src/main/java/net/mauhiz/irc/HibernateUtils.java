package net.mauhiz.irc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Une session par thread
 * 
 * @author mauhiz
 */
public class HibernateUtils {
    /**
     * session
     */
    public static final ThreadLocal<Session> SESSION = new ThreadLocal<Session>();
    /**
     * session factory
     */
    private static final SessionFactory SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
    
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
            SESSION.set(s);
        }
        return s;
    }
    
}
