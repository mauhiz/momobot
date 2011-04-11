package net.mauhiz.util;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class KeepAlive extends AbstractRunnable {
    private static final long SLEEP = DateUtils.MILLIS_PER_HOUR;
    private final Session session;
    private final SessionFactory sessionFactory;

    public KeepAlive(SessionFactory sessionFactory) {
        super();
        this.sessionFactory = sessionFactory;
        session = sessionFactory.openSession();
    }

    public Session getSession() {
        return session;
    }

    @Override
    public boolean isRunning() {
        return super.isRunning() && !sessionFactory.isClosed() && session.isConnected();
    }

    @Override
    public void run() {
        while (isRunning()) {
            sleep(SLEEP);

            // FIXME fonctionne pour MySQL mais pas pour Oracle par ex. trouver un keepalive generique.
            session.createSQLQuery("SELECT 1").uniqueResult();
        }
    }

}