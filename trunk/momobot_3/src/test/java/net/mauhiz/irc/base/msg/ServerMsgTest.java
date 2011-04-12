package net.mauhiz.irc.base.msg;

import junit.framework.Assert;
import net.mauhiz.irc.AbstractServerTest;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetUser;

import org.junit.Test;

public class ServerMsgTest extends AbstractServerTest {

    /**
     * :port80b.se.quakenet.org 311 momobot3 Krauser2 ~mauhiz 195.255.97-84.rev.gaoland.net * :currently baving _@/'
     */
    @Test
    public void testProcess311() {
        ServerMsg msg = (ServerMsg) QNET
                .buildFromRaw(":port80b.se.quakenet.org 311 momobot3 Krauser2 ~mauhiz 195.255.97-84.rev.gaoland.net * :currently baving _@/'");
        msg.process(null);
        IrcUser user = QNET.findUser("Krauser2", false);
        Assert.assertEquals("~mauhiz", user.getMask().getUser());
        Assert.assertEquals("195.255.97-84.rev.gaoland.net", user.getMask().getHost());
        Assert.assertEquals("currently baving _@/'", user.getFullName());
    }

    /**
     * :port80b.se.quakenet.org 319 momobot3 Krauser2 :@#cos_squad @#eule #pitinours +#-wav- +#-hp- +#eloquence @#tsi.fr
     */
    @Test
    public void testProcess319() {
        ServerMsg msg = (ServerMsg) QNET
                .buildFromRaw(":port80b.se.quakenet.org 319 momobot3 Krauser2 :@#cos_squad @#eule #pitinours +#-wav- +#-hp- +#eloquence @#tsi.fr");
        IrcChannel chan = QNET.findChannel("#cos_squad", true);
        msg.process(null);
        IrcUser user = QNET.findUser("Krauser2", false);
        Assert.assertTrue(chan.contains(user));
    }

    /**
     * :port80b.se.quakenet.org 330 momobot3 Krauser2 mauhiz :is authed as
     */
    @Test
    public void testProcess330() {
        ServerMsg msg = (ServerMsg) QNET
                .buildFromRaw(":port80b.se.quakenet.org 330 momobot3 Krauser2 mauhiz :is authed as");
        msg.process(null);
        QnetUser user = (QnetUser) QNET.findUser("Krauser2", false);
        Assert.assertEquals("mauhiz", user.getAuth());
    }
}
