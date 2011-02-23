package net.mauhiz.irc.bot.triggers.admin;

import java.util.Arrays;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.MmbTriggerManager;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class ActivateTrigger extends AbstractTextTrigger implements IAdminTrigger, IPrivmsgTrigger {

    /**
     * @param trigger
     */
    public ActivateTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg pme, IIrcControl control) {
        String[] args = StringUtils.split(getArgs(pme.getMessage()));
        if (ArrayUtils.isEmpty(args)) {
            Privmsg retour = Privmsg.buildPrivateAnswer(pme, "you need to specify Trigger class name");
            control.sendMsg(retour);
            Privmsg retour2 = Privmsg.buildPrivateAnswer(pme, "for instance : " + this + " " + getClass().getName());
            control.sendMsg(retour2);
        } else {
            String className = args[0];
            LOG.info("Activate trigger class = " + className);
            ((MmbTriggerManager) control.getManager()).loadTrigClass(className, "",
                    Arrays.copyOfRange(args, 1, args.length));
        }
    }
}
