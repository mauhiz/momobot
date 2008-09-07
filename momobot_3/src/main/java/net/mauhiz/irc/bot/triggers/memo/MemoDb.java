package net.mauhiz.irc.bot.triggers.memo;

import java.util.List;

import net.mauhiz.irc.HibernateUtils;
import net.mauhiz.irc.base.data.AbstractHook;
import net.mauhiz.irc.base.data.IrcServer;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

/**
 * @author mauhiz
 */
public class MemoDb extends AbstractHook<IrcServer> {
    /**
     * @param server
     * @return instance
     */
    public static MemoDb getInstance(final IrcServer server) {
        MemoDb ret = server.getHookedObject(MemoDb.class);
        if (ret == null) {
            return new MemoDb(server);
        }
        return ret;
    }
    
    /**
     * @param server1
     */
    public MemoDb(final IrcServer server1) {
        super(server1);
    }
    
    /**
     * @return le nbre de memos enregistres par rapport a ce serveur
     */
    public int countMemos() {
        Query countQry = HibernateUtils.currentSession().createQuery(
                "select count(1) from Memo where serverAlias = :serverAlias");
        countQry.setString("server.getAlias()", hook.getAlias());
        Integer uniqueResult = (Integer) countQry.uniqueResult();
        if (uniqueResult == null) {
            return 0;
        }
        return uniqueResult.intValue();
    }
    
    /**
     * @param key
     *            la clé
     * @return le mémo associé
     */
    public String getMemo(final String key) {
        String result = getValue(key);
        if (result == null) {
            return "Pas de mémo pour '" + key + "'";
        }
        return result;
    }
    
    /**
     * @return la liste des memos
     */
    public String getMemos() {
        Query getQry = HibernateUtils.currentSession().createQuery(
                "select key from Memo where serverAlias = :serverAlias");
        getQry.setString("serverAlias", hook.getAlias());
        List<String> results = getQry.list();
        if (results.isEmpty()) {
            return "Pas de memo pour l'instant";
        }
        
        return "mémos : " + StringUtils.join(results, ' ');
    }
    
    /**
     * @param key
     *            la clé
     * @return le mémo associé
     */
    public String getValue(final String key) {
        Query getQry = HibernateUtils.currentSession().createQuery(
                "select value from Memo where serverAlias = :serverAlias and key = :key");
        getQry.setString("serverAlias", hook.getAlias());
        getQry.setString("key", key);
        return (String) getQry.uniqueResult();
    }
    
    /**
     * @param key
     *            la clé
     * @param value
     *            le mémo à set
     * @return un msg
     */
    public String setMemo(final String key, final String value) {
        String oldValue = getValue(key);
        final StringBuilder msg = new StringBuilder("Mémo '").append(key).append("' ");
        HibernateUtils.currentSession().getTransaction().begin();
        if (oldValue == null) {
            Memo memo = new Memo();
            memo.setServerAlias(hook.getAlias());
            memo.setKey(key);
            memo.setValue(value);
            HibernateUtils.currentSession().save(memo);
            msg.append("enregistré");
        } else {
            /* TODO mise a jour */
            Query getQry = HibernateUtils.currentSession().createQuery(
                    "update Memo set value = :value where serverAlias = :serverAlias and key = :key");
            getQry.setString("serverAlias", hook.getAlias());
            getQry.setString("key", key);
            getQry.setString("value", value);
            int updated = getQry.executeUpdate();
            if (updated == 1) {
                msg.append("mis à jour");
            } else {
                HibernateUtils.currentSession().getTransaction().rollback();
                return "Erreur, mémo non enregistré";
            }
        }
        
        HibernateUtils.currentSession().getTransaction().commit();
        return msg.toString();
        
    }
}
