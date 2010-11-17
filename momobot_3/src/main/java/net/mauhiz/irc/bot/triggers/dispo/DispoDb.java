package net.mauhiz.irc.bot.triggers.dispo;

import java.util.Date;
import java.util.List;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.util.HibernateUtils;
import net.mauhiz.util.Hooks;

import org.hibernate.Query;

/**
 * @author mauhiz
 */
public final class DispoDb {
    
    /**
     * @param server
     * @return instance
     */
    public static DispoDb getInstance(IrcServer server) {
        DispoDb ret = Hooks.getHook(server, DispoDb.class);
        if (ret == null) {
            return new DispoDb(server);
        }
        return ret;
    }
    
    /**
     * @param dispo
     */
    public static void updateDispo(Dispo dispo) {
        HibernateUtils.currentSession().getTransaction().begin();
        Query getQry = HibernateUtils
                .currentSession()
                .createQuery(
                        "delete from Dispo where channel = :channel and qauth = :qauth and serverAlias = :serverAlias and quand = :quand");
        getQry.setString("channel", dispo.getChannel());
        getQry.setDate("quand", dispo.getQuand());
        getQry.setString("qauth", dispo.getQauth());
        getQry.setString("serverAlias", dispo.getServerAlias());
        getQry.executeUpdate();
        HibernateUtils.currentSession().save(dispo);
        HibernateUtils.currentSession().getTransaction().commit();
    }
    
    private final IrcServer hook;
    
    /**
     * @param server
     */
    private DispoDb(IrcServer server) {
        Hooks.addHook(server, this);
        hook = server;
    }
    
    /**
     * @param channel
     * @param when
     * @return list of dispo
     */
    public List<Dispo> getDispo(String channel, Date when) {
        Query getQry = HibernateUtils.currentSession().createQuery(
                "from Dispo where channel = :channel and quand = :quand and serverAlias = :serverAlias");
        getQry.setString("channel", channel);
        getQry.setDate("quand", when);
        getQry.setString("serverAlias", hook.getAlias());
        return getQry.list();
    }
    
}