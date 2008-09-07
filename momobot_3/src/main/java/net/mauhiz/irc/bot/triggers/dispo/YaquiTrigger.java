package net.mauhiz.irc.bot.triggers.dispo;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import net.mauhiz.irc.DateUtils;
import net.mauhiz.irc.base.Color;
import net.mauhiz.irc.base.ColorUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IrcMessage;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class YaquiTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(YaquiTrigger.class);
    /**
     * un espace.
     */
    private static final char SEP = ' ';
    // doSqlQuery("SELECT `qauth`, `heure1`, `heure2` FROM `dispo` WHERE `channel` = '" + channel + "' AND `date`= '" +
    // datesql.toString() + "'");
    
    /**
     * @param msg
     * @param heureDispo
     * @param heurePala
     * @return ?
     */
    private static StringBuilder appendDispos(final StringBuilder msg, final Collection<String> heureDispo,
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
     * @param imsg
     * @param cal
     * @return msg
     */
    private String doTrigger(final IrcMessage imsg, final Calendar cal) {
        java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
        final Collection<String> heure1Dispo = new LinkedList<String>();
        final Collection<String> heure1Pala = new LinkedList<String>();
        final Collection<String> heure2Dispo = new LinkedList<String>();
        final Collection<String> heure2Pala = new LinkedList<String>();
        List<Dispo> dispos = DispoDb.getInstance(imsg.getServer()).getDispo(imsg.getTo(), date);
        for (final Dispo dispo : dispos) {
            if (dispo.getPresent1() == Dispo.Present.LA) {
                heure1Dispo.add(dispo.getQauth());
            } else if (dispo.getPresent1() == Dispo.Present.PAS_LA) {
                heure1Pala.add(dispo.getQauth());
            }
            if (dispo.getPresent2() == Dispo.Present.LA) {
                heure2Dispo.add(dispo.getQauth());
            } else if (dispo.getPresent2() == Dispo.Present.PAS_LA) {
                heure2Pala.add(dispo.getQauth());
            }
        }
        final StringBuilder msg = new StringBuilder(DateUtils.getJourFromDate(date));
        msg.append(" à 21h : ");
        appendDispos(msg, heure1Dispo, heure1Pala);
        msg.append("22h30 : ");
        appendDispos(msg, heure2Dispo, heure2Pala);
        return msg.toString();
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
                doTrigger(cme, date);
                date.setTimeInMillis(date.getTimeInMillis() + unJour);
            }
        } else if (StringUtils.isBlank(msg)) {
            /* dispo pour le jour même */
            date = Calendar.getInstance(Locale.FRANCE);
            doTrigger(cme, date);
        } else {
            /* dispo pour plus tard */
            date = DateUtils.getDateFromJour(msg);
            if (date == null) {
                Notice notice = Notice.buildPrivateAnswer(cme, "syntaxe : " + this + " jour["
                        + StringUtils.join(DateFormatSymbols.getInstance(Locale.FRANCE).getWeekdays(), '/') + "] ou $"
                        + this + " semaine");
                control.sendMsg(notice);
            } else {
                Privmsg resp = Privmsg.buildAnswer(cme, doTrigger(cme, date));
                control.sendMsg(resp);
            }
        }
    }
}
