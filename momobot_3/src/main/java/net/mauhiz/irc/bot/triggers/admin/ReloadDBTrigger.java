package net.mauhiz.irc.bot.triggers.admin;

import net.mauhiz.irc.HibernateUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ReloadDBTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, IAdminTrigger {
    /**
     * logger.
     */
    static final Logger LOG = Logger.getLogger(ReloadDBTrigger.class);
    
    /**
     * @param trigger
     *            le trigger
     */
    public ReloadDBTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg pme, final IIrcControl control) {
        HibernateUtils.closeSession();
        HibernateUtils.currentSession();
        String resp = "DB reloaded";
        Privmsg msg = Privmsg.buildPrivateAnswer(pme, resp);
        control.sendMsg(msg);
    }
}
