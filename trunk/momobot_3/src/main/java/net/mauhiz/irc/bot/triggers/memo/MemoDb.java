package net.mauhiz.irc.bot.triggers.memo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mauhiz.irc.HibernateUtils;
import net.mauhiz.irc.base.data.IrcServer;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;
import org.hibernate.Query;

/**
 * @author mauhiz
 */
public class MemoDb {
    private static final Map<IrcServer, MemoDb> DBS = new HashMap<IrcServer, MemoDb>();
    
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(MemoDb.class);
    /**
     * @param server
     * @return
     */
    public static MemoDb getInstance(final IrcServer server) {
        MemoDb db = DBS.get(server);
        if (db == null) {
            db = new MemoDb(server);
            DBS.put(server, db);
        }
        return db;
    }
    IrcServer server;
    
    /**
     * @param server1
     */
    public MemoDb(final IrcServer server1) {
        server = server1;
    }
    
    /**
     * @return le nbre de memos enregistres par rapport a ce serveur
     */
    public int countMemos() {
        Query countQry = HibernateUtils.currentSession().createQuery(
                "select count(1) from Memo where serverAlias = :serverAlias");
        countQry.setString("serverAlias", server.getAlias());
        return (Integer) countQry.uniqueResult();
    }
    
    /**
     * @param key
     *            la clé
     * @return le mémo associé
     */
    public String getMemo(final String key) {
        String result = getValue(key);
        if (result == null) {
            return "Pas de mémo pour " + key;
        }
        return result;
    }
    
    /**
     * @return la liste des memos
     */
    public String getMemos() {
        final StrBuilder msg = new StrBuilder("mémos :");
        Query getQry = HibernateUtils.currentSession().createQuery(
                "select value from Memo where serverAlias = :serverAlias");
        getQry.setString("serverAlias", server.getAlias());
        List<String> results = getQry.list();
        for (final String result : results) {
            msg.append(' ').append(result);
        }
        return msg.toString();
    }
    
    /**
     * @param key
     *            la clé
     * @return le mémo associé
     */
    public String getValue(final String key) {
        Query getQry = HibernateUtils.currentSession().createQuery(
                "select value from Memo where serverAlias = :serverAlias and key = :key");
        getQry.setString("serverAlias", server.getAlias());
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
        final StrBuilder msg = new StrBuilder("Mémo ").append(key);
        if (oldValue == null) {
            Memo memo = new Memo();
            memo.setServerAlias(server.getAlias());
            memo.setKey(key);
            memo.setValue(value);
            HibernateUtils.currentSession().save(memo);
            msg.append(" enregistré");
        } else {
            /* TODO mise a jour */
            msg.append(" mis à jour");
        }
        return msg.toString();
        // return "Erreur, mémo non enregistré";
    }
}
