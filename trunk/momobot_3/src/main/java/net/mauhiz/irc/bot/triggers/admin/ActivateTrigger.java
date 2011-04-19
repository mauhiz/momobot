package net.mauhiz.irc.bot.triggers.admin;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.ArgumentList;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.DefaultTriggerManager;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.base.trigger.ITriggerManager;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;

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
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg pme, IIrcControl control) {
        ArgumentList args = getArgs(pme);
        if (args.isEmpty()) {
            Privmsg retour = new Privmsg(pme, "you need to specify Trigger class name", true);
            control.sendMsg(retour);
            Privmsg retour2 = new Privmsg(pme, "for instance : " + this + " " + getClass().getName(), true);
            control.sendMsg(retour2);
        } else {
            String className = args.poll();
            LOG.info("Activate trigger class = " + className);
            ITriggerManager[] managers = control.getManagers();
            for (ITriggerManager manager : managers) {
                if (manager instanceof DefaultTriggerManager) {
                    ((DefaultTriggerManager) manager).loadTrigClass(className, "", args.asList());
                    break;
                }
            }
        }
    }
}
