package net.mauhiz.irc.bot.triggers.admin;

import java.util.Arrays;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.Launcher;
import net.mauhiz.irc.bot.MmbTriggerManager;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ActivateTrigger extends AbstractTextTrigger implements IAdminTrigger, IPrivmsgTrigger {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(ActivateTrigger.class);
    
    /**
     * @param trigger
     */
    public ActivateTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg pme, final IIrcControl control) {
        final String[] args = StringUtils.split(getArgs(pme.getMessage()));
        if (ArrayUtils.isEmpty(args)) {
            Privmsg retour = Privmsg.buildPrivateAnswer(pme, "you need to specify Trigger class name");
            Privmsg retour2 = Privmsg.buildPrivateAnswer(pme, "for instance : " + this + " " + getClass().getName());
            control.sendMsg(retour);
            control.sendMsg(retour2);
        } else {
            String className = args[0];
            LOG.info("Activate trigger class = " + className);
            Launcher.loadTrigClass(className, "", ((MmbTriggerManager) ((IrcControl) control).getManager()), Arrays
                    .copyOfRange(args, 1, args.length));
        }
    }
}
