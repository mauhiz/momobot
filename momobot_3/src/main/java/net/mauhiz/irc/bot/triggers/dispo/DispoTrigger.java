package net.mauhiz.irc.bot.triggers.dispo;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.StringTokenizer;

import net.mauhiz.irc.DateUtils;
import net.mauhiz.irc.SqlUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.data.qnet.QnetUser;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.model.WhoisRequest;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class DispoTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(DispoTrigger.class);
    
    /**
     * @param trigger
     *            le trigger
     */
    public DispoTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        IrcUser user = Users.getInstance(cme.getServer()).findUser(new Mask(cme.getFrom()), false);
        if (!(user instanceof QnetUser)) {
            LOG.error("user non Qnet: " + user);
            return;
        }
        final QnetUser quser = (QnetUser) user;
        if (StringUtils.isEmpty(quser.getAuth())) {
            final WhoisRequest whois = new WhoisRequest(quser.getNick(), cme.getServer(), control);
            whois.execute();
            /* on attend le whois */
            while (whois.isRunning()) {
                Thread.yield();
            }
            /*
             * TODO : bug a la con de merde, il detecte pas le whois tout de suite :/
             */
            if (StringUtils.isEmpty(quser.getAuth())) {
                Notice notice = Notice.buildPrivateAnswer(cme,
                        "il faut etre auth sur Qnet pour utiliser cette fonction");
                control.sendMsg(notice);
                return;
            }
        }
        final StringTokenizer tokenizer = new StringTokenizer(getArgs(cme.getMessage()));
        final String jour = tokenizer.nextToken();
        final String[] args = new String[2];
        args[0] = tokenizer.nextToken();
        args[1] = tokenizer.nextToken();
        /* tokens finis */
        final Calendar date = DateUtils.getDateFromJour(jour);
        final byte[] heures = new byte[2];
        for (byte i = 0; i < 2; ++i) {
            if (args[i].equalsIgnoreCase("oui")) {
                heures[i] = 1;
            } else if (args[i].equalsIgnoreCase("non")) {
                heures[i] = -1;
            }
        }
        Notice notice;
        try {
            SqlUtils.updateDispo(cme.getTo(), quser.getAuth(), date.getTimeInMillis(), heures[0], heures[1]);
            notice = Notice.buildPrivateAnswer(cme, "dispo enregistrée pour le " + DateUtils.dateFormat.format(date));
        } catch (final SQLException sqle) {
            notice = Notice.buildPrivateAnswer(cme, "syntaxe : " + this
                    + " jour[lundi/mardi/...] 21h[oui/non/?] 22h30[oui/non/?]");
        }
        control.sendMsg(notice);
    }
}
