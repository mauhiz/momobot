package net.mauhiz.irc.bot.triggers.memo;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;
import net.mauhiz.irc.HibernateUtils;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class MemoTest {
    /**
     * @throws SQLException
     */
    @Test
    public void testMemo() throws SQLException {
        Session session = HibernateUtils.currentSession();
        Memo memo0 = new Memo();
        memo0.setKey("haroun");
        memo0.setValue("tazieff");
        memo0.setServerAlias("quakenet");
        session.save(memo0);
        long id0 = memo0.getId();
        
        Query qry1 = session.getNamedQuery("getMemo");
        qry1.setString("key", "haroun");
        qry1.setString("serverAlias", "quakenet");
        List results = qry1.list();
        System.out.println(results);
        
        Query qry2 = session.createQuery("from Memo where key = :key and serverAlias = :serverAlias");
        qry2.setString("key", "haroun");
        qry2.setString("serverAlias", "quakenet");
        Memo memo2 = (Memo) qry2.uniqueResult();
        Assert.assertEquals("tazieff", memo2.getValue());
        
        Memo memo3 = (Memo) session.load(Memo.class, Long.valueOf(id0));
        Assert.assertEquals("tazieff", memo3.getValue());
        
    }
    
}