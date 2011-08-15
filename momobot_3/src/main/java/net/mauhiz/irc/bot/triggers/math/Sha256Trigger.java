package net.mauhiz.irc.bot.triggers.math;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.util.FileUtil;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

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
     */
    public void doTrigger(Privmsg cme, IIrcControl control) {
        String args = getTriggerContent(cme);

        if (StringUtils.isEmpty(args)) {
            showHelp(control, cme);
        } else {
            Privmsg resp;
            try {
                resp = new Privmsg(cme, "sha-256 de " + args + ": "
                        + computeSha256(FileUtil.getBytes(args, FileUtil.ISO8859_15)));
            } catch (NoSuchAlgorithmException nsae) {
                resp = new Privmsg(cme, "J'ai pas de sha-256. Sry.");
            }
            control.sendMsg(resp);
        }
    }

    @Override
    public String getTriggerHelp() {
        return super.getTriggerHelp() + " <data>";
    }
}
