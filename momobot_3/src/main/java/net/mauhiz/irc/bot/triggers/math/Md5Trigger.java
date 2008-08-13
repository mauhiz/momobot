package net.mauhiz.irc.bot.triggers.math;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Calcule un md5.
 * 
 * @author mauhiz
 */
public class Md5Trigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    
    /**
     * @param b
     * @return un affichage hexa du byte non signe
     */
    static String byteToString(final byte b) {
        String out = Integer.toHexString(b);
        if (out.length() > 2) {
            return out.substring(6);
        } else if (out.length() == 1) {
            return "0" + out;
        }
        return out;
    }
    
    /**
     * @param trigger
     */
    public Md5Trigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * MessageDigest
     * 
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        final String args = getArgs(cme.getMessage());
        String resp;
        if (StringUtils.isEmpty(args)) {
            resp = "md5 de quoi ?";
        } else {
            resp = "md5 de " + args + ": " + DigestUtils.md5Hex(args);
        }
        Privmsg msg = Privmsg.buildAnswer(cme, resp);
        control.sendMsg(msg);
    }
}
