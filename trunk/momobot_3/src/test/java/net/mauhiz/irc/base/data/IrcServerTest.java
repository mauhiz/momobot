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

    @Test
    public void testBuildFromRaw1() {
        String test = ":port80a.se.quakenet.org NOTICE momobot3 :on 4 ca 1(4) ft 20(20) tr";
        IIrcMessage msg = DECODER.buildFromRaw(TO_QNET, test);
        Assert.assertTrue(msg instanceof Notice);
        Notice notice = (Notice) msg;
        Assert.assertTrue(notice.getFrom() instanceof IIrcServerPeer);
        Assert.assertEquals("port80a.se.quakenet.org", notice.getFrom().getIrcForm());
        Assert.assertTrue(notice.getTo() instanceof IrcUser);
        Assert.assertEquals("momobot3", notice.getTo().toString());
        Assert.assertEquals("on 4 ca 1(4) ft 20(20) tr", notice.getMessage());
    }

    @Test
    public void testBuildFromRaw5() {
        String test = ":port80b.se.quakenet.org 366 HP|Angie #truite :End of /NAMES list.";
        IIrcMessage msg = DECODER.buildFromRaw(TO_QNET, test);
        Assert.assertTrue(msg instanceof ServerMsg);
        ServerMsg smsg = (ServerMsg) msg;
        Assert.assertEquals("End of /NAMES list.", smsg.getMsg());
        Assert.assertEquals(NumericReplies.RPL_ENDOFNAMES.getCode(), smsg.getCode());
    }

    @Test
    public void testInvisible() {
        String test = ":momobot3!~mmb@40.178.119-80.rev.gaoland.net MODE momobot3 +i";
        IIrcMessage msg = DECODER.buildFromRaw(TO_QNET, test);
        Assert.assertTrue(msg instanceof Mode);
        Mode mode = (Mode) msg;
        Assert.assertEquals("momobot3!~mmb@40.178.119-80.rev.gaoland.net", mode.getFrom().getIrcForm());
        Assert.assertEquals("momobot3", mode.getModifiedObject().toString());
        Assert.assertEquals("+i", mode.getArgs().peek());
    }

    @Test
    public void testJoin() {
        String test = ":HP|Angie!~mauhiz@86.255.97-84.rev.gaoland.net JOIN #truite";
        IIrcMessage msg = DECODER.buildFromRaw(TO_QNET, test);
        Assert.assertTrue(msg instanceof Join);
        Join join = (Join) msg;
        Assert.assertEquals("HP|Angie!~mauhiz@86.255.97-84.rev.gaoland.net", join.getFrom().getIrcForm());
        Assert.assertEquals("#truite", join.getChans()[0].getIrcForm());
    }

    @Test
    public void testKick() {
        String test = ":mauhiz!~mauhiz@86.255.97-84.rev.gaoland.net KICK #truite momobot3 :go away";
        IIrcMessage msg = DECODER.buildFromRaw(TO_QNET, test);
        Assert.assertTrue(msg instanceof Kick);
        Kick kick = (Kick) msg;
        Assert.assertEquals("#truite", kick.getChan().getIrcForm());
        Assert.assertEquals("momobot3", kick.getTarget().toString());
        Assert.assertEquals("go away", kick.getReason());
    }

    @Test
    public void testNamReply() {
        String test = ":port80b.se.quakenet.org 353 HP|Angie = #truite :@HP|Angie";
        IIrcMessage msg = DECODER.buildFromRaw(TO_QNET, test);
        Assert.assertTrue(msg instanceof ServerMsg);
        ServerMsg smsg = (ServerMsg) msg;
        Assert.assertEquals("@HP|Angie", smsg.getMsg());
        Assert.assertEquals(NumericReplies.RPL_NAMREPLY.getCode(), smsg.getCode());
    }

    @Test
    public void testTopic() {
        String test = ":Krauser2!~mauhiz@254.255.97-84.rev.gaoland.net TOPIC #truite :lol @ youtube!!";
        IIrcMessage msg = DECODER.buildFromRaw(TO_QNET, test);
        Assert.assertTrue(msg instanceof SetTopic);
        SetTopic topic = (SetTopic) msg;
        Assert.assertEquals("#truite", topic.getChan().toString());
        Assert.assertEquals("lol @ youtube!!", topic.getTopic());
    }

}
