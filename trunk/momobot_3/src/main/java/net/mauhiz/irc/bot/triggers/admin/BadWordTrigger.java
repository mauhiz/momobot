package net.mauhiz.irc.bot.triggers.admin;

import java.util.Set;
import java.util.TreeSet;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.IrcMessage;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;

/**
 * @author mauhiz
 */
public class BadWordTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * TODO utiliser SQL.
     */
    private static final Set<String> BADWORDS = new TreeSet<String>();
    static {
        BADWORDS.add("caca");
        BADWORDS.add("prout");
    }
    
    /**
     * @param trigger
     */
    public BadWordTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        if (im.getMessage().startsWith(toString())) {
            /* mode controle */
            /* TODO admin mode */
            String args = getArgs(im.getMessage());
            Privmsg resp;
            if (StringUtils.isEmpty(args)) {
                resp = Privmsg.buildPrivateAnswer(im, "Syntaxe: " + toString() + " badword");
            } else {
                BADWORDS.add(args);
                resp = Privmsg.buildPrivateAnswer(im, "Badword ajouté: " + args);
            }
            control.sendMsg(resp);
        } else {
            for (final String word : new StrTokenizer(im.getMessage()).getTokenArray()) {
                for (final String badword : BADWORDS) {
                    if (badword.equalsIgnoreCase(word)) {
                        getAngry(im, control, badword);
                    }
                }
            }
        }
        
    }
    
    /**
     * @param toReply
     * @param control
     * @param badword
     */
    private void getAngry(final IrcMessage toReply, final IIrcControl control, final String badword) {
        IIrcMessage resp;
        if (true /* TODO verifier que je suis OP */) {
            /* hinhin, je suis op */
            resp = new Kick(toReply.getServer(), null, null, toReply.getTo(), toReply.getFrom(),
                    "You shall not use bad words such as " + badword);
        } else {
            /* je rage quand même */
            resp = Privmsg.buildAnswer(toReply, toReply.getFrom() + " is so rude, he said " + badword + "!!");
        }
        control.sendMsg(resp);
    }
}
