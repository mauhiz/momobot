package net.mauhiz.irc.base.data;

import net.mauhiz.irc.AbstractServerTest;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Mode;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.NumericReplies;
import net.mauhiz.irc.base.msg.ServerMsg;
import net.mauhiz.irc.base.msg.SetTopic;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class IrcServerTest extends AbstractServerTest {

    /**
     * Test method for {@link AbstractIrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testBuildFromRaw1() {
        String test = ":port80a.se.quakenet.org NOTICE momobot3 :on 4 ca 1(4) ft 20(20) tr";
        IIrcMessage msg = QNET.buildFromRaw(test);
        Assert.assertTrue(msg instanceof Notice);
        Notice notice = (Notice) msg;
        Assert.assertTrue(notice.getFrom() instanceof IrcServer);
        Assert.assertEquals("port80a.se.quakenet.org", notice.getFrom().getIrcForm());
        Assert.assertTrue(notice.getTo() instanceof IrcUser);
        Assert.assertEquals("momobot3", notice.getTo().getIrcForm());
        Assert.assertEquals("on 4 ca 1(4) ft 20(20) tr", notice.getMessage());
    }

    /**
     * Test method for {@link net.mauhiz.irc.base.data.AbstractIrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testBuildFromRaw5() {
        String test = ":port80b.se.quakenet.org 366 HP|Angie #truite :End of /NAMES list.";
        IIrcMessage msg = QNET.buildFromRaw(test);
        Assert.assertTrue(msg instanceof ServerMsg);
        ServerMsg smsg = (ServerMsg) msg;
        Assert.assertEquals("#truite :End of /NAMES list.", smsg.getMsg());
        Assert.assertEquals(NumericReplies.RPL_ENDOFNAMES.getCode(), smsg.getCode());
    }

    /**
     * Test method for {@link net.mauhiz.irc.base.data.AbstractIrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testInvisible() {
        String test = ":momobot3!~mmb@40.178.119-80.rev.gaoland.net MODE momobot3 +i";
        IIrcMessage msg = QNET.buildFromRaw(test);
        Assert.assertTrue(msg instanceof Mode);
        Mode mode = (Mode) msg;
        Assert.assertEquals("momobot3!~mmb@40.178.119-80.rev.gaoland.net", mode.getFrom().getIrcForm());
        Assert.assertEquals("momobot3", mode.getTo().toString());
        Assert.assertEquals("+i", mode.getMessage());
    }

    /**
     * Test method for {@link net.mauhiz.irc.base.data.AbstractIrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testJoin() {
        String test = ":HP|Angie!~mauhiz@86.255.97-84.rev.gaoland.net JOIN #truite";
        IIrcMessage msg = QNET.buildFromRaw(test);
        Assert.assertTrue(msg instanceof Join);
        Join join = (Join) msg;
        Assert.assertEquals("HP|Angie!~mauhiz@86.255.97-84.rev.gaoland.net", join.getFrom().getIrcForm());
        Assert.assertEquals("#truite", join.getTo().getIrcForm());
    }

    /**
     * Test method for {@link net.mauhiz.irc.base.data.AbstractIrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testKick() {
        String test = ":mauhiz!~mauhiz@86.255.97-84.rev.gaoland.net KICK #truite momobot3 :go away";
        IIrcMessage msg = QNET.buildFromRaw(test);
        Assert.assertTrue(msg instanceof Kick);
        Kick kick = (Kick) msg;
        Assert.assertEquals("#truite", kick.getTo().getIrcForm());
        Assert.assertEquals("momobot3", kick.getTarget().toString());
        Assert.assertEquals("go away", kick.getReason());
    }

    /**
     * Test method for {@link net.mauhiz.irc.base.data.AbstractIrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testNamReply() {
        String test = ":port80b.se.quakenet.org 353 HP|Angie = #truite :@HP|Angie";
        IIrcMessage msg = QNET.buildFromRaw(test);
        Assert.assertTrue(msg instanceof ServerMsg);
        ServerMsg smsg = (ServerMsg) msg;
        Assert.assertEquals("= #truite :@HP|Angie", smsg.getMsg());
        Assert.assertEquals(NumericReplies.RPL_NAMREPLY.getCode(), smsg.getCode());
    }

    /**
     * Test method for {@link net.mauhiz.irc.base.data.AbstractIrcServer#buildFromRaw(java.lang.String)}.
     */
    @Test
    public void testTopic() {
        String test = ":Krauser2!~mauhiz@254.255.97-84.rev.gaoland.net TOPIC #truite :lol @ youtube!!";
        IIrcMessage msg = QNET.buildFromRaw(test);
        Assert.assertTrue(msg instanceof SetTopic);
        SetTopic topic = (SetTopic) msg;
        Assert.assertEquals("#truite", topic.getTo().toString());
        Assert.assertEquals("lol @ youtube!!", topic.getTopic());
    }

}
