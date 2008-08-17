package net.mauhiz.irc.bot.triggers.fun;

import net.mauhiz.irc.base.Color;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
public class Q3NickTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, IrcSpecialChars {
    /**
     * TODO finir la methode.
     * 
     * @param colorCode
     * @return le code couleur q3
     */
    private static String computeQ3ColorCode(final String colorCode) {
        final String q3Code;
        if (Color.BLACK.toString().equals(colorCode)) {
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
        final StrBuilder colorCode = new StrBuilder();
        int inColor = 0;
        for (char c : args.toCharArray()) {
            if (c == DELIM_COLOR) {
                inColor = 1;
                continue;
            } else if (inColor == 1) {
                colorCode.clear();
                if (Character.isDigit(c)) {
                    colorCode.append(c);
                    ++inColor;
                } else {
                    inColor = 0;
                }
            } else if (inColor == 2) {
                if (Character.isDigit(c)) {
                    colorCode.append(c);
                }
                q3nick.append(computeQ3ColorCode(colorCode.toString()));
            } else {
                q3nick.append(c);
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
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        final Privmsg msg = Privmsg.buildAnswer(im, createq3nick(getArgs(im.getMessage())));
        control.sendMsg(msg);
    }
}
