package net.mauhiz.irc.bot.triggers.memo;

import java.util.List;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.util.HibernateUtils;
import net.mauhiz.util.Hooks;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

/**
 * @author mauhiz
 */
public class MemoDb {
    /**
     * @param server
     * @return instance
     */
    public static MemoDb getInstance(IrcServer server) {
        MemoDb ret = Hooks.getHook(server, MemoDb.class);
        if (ret == null) {
            return new MemoDb(server);
        }
        return ret;
    }
    
    private final IrcServer hook;
    
    /**
     * @param server1
     */
    public MemoDb(IrcServer server1) {
        Hooks.addHook(server1, this);
        hook = server1;
    }
    
    /**
     * @return le nbre de memos enregistres par rapport a ce serveur
     */
    public int countMemos() {
        Query countQry = HibernateUtils.currentSession().createQuery(
                "select count(*) from Memo where serverAlias = :serverAlias");
        countQry.setString("serverAlias", hook.getAlias());
        Integer uniqueResult = (Integer) countQry.uniqueResult();
        if (uniqueResult == null) {
            return 0;
        }
        return uniqueResult.intValue();
    }
    
    /**
     * @param key
     *            la cle
     * @return le memo associe
     */
    public String getMemo(String key) {
        String result = getValue(key);
        if (result == null) {
            return "Pas de memo pour '" + key + "'";
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
        
        return "memos : " + StringUtils.join(results, ' ');
    }
    
    /**
     * @param key
     *            la cle
     * @return le memo associe
     */
    public String getValue(String key) {
        Query getQry = HibernateUtils.currentSession().createQuery(
                "select value from Memo where serverAlias = :serverAlias and key = :key");
        getQry.setString("serverAlias", hook.getAlias());
        getQry.setString("key", key);
        return (String) getQry.uniqueResult();
    }
    
    /**
     * @param key
     *            la cle
     * @param value
     *            le memo a set
     * @return un msg
     */
    public String setMemo(String key, String value) {
        String oldValue = getValue(key);
        StringBuilder msg = new StringBuilder("Memo '").append(key).append("' ");
        HibernateUtils.currentSession().getTransaction().begin();
        if (oldValue == null) {
            Memo memo = new Memo();
            memo.setServerAlias(hook.getAlias());
            memo.setKey(key);
            memo.setValue(value);
            HibernateUtils.currentSession().save(memo);
            msg.append("enregistre");
        } else {
            /* TODO mise a jour */
            Query getQry = HibernateUtils.currentSession().createQuery(
                    "update Memo set value = :value where serverAlias = :serverAlias and key = :key");
            getQry.setString("serverAlias", hook.getAlias());
            getQry.setString("key", key);
            getQry.setString("value", value);
            int updated = getQry.executeUpdate();
            if (updated == 1) {
                msg.append("mis a jour");
            } else {
                HibernateUtils.currentSession().getTransaction().rollback();
                return "Erreur, memo non enregistre";
            }
        }
        
        HibernateUtils.currentSession().getTransaction().commit();
        return msg.toString();
        
    }
}
