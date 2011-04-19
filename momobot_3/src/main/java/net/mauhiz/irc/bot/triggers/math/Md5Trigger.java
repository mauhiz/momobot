package net.mauhiz.irc.bot.triggers.math;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Calcule un md5.
 * 
 * @author mauhiz
 */
public class Md5Trigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     */
    public Md5Trigger(String trigger) {
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
        String args = getTriggerContent(cme);
        String resp;
        if (StringUtils.isEmpty(args)) {
            resp = "md5 de quoi ?";
        } else {
            resp = "md5 de " + args + ": " + DigestUtils.md5Hex(args);
        }
        Privmsg msg = new Privmsg(cme, resp);
        control.sendMsg(msg);
    }
}
