package net.mauhiz.irc.bot.triggers.dispo;

import java.util.List;

import net.mauhiz.irc.HibernateUtils;
import net.mauhiz.irc.base.data.AbstractHook;
import net.mauhiz.irc.base.data.IrcServer;

import org.hibernate.Query;

/**
 * @author mauhiz
 */
public class DispoDb extends AbstractHook<IrcServer> {
    
    /**
     * @param server
     * @return instance
     */
    public static DispoDb getInstance(final IrcServer server) {
        DispoDb ret = server.getHookedObject(DispoDb.class);
        if (ret == null) {
            return new DispoDb(server);
        }
        return ret;
    }
    
    /**
     * @param dispo
     */
    public static void updateDispo(final Dispo dispo) {
        HibernateUtils.currentSession().getTransaction().begin();
        Query getQry = HibernateUtils
                .currentSession()
                .createQuery(
                        "delete from Dispo where channel = :channel and qauth = :qauth and serverAlias = :serverAlias and when = :when");
        getQry.setString("channel", dispo.getChannel());
        getQry.setDate("when", dispo.getWhen());
        getQry.setString("qauth", dispo.getQauth());
        getQry.setString("serverAlias", dispo.getServerAlias());
        getQry.executeUpdate();
        HibernateUtils.currentSession().save(dispo);
        HibernateUtils.currentSession().getTransaction().commit();
    }
    
    /**
     * @param server
     */
    public DispoDb(final IrcServer server) {
        super(server);
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
        getQry.setString("serverAlias", hook.getAlias());
        return getQry.list();
    }
    
}
