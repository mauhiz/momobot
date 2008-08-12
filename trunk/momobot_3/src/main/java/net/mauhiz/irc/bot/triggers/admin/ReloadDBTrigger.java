package net.mauhiz.irc.bot.triggers.admin;

import java.sql.SQLException;

import net.mauhiz.irc.SqlUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.memo.DbMemoUtils;

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
        String resp;
        try {
            DbMemoUtils.loadMemoDB();
            SqlUtils.loadPlayerDB();
            resp = "DB reloaded";
        } catch (final SQLException sqle) {
            LOG.error(sqle, sqle);
            resp = "Erreur : " + sqle;
        }
        Privmsg msg = Privmsg.buildPrivateAnswer(pme, resp);
        control.sendMsg(msg);
    }
}
