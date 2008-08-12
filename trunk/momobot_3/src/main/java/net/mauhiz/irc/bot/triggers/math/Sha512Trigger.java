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
 * Calcule un sha-512.
 * 
 * @author mauhiz
 */
public class Sha512Trigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * 
     */
    static final Logger LOG = Logger.getLogger(Sha512Trigger.class);
    
    /**
     * @param input
     * @return le md5
     * @throws NoSuchAlgorithmException
     */
    static String computeSha512(final byte[] input) throws NoSuchAlgorithmException {
        byte[] output = MessageDigest.getInstance("SHA-512").digest(input);
        StrBuilder sb = new StrBuilder();
        for (byte b : output) {
            sb.append(Md5Trigger.byteToString(b));
        }
        return sb.toString();
    }
    
    /**
     * @param trigger
     */
    public Sha512Trigger(final String trigger) {
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
        Privmsg resp;
        if (StringUtils.isEmpty(args)) {
            resp = Privmsg.buildAnswer(cme, "sha-512 de quoi ?");
        } else {
            try {
                resp = Privmsg.buildAnswer(cme, "sha-512 de " + args + ": " + computeSha512(args.getBytes()));
            } catch (final NoSuchAlgorithmException nsae) {
                resp = Privmsg.buildAnswer(cme, "J'ai pas de sha-512. Sry.");
            }
        }
        control.sendMsg(resp);
    }
}
