package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Mask;

/**
 * @author mauhiz
 */
public class Action extends Privmsg implements IrcSpecialChars {
    
    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static Action buildAnswer(IIrcMessage toReply, String msg) {
        String oldDest = toReply.getTo();
        if (MomoStringUtils.isChannelName(oldDest)) {
            return new Action(null, oldDest, toReply.getServer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }
    
    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static Action buildPrivateAnswer(IIrcMessage toReply, String msg) {
        Mask from = new Mask(toReply.getFrom());
        return new Action(null, from.getNick(), toReply.getServer(), msg);
    }
    
    /**
     * @param from1
     * @param to1
     * @param server1
     * @param message1
     */
    public Action(String from1, String to1, IrcServer server1, String message1) {
        super(from1, to1, server1, message1);
    }
    
    /**
     * @see net.mauhiz.irc.base.msg.Privmsg#getMessage()
     */
    @Override
    public String getMessage() {
        return QUOTE_STX + "ACTION " + super.getMessage() + QUOTE_STX;
    }
}
