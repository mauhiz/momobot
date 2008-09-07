package net.mauhiz.irc.bot.triggers.dispo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mauhiz.irc.HibernateUtils;
import net.mauhiz.irc.base.data.IrcServer;

import org.apache.log4j.Logger;
import org.hibernate.Query;

/**
 * @author mauhiz
 */
public class DispoDb {
    private static final Map<IrcServer, DispoDb> DBS = new HashMap<IrcServer, DispoDb>();
    
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(DispoDb.class);
    /**
     * @param server
     * @return
     */
    public static DispoDb getInstance(final IrcServer server) {
        DispoDb db = DBS.get(server);
        if (db == null) {
            db = new DispoDb(server);
            DBS.put(server, db);
        }
        return db;
    }
    
    /**
     * @param dispo
     */
    public static void updateDispo(final Dispo dispo) {
        HibernateUtils.currentSession().getTransaction().begin();
        Query getQry = HibernateUtils
                .currentSession()
                .createQuery(
                        "delete from Dispo where channel = :channel and when = :when and qauth = :quath and serverAlias = :serverAlias");
        getQry.setString("channel", dispo.getChannel());
        getQry.setDate("when", dispo.getWhen());
        getQry.setString("qauth", dispo.getQauth());
        getQry.setString("serverAlias", dispo.getServerAlias());
        getQry.executeUpdate();
        HibernateUtils.currentSession().save(dispo);
        HibernateUtils.currentSession().getTransaction().commit();
    }
    
    IrcServer server;
    
    /**
     * @param server1
     */
    public DispoDb(final IrcServer server1) {
        server = server1;
    }
    
    /**
     * @param channel
     * @param when
     * @return list of dispo
     */
    public List<Dispo> getDispo(final String channel, final java.sql.Date when) {
        Query getQry = HibernateUtils.currentSession().createQuery(
                "from Dispo where channel = :channel and when = :when and serverAlias = :serverAlias");
        getQry.setString("channel", channel);
        getQry.setDate("when", when);
        getQry.setString("serverAlias", server.getAlias());
        return getQry.list();
    }
    
}
