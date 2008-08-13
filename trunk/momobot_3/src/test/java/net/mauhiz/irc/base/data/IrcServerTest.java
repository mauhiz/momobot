package net.mauhiz.irc.base.data;

import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Mode;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.ServerMsg;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class IrcServerTest {
    /**
     * mon serveur de test.
     */
    IrcServer server;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        server = new IrcServer("irc://irc.quakenet.org:6667/");
        server.setMyNick("momobot3");
        server.setMyLogin("mmb");
        server.setMyFullName("MMB v3");
        server.setAlias("QuakeNet");
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.base.data.IrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testBuildFromRaw1() {
        String test = ":port80a.se.quakenet.org NOTICE momobot3 :on 4 ca 1(4) ft 20(20) tr";
        IIrcMessage msg = server.buildFromRaw(test);
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
        IIrcMessage msg = server.buildFromRaw(test);
        Assert.assertTrue(msg instanceof Mode);
        Mode mode = (Mode) msg;
        Assert.assertEquals("momobot3!~mmb@40.178.119-80.rev.gaoland.net", mode.getFrom());
        Assert.assertEquals("momobot3", mode.getTo());
        Assert.assertEquals("+i", mode.getMessage());
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.base.data.IrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testBuildFromRaw3() {
        String test = ":HP|Angie!~mauhiz@86.255.97-84.rev.gaoland.net JOIN #truite";
        IIrcMessage msg = server.buildFromRaw(test);
        Assert.assertTrue(msg instanceof Join);
        Join join = (Join) msg;
        Assert.assertEquals("HP|Angie!~mauhiz@86.255.97-84.rev.gaoland.net", join.getFrom());
        Assert.assertEquals("#truite", join.getChan());
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.base.data.IrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testBuildFromRaw4() {
        String test = ":port80b.se.quakenet.org 353 HP|Angie = #truite :@HP|Angie";
        IIrcMessage msg = server.buildFromRaw(test);
        Assert.assertTrue(msg instanceof ServerMsg);
        ServerMsg smsg = (ServerMsg) msg;
        Assert.assertEquals("= #truite :@HP|Angie", smsg.getMsg());
        Assert.assertEquals(353, smsg.getCode());
    }
    
    /**
     * Test method for {@link net.mauhiz.irc.base.data.IrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testBuildFromRaw5() {
        String test = ":port80b.se.quakenet.org 366 HP|Angie #truite :End of /NAMES list.";
        IIrcMessage msg = server.buildFromRaw(test);
        Assert.assertTrue(msg instanceof ServerMsg);
        ServerMsg smsg = (ServerMsg) msg;
        Assert.assertEquals("#truite :End of /NAMES list.", smsg.getMsg());
        Assert.assertEquals(366, smsg.getCode());
    }
}
