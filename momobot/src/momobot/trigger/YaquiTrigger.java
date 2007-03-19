package momobot.trigger;

import ircbot.AColors;
import ircbot.IIrcSpecialChars;
import ircbot.IrcUser;
import ircbot.ATrigger;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import momobot.Db;
import momobot.Dispo;
import momobot.MomoBot;
import utils.Utils;

/**
 * @author Administrator
 */
public class YaquiTrigger extends ATrigger implements IIrcSpecialChars {
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
        msg.append(Utils.getJourFromDate(d) + " a 21h : ");
        final Set < String > heure1Dispo = new TreeSet < String >();
        final Set < String > heure1Pala = new TreeSet < String >();
        final Set < String > heure2Dispo = new TreeSet < String >();
        final Set < String > heure2Pala = new TreeSet < String >();
        try {
            for (final Iterator < Dispo > ite = Db.getDispos(channel, d
                    .getTime()); ite.hasNext();) {
                final Dispo dispo = ite.next();
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
            if (heure1Dispo.size() == 0) {
                msg.append("personne ");
            } else {
                msg.append(AColors.DARK_GREEN);
                for (final String string : heure1Dispo) {
                    msg.append(string);
                    msg.append(" ");
                }
                msg.append(NORMAL);
            }
            if (heure1Pala.size() > 0) {
                msg.append("(absents : " + AColors.RED);
                for (final String string : heure1Pala) {
                    msg.append(string);
                    msg.append(" ");
                }
                msg.append(NORMAL + ") ");
            }
            msg.append("22h30 : ");
            if (heure2Dispo.size() == 0) {
                msg.append("personne ");
            } else {
                msg.append(AColors.DARK_GREEN);
                for (final String string : heure2Dispo) {
                    msg.append(string);
                    msg.append(" ");
                }
                msg.append(NORMAL);
            }
            if (heure2Pala.size() > 0) {
                msg.append("(absents : " + AColors.RED);
                for (final String string : heure2Pala) {
                    msg.append(string);
                    msg.append(" ");
                }
                msg.append(NORMAL + ") ");
            }
            MomoBot.getInstance().sendMessage(channel, msg.toString());
        } catch (final Exception e) {
            Utils.logError(getClass(), e);
        }
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        // rien
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
            final long unJour = 1000 * 60 * 60 * 24;
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
