package momobot.trigger.fun;

import ircbot.Channel;
import ircbot.ColorsUtils;
import ircbot.IIrcSpecialChars;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
public class Q3NickTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * TODO finir la methode.
     * 
     * @param colorCode
     * @return le code couleur q3
     */
    private static String computeQ3ColorCode(final String colorCode) {
        final String q3Code;
        if (colorCode == ColorsUtils.BLACK) {
            q3Code = "0";
        } else {
            return "";
        }
        return "^" + q3Code;
    }

    /**
     * @param args
     * @return un nick q3
     */
    private static String createq3nick(final String args) {
        final StrBuilder q3nick = new StrBuilder();
        String colorCode = "";
        int inColor = 0;
        for (int i = 0; i < args.length(); ++i) {
            if (args.charAt(i) == IIrcSpecialChars.COLOR) {
                inColor = 1;
                continue;
            } else if (inColor == 1) {
                if (Character.isDigit(args.charAt(i))) {
                    colorCode = "" + args.charAt(i);
                    ++inColor;
                } else {
                    inColor = 0;
                    colorCode = "";
                }
            } else if (inColor == 2) {
                if (Character.isDigit(args.charAt(i))) {
                    colorCode = colorCode + args.charAt(i);
                }
                q3nick.append(computeQ3ColorCode(colorCode));
            } else {
                q3nick.append(args.charAt(i));
            }
        }
        return q3nick.toString();
    }

    /**
     * @param trigger
     */
    public Q3NickTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void executePublicTrigger(final IrcUser from, final Channel channel, final String message) {
        MomoBot.getBotInstance().sendMessage(channel, createq3nick(getArgs(message)));
    }
}
