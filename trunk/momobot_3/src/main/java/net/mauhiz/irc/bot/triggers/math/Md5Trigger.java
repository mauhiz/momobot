package net.mauhiz.irc.bot.triggers.math;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * Calcule un md5.
 * 
 * @author mauhiz
 */
public class Md5Trigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * 
     */
    static final Logger LOG = Logger.getLogger(Md5Trigger.class);
    
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
     * @param input
     * @return le md5
     * @throws NoSuchAlgorithmException
     */
    static String computeMd5(final byte[] input) throws NoSuchAlgorithmException {
        byte[] output = MessageDigest.getInstance("MD5").digest(input);
        StrBuilder sb = new StrBuilder();
        for (byte b : output) {
            sb.append(byteToString(b));
        }
        return sb.toString();
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
            try {
                resp = "md5 de " + args + ": " + computeMd5(args.getBytes());
            } catch (final NoSuchAlgorithmException nsae) {
                resp = "J'ai pas de md5. Sry.";
            }
        }
        Privmsg msg = Privmsg.buildAnswer(cme, resp);
        control.sendMsg(msg);
    }
}
