package utils;

import static java.text.DateFormat.MEDIUM;
import static java.util.Locale.FRANCE;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mauhiz
 */
public class DateUtils {
    /**
     * le nmobre de jours dans une semaine.
     */
    public static final byte       DAYS_PER_WEEK = 7;
    /**
     * 
     */
    private static final DateUtils INSTANCE      = new DateUtils();
    /**
     * les jours de la semaine. dimanche = {@link Calendar#MONDAY}
     */
    private static final String[]  JOURS         = { "dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi",
            "samedi",                           };

    /**
     * @param jour
     *            le jour
     * @return la date dans le futur
     */
    public static Date getDateFromJour(final String jour) {
        byte entierFutur = 0;
        while (entierFutur < JOURS.length) {
            if (jour.equalsIgnoreCase(JOURS[entierFutur])) {
                break;
            }
            ++entierFutur;
        }
        if (entierFutur == JOURS.length) {
            throw new IllegalArgumentException();
        }
        /* ajd */
        final Calendar jourNow = Calendar.getInstance();
        jourNow.setTime(new Date());
        /* calcul du décalage */
        final long unJour = MILLISECONDS.convert(1, DAYS);
        /* plus tard */
        /* update : le +7 pour eviter les resultats negatifs */
        return new Date(jourNow.getTimeInMillis() +
                (entierFutur + 1 - jourNow.get(Calendar.DAY_OF_WEEK) + DAYS_PER_WEEK) % DAYS_PER_WEEK * unJour);
    }

    /**
     * @param date
     *            la date
     * @return le jour en francais
     */
    public static String getJourFromDate(final Date date) {
        final Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return JOURS[cld.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * @return un timestamp
     */
    public static String getTimeStamp() {
        synchronized (INSTANCE.myTime) {
            return INSTANCE.myTime.format(new Date());
        }
    }

    /**
     * mon format de date local.
     */
    public final DateFormat  myDate = DateFormat.getDateInstance(MEDIUM, FRANCE);
    /**
     * mon format d'heure local.
     */
    private final DateFormat myTime = DateFormat.getTimeInstance(MEDIUM, FRANCE);
}
