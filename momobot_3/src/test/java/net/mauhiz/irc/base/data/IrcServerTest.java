package net.mauhiz.irc.base.data;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Mode;
import net.mauhiz.irc.base.msg.Notice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class IrcServerTest {
    IrcServer server;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.server = new IrcServer("irc://irc.quakenet.org:6667/");
        this.server.setMyNick("momobot3");
        this.server.setMyLogin("mmb");
        this.server.setMyFullName("MMB v3");
        this.server.setAlias("QuakeNet");
    }

    /**
     * Test method for {@link net.mauhiz.irc.base.data.IrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testBuildFromRaw1() {
        String test = ":port80a.se.quakenet.org NOTICE momobot3 :on 4 ca 1(4) ft 20(20) tr";
        IIrcMessage msg = this.server.buildFromRaw(test);
        Assert.assertTrue(msg instanceof Notice);
        Notice notice = (Notice) msg;
        Assert.assertEquals("port80a.se.quakenet.org", notice.getFrom());
        Assert.assertEquals("momobot3", notice.getTo());
        Assert.assertEquals("on 4 ca 1(4) ft 20(20) tr", notice.getMessage());
    }

    /**
     * Test method for {@link net.mauhiz.irc.base.data.IrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testBuildFromRaw2() {
        String test = ":momobot3!~mmb@40.178.119-80.rev.gaoland.net MODE momobot3 +i";
        IIrcMessage msg = this.server.buildFromRaw(test);
        Assert.assertTrue(msg instanceof Mode);
        Mode mode = (Mode) msg;
        Assert.assertEquals("momobot3!~mmb@40.178.119-80.rev.gaoland.net", mode.getFrom());
        Assert.assertEquals("momobot3", mode.getTo());
        Assert.assertEquals("+i", mode.getMessage());
    }
}
