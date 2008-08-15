package net.mauhiz.irc.bot.triggers.admin;

import java.util.Arrays;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
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
public class DeActivateTrigger extends AbstractTextTrigger implements IAdminTrigger, IPrivmsgTrigger {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(DeActivateTrigger.class);
    
    /**
     * @param trigger
     */
    public DeActivateTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
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
            LOG.info("Toggle trigger class = " + className);
            MmbTriggerManager manager = (MmbTriggerManager) ((IrcControl) control).getManager();
            boolean success;
            if (args.length == 1) {
                /* remove inconditionnel */
                success = manager.removeTrigger(className, null);
            } else {
                /* remove de certains triggertext seulement */
                String[] texts = Arrays.copyOfRange(args, 1, args.length);
                success = manager.removeTrigger(className, texts);
            }
            
            if (!success) {
                Privmsg retour = Privmsg.buildPrivateAnswer(pme, "Could not load trigger " + className);
                control.sendMsg(retour);
            }
        }
    }
}
