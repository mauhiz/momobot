package momobot.trigger;

import ircbot.AColors;
import ircbot.ATrigger;
import ircbot.IIrcSpecialChars;
import ircbot.IrcUser;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import momobot.Db;
import momobot.Dispo;
import momobot.MomoBot;
import org.apache.log4j.Logger;
import utils.Utils;

/**
 * @author Administrator
 */
public class YaquiTrigger extends ATrigger implements IIrcSpecialChars {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(YaquiTrigger.class);

    /**
     * @param trigger
     *            le trigger
     */
    public YaquiTrigger(final String trigger) {
        super(trigger);
        setPublic(true);
    }

    /**
     * @param channel
     *            le channel
     * @param d
     *            la date
     */
    private void doTrigger(final String channel, final Date d) {
        final StringBuffer msg = new StringBuffer();
        msg.append(Utils.getJourFromDate(d) + " à 21h : ");
        final Collection < String > heure1Dispo = new TreeSet < String >();
        final Collection < String > heure1Pala = new TreeSet < String >();
        final Collection < String > heure2Dispo = new TreeSet < String >();
        final Collection < String > heure2Pala = new TreeSet < String >();
        try {
            for (final Dispo dispo : Db.getDispos(channel, d.getTime())) {
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
            if (heure1Dispo.isEmpty()) {
                msg.append("personne ");
            } else {
                msg.append(AColors.DARK_GREEN);
                for (final String string : heure1Dispo) {
                    msg.append(string);
                    msg.append(" ");
                }
                msg.append(NORMAL);
            }
            if (!heure1Pala.isEmpty()) {
                msg.append("(absents : " + AColors.RED);
                for (final String string : heure1Pala) {
                    msg.append(string);
                    msg.append(" ");
                }
                msg.append(NORMAL + ") ");
            }
            msg.append("22h30 : ");
            if (heure2Dispo.isEmpty()) {
                msg.append("personne ");
            } else {
                msg.append(AColors.DARK_GREEN);
                for (final String string : heure2Dispo) {
                    msg.append(string);
                    msg.append(" ");
                }
                msg.append(NORMAL);
            }
            if (!heure2Pala.isEmpty()) {
                msg.append("(absents : " + AColors.RED);
                for (final String string : heure2Pala) {
                    msg.append(string);
                    msg.append(" ");
                }
                msg.append(NORMAL + ") ");
            }
            MomoBot.getInstance().sendMessage(channel, msg.toString());
        } catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e, e);
            }
        }
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        /* rien */
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        final String msg = getArgs(message).toLowerCase();
        Date d;
        if (msg.equals("semaine")) {
            d = new Date();
            final long unJour = TimeUnit.MILLISECONDS
                    .convert(1L, TimeUnit.DAYS);
            final int nbJoursSemaine = 7;
            for (int i = 0; i < nbJoursSemaine; i++) {
                doTrigger(channel, d);
                d.setTime(d.getTime() + unJour);
            }
            return;
        }
        if (msg.length() < 1) {
            d = new Date();
        } else {
            try {
                d = Utils.getDateFromJour(msg);
            } catch (final NullPointerException e) {
                MomoBot.getInstance().sendNotice(
                        user,
                        "syntaxe : $" + getTriggerText()
                                + " jour[lundi/mardi/...] ou $"
                                + getTriggerText() + " semaine");
                return;
            }
        }
        doTrigger(channel, d);
    }
}
