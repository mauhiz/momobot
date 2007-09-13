package momobot.dispo;

import ircbot.Channel;
import ircbot.ColorsUtils;
import ircbot.IIrcSpecialChars;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import momobot.MomoBot;
import momobot.SqlUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

import utils.DateUtils;

/**
 * @author mauhiz
 */
public class YaquiTrigger extends AbstractTrigger implements IIrcSpecialChars, IPublicTrigger {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(YaquiTrigger.class);
    /**
     * un espace.
     */
    private static final String SEP = " ";

    /**
     * @param msg
     * @param heureDispo
     * @param heurePala
     * @return ?
     */
    private static StrBuilder appendDispos(final StrBuilder msg,
            final Collection < String > heureDispo,
            final Collection < String > heurePala) {
        if (heureDispo.isEmpty()) {
            msg.append("personne ");
        } else {
            msg.append(ColorsUtils.DARK_GREEN);
            for (final String string : heureDispo) {
                msg.append(string).append(SEP);
            }
            msg.append(NORMAL);
        }
        if (!heurePala.isEmpty()) {
            msg.append("(absents : ").append(ColorsUtils.RED);
            for (final String string : heurePala) {
                msg.append(string).append(SEP);
            }
            msg.append(NORMAL).append(") ");
        }
        return msg;
    }

    /**
     * @param trigger
     *            le trigger
     */
    public YaquiTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @param channel
     *            le channel
     * @param date
     *            la date
     */
    private void doTrigger(final String channel, final Date date) {
        final Collection < String > heure1Dispo = new LinkedList < String >();
        final Collection < String > heure1Pala = new LinkedList < String >();
        final Collection < String > heure2Dispo = new LinkedList < String >();
        final Collection < String > heure2Pala = new LinkedList < String >();
        try {
            for (final Dispo dispo : SqlUtils.getDispos(channel, date.getTime())) {
                if (dispo.getHeure1() == 1) {
                    heure1Dispo.add(dispo.getQauth());
                } else if (dispo.getHeure1() == -1) {
                    heure1Pala.add(dispo.getQauth());
                }
                if (dispo.getHeure2() == 1) {
                    heure2Dispo.add(dispo.getQauth());
                } else if (dispo.getHeure2() == -1) {
                    heure2Pala.add(dispo.getQauth());
                }
            }
            final StrBuilder msg = new StrBuilder(DateUtils.getJourFromDate(date));
            msg.append(" à 21h : ");
            appendDispos(msg, heure1Dispo, heure1Pala);
            msg.append("22h30 : ");
            appendDispos(msg, heure2Dispo, heure2Pala);
            MomoBot.getBotInstance().sendMessage(Channel.getChannel(channel), msg.toString());
        } catch (final SQLException sqle) {
            LOG.error(sqle, sqle);
        }
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!test(message)) {
            return;
        }
        final String msg = getArgs(message).toLowerCase(Locale.FRANCE);
        final Date date = new Date();
        if ("semaine".equals(msg)) {
            /* dispo pour toute la semaine */
            final long unJour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
            for (byte i = 0; i < DateUtils.DAYS_PER_WEEK; ++i) {
                doTrigger(channel.getNom(), date);
                date.setTime(date.getTime() + unJour);
            }
        } else if (StringUtils.isBlank(msg)) {
            /* dispo pour le jour même */
            doTrigger(channel.getNom(), date);
        } else {
            /* dispo pour plus tard */
            try {
                date.setTime(DateUtils.getDateFromJour(msg).getTime());
                doTrigger(channel.getNom(), date);
            } catch (final IllegalArgumentException iae) {
                MomoBot.getBotInstance().sendNotice(
                        user,
                        "syntaxe : $" + getTriggerText() + " jour[lundi/mardi/...] ou $" + getTriggerText()
                                + " semaine");
            }
        }
    }
}
