package net.mauhiz.irc.bot.triggers.memo;

import net.mauhiz.util.HibernateUtils;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 * 
 */
public class MemoTest {
    @Test
    public void testMemo() {
        // TODO setup test storage
        Session session = HibernateUtils.currentSession();
        Memo memo0 = new Memo();
        memo0.setKey("haroun");
        memo0.setValue("tazieff");
        memo0.setServerAlias("irc.quakenet.org");
        session.save(memo0);
        long id0 = memo0.getId();

        Query qry2 = session.createQuery("from Memo where key = :key and serverAlias = :serverAlias");
        qry2.setString("key", "haroun");
        qry2.setString("serverAlias", "irc.quakenet.org");
        Memo memo2 = (Memo) qry2.uniqueResult();
        Assert.assertEquals("tazieff", memo2.getValue());

        Memo memo3 = (Memo) session.load(Memo.class, Long.valueOf(id0));
        Assert.assertEquals("tazieff", memo3.getValue());
    }

}
