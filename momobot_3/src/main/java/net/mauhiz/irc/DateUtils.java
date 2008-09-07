package net.mauhiz.irc;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang.time.StopWatch;

/**
 * @author mauhiz
 */
public class DateUtils {
    /**
     * mon format de date local.
     */
    public static final Format DATE_FORMAT = FastDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE);
    
    /**
     * les jours de la semaine.
     */
    public static final String[] JOURS = DateFormatSymbols.getInstance(Locale.FRANCE).getWeekdays();
    
    /**
     * mon format d'heure local.
     */
    private static final FastDateFormat TIME_FORMAT = FastDateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.FRANCE);
    
    /**
     * @param jour
     *            le jour
     * @return la date dans le futur
     */
    public static Calendar getDateFromJour(final String jour) {
        int entierFutur = 0;
        while (entierFutur < JOURS.length) {
            if (jour.equalsIgnoreCase(JOURS[entierFutur])) {
                break;
            }
            ++entierFutur;
        }
        if (entierFutur == JOURS.length) {
            return null;
        }
        /* ajd */
        final Calendar jourNow = Calendar.getInstance();
        /* calcul du décalage */
        final long unJour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        /* plus tard */
        /* update : le +7 pour eviter les resultats negatifs */
        Calendar retour = Calendar.getInstance(Locale.FRANCE);
        retour.setTimeInMillis(jourNow.getTimeInMillis()
                + (entierFutur + 1 - jourNow.get(Calendar.DAY_OF_WEEK) + JOURS.length) % JOURS.length * unJour);
        return retour;
    }
    
    /**
     * @param date
     *            la date
     * @return le jour en francais
     */
    public static String getJourFromDate(final Calendar date) {
        return JOURS[date.get(Calendar.DAY_OF_WEEK) - 1];
    }
    
    /**
     * @param date
     *            la date
     * @return le jour en francais
     */
    public static String getJourFromDate(final Date date) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        cal.setTime(date);
        return getJourFromDate(cal);
    }
    
    /**
     * @return un timestamp
     */
    public static String getTimeStamp() {
        return TIME_FORMAT.format(Calendar.getInstance(Locale.FRANCE));
    }
    
    /**
     * @param sw
     * @return un timestamp
     */
    public static String getTimeStamp(final StopWatch sw) {
        return TIME_FORMAT.format(System.currentTimeMillis() - sw.getTime());
    }
}
