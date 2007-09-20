package momobot.dispo;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.QnetUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

import java.sql.SQLException;
import java.util.Date;
import java.util.StringTokenizer;

import momobot.MomoBot;
import momobot.SqlUtils;
import momobot.whois.Whois;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import utils.DateUtils;

/**
 * @author mauhiz
 */
public class DispoTrigger extends AbstractTrigger implements IPublicTrigger {
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
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!(user instanceof QnetUser)) {
            LOG.error("user non Qnet");
            return;
        }
        final QnetUser quser = (QnetUser) user;
        if (StringUtils.isEmpty(quser.getQnetAuth())) {
            final Whois whois = new Whois(user.getNick());
            whois.execute();
            /* on attend le whois */
            while (whois.isRunning()) {
                Thread.yield();
            }
            /*
             * TODO : bug a la con de merde, il detecte pas le whois tout de suite :/
             */
            if (StringUtils.isEmpty(quser.getQnetAuth())) {
                MomoBot.getBotInstance().sendNotice(user, "il faut etre auth sur Qnet pour utiliser cette fonction");
                return;
            }
        }
        final StringTokenizer tokenizer = new StringTokenizer(getArgs(message));
        final String jour = tokenizer.nextToken();
        final String[] heure = new String[2];
        heure[0] = tokenizer.nextToken();
        heure[1] = tokenizer.nextToken();
        /* tokens finis */
        final Date date = DateUtils.getDateFromJour(jour);
        final byte[] heures = new byte[2];
        for (byte i = 0; i < 2; ++i) {
            if (heure[i].equalsIgnoreCase("oui")) {
                heures[i] = 1;
            } else if (heure[i].equalsIgnoreCase("non")) {
                heures[i] = -1;
            }
        }
        try {
            SqlUtils.updateDispo(channel.getNom(), ((QnetUser) user).getQnetAuth(), date.getTime(), heures[0],
                    heures[1]);
            MomoBot.getBotInstance().sendNotice(user,
                    "dispo enregistrée pour le " + new DateUtils().getDateFormat().format(date));
        } catch (final SQLException sqle) {
            MomoBot.getBotInstance().sendNotice(user,
                    "syntaxe : $dispo jour[lundi/mardi/...] 21h[oui/non/?] 22h30[oui/non/?]");
        }
    }
}
