package net.mauhiz.irc.bot.triggers.admin;

import java.util.Arrays;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.IrcMessage;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.Launcher;
import net.mauhiz.irc.bot.MmbTriggerManager;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

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
     * @param control
     * @param className
     * @param toReply
     */
    private void dispClassError(final IIrcControl control, final String className, final IrcMessage toReply) {
        Privmsg retour = Privmsg.buildPrivateAnswer(toReply, className + ": is not a valid trigger class name.");
        control.sendMsg(retour);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        Privmsg pme = im;
        final String[] args = StringUtils.split(pme.getMessage(), " ");
        if (args == null || args.length < 3) {
            Privmsg retour = Privmsg.buildPrivateAnswer(pme,
                    "this trigger needs at least two parameters : class name and trigger name");
            control.sendMsg(retour);
        } else {
            String className = args[1];
            LOG.info("Toggle trigger class = " + className);
            boolean success = Launcher.loadTrigClass(className, "$", ((MmbTriggerManager) ((IrcControl) control)
                    .getManager()), Arrays.copyOfRange(args, 1, args.length));
            if (!success) {
                dispClassError(control, className, pme);
            }
        }
    }
}
