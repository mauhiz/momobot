package net.mauhiz.irc.bot.triggers.math;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.IrcMessage;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.text.StrTokenizer;

/**
 * @author mauhiz
 */
public class BaseConvTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param toReply
     * @param control
     * @param base
     * @return si la base est acceptable
     */
    private static boolean checkBase(final IrcMessage toReply, final IIrcControl control, final int base) {
        if (base < Character.MIN_RADIX) {
            Notice not = Notice.buildPrivateAnswer(toReply, "La base " + base + " est trop petite");
            control.sendMsg(not);
            return false;
        } else if (base > Character.MAX_RADIX) {
            return false;
        }
        return true;
    }
    
    /**
     * @param trigger
     */
    public BaseConvTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        final String args = getArgs(cme.getMessage());
        if (args.trim().isEmpty()) {
            showUsage(control, cme);
            return;
        }
        final StrTokenizer tok = new StrTokenizer(args);
        final String nombre = tok.nextToken();
        final int base1 = Integer.parseInt(tok.nextToken());
        if (!checkBase(cme, control, base1)) {
            return;
        }
        IIrcMessage resp;
        try {
            final int intNombre = Integer.parseInt(nombre, base1);
            final int base2 = Integer.parseInt(tok.nextToken());
            if (!checkBase(cme, control, base2)) {
                return;
            }
            final String resultat = Integer.toString(intNombre, base2);
            resp = Privmsg.buildAnswer(cme, nombre + " en base " + base1 + " = " + resultat + " en base " + base2);
        } catch (final NumberFormatException nfe) {
            resp = Notice.buildPrivateAnswer(cme, "Le nombre " + nombre + " est illisible en base " + base1);
        }
        control.sendMsg(resp);
    }
    
    /**
     * @param control
     * @param toReply
     */
    private void showUsage(final IIrcControl control, final IrcMessage toReply) {
        Notice notice = Notice.buildPrivateAnswer(toReply, "Syntaxe : " + toString() + " nombre base_from base_to");
        control.sendMsg(notice);
        notice = Notice.buildPrivateAnswer(toReply, "Exemple : " + toString() + " ff 16 10");
        control.sendMsg(notice);
    }
}