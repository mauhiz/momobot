package net.mauhiz.irc.bot.triggers.math;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.util.FileUtil;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

/**
 * Calcule un sha-256.
 * 
 * @author mauhiz
 */
public class Sha256Trigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    
    /**
     * @param input
     * @return le md5
     * @throws NoSuchAlgorithmException
     */
    static String computeSha256(byte[] input) throws NoSuchAlgorithmException {
        byte[] output = MessageDigest.getInstance("SHA-256").digest(input);
        return new String(Hex.encodeHex(output));
    }
    
    /**
     * @param trigger
     */
    public Sha256Trigger(String trigger) {
        super(trigger);
    }
    
    /**
     * MessageDigest
     * 
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg cme, IIrcControl control) {
        String args = getArgs(cme.getMessage());
        Privmsg resp;
        if (StringUtils.isEmpty(args)) {
            resp = Privmsg.buildAnswer(cme, "sha-256 de quoi ?");
        } else {
            try {
                resp = Privmsg.buildAnswer(cme,
                        "sha-256 de " + args + ": " + computeSha256(args.getBytes(FileUtil.ISO8859_15)));
            } catch (NoSuchAlgorithmException nsae) {
                resp = Privmsg.buildAnswer(cme, "J'ai pas de sha-256. Sry.");
            }
        }
        control.sendMsg(resp);
    }
}
