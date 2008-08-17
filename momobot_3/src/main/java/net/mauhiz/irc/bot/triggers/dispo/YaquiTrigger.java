package net.mauhiz.irc.bot.triggers.dispo;

import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.DateUtils;
import net.mauhiz.irc.SqlUtils;
import net.mauhiz.irc.base.Color;
import net.mauhiz.irc.base.ColorUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class YaquiTrigger extends AbstractTextTrigger implements IrcSpecialChars, IPrivmsgTrigger {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(YaquiTrigger.class);
    /**
     * un espace.
     */
    private static final char SEP = ' ';
    
    /**
     * @param msg
     * @param heureDispo
     * @param heurePala
     * @return ?
     */
    private static StrBuilder appendDispos(final StrBuilder msg, final Collection<String> heureDispo,
            final Collection<String> heurePala) {
        if (heureDispo.isEmpty()) {
            msg.append("personne ");
        } else {
            String heuresDispo = StringUtils.join(heureDispo, SEP);
            msg.append(ColorUtils.toColor(heuresDispo, Color.DARK_GREEN));
        }
        if (!heurePala.isEmpty()) {
            msg.append("(absents : ");
            String heuresDispo = StringUtils.join(heurePala, SEP);
            msg.append(ColorUtils.toColor(heuresDispo, Color.RED));
            msg.append(") ");
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
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        final String msg = getArgs(cme.getMessage()).toLowerCase(Locale.FRANCE);
        final Calendar date;
        if ("semaine".equals(msg)) {
            /* dispo pour toute la semaine */
            final long unJour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
            date = Calendar.getInstance(Locale.FRANCE);
            for (int i = 0; i < DateUtils.JOURS.length; ++i) {
                doTrigger(cme.getTo(), date);
                date.setTimeInMillis(date.getTimeInMillis() + unJour);
            }
        } else if (StringUtils.isBlank(msg)) {
            /* dispo pour le jour même */
            date = Calendar.getInstance(Locale.FRANCE);
            doTrigger(cme.getTo(), date);
        } else {
            /* dispo pour plus tard */
            date = DateUtils.getDateFromJour(msg);
            if (date == null) {
                Notice notice = Notice.buildPrivateAnswer(cme, "syntaxe : " + this + " jour["
                        + StringUtils.join(DateFormatSymbols.getInstance(Locale.FRANCE).getWeekdays(), '/') + "] ou $"
                        + this + " semaine");
                control.sendMsg(notice);
            } else {
                Privmsg resp = Privmsg.buildAnswer(cme, doTrigger(cme.getTo(), date));
                control.sendMsg(resp);
            }
        }
    }
    /**
     * @param channel
     *            le channel
     * @param date
     *            la date
     * @return msg
     */
    private String doTrigger(final String channel, final Calendar date) {
        final Collection<String> heure1Dispo = new LinkedList<String>();
        final Collection<String> heure1Pala = new LinkedList<String>();
        final Collection<String> heure2Dispo = new LinkedList<String>();
        final Collection<String> heure2Pala = new LinkedList<String>();
        try {
            for (final Dispo dispo : SqlUtils.getDispos(channel, date.getTimeInMillis())) {
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
            return msg.toString();
        } catch (final SQLException sqle) {
            LOG.error(sqle, sqle);
            return null;
        }
    }
}
