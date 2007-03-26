package utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
public abstract class Utils extends Calendar {
    /**
     * le nmobre de jours dans une semaine.
     */
    private static final int                     DAYS_PER_WEEK = 7;
    /**
     * les jours de la semaine.
     */
    private static final Map < String, Integer > JOURS;
    /**
     * mon format de date local.
     */
    public static final DateFormat               MY_DATE;
    /**
     * mon format d'heure local.
     */
    private static final DateFormat              MY_TIME;
    /**
     * remplit les jours de la semaine.
     */
    static {
        MY_DATE = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE);
        MY_TIME = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.FRANCE);
        JOURS = new HashMap < String, Integer >(DAYS_PER_WEEK);
        JOURS.put("lundi", Integer.valueOf(MONDAY));
        JOURS.put("mardi", Integer.valueOf(TUESDAY));
        JOURS.put("mercredi", Integer.valueOf(WEDNESDAY));
        JOURS.put("jeudi", Integer.valueOf(THURSDAY));
        JOURS.put("vendredi", Integer.valueOf(FRIDAY));
        JOURS.put("samedi", Integer.valueOf(SATURDAY));
        JOURS.put("dimanche", Integer.valueOf(SUNDAY));
    }

    /**
     * @param jour
     *            le jour
     * @return la date dans le futur
     */
    public static Date getDateFromJour(final String jour) {
        // ajd
        final Calendar jourNow = Calendar.getInstance();
        final Date aujourdhui = new Date();
        jourNow.setTime(aujourdhui);
        final int entierNow = jourNow.get(DAY_OF_WEEK);
        // plus tard
        long longFutur = aujourdhui.getTime();
        final int entierFutur = JOURS.get(jour).intValue();
        // calcul du decalage
        final long unJour = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        // update : le +7 pour eviter les resultats negatifs
        longFutur += (entierFutur - entierNow + DAYS_PER_WEEK) % DAYS_PER_WEEK
                * unJour;
        return new Date(longFutur);
    }

    /**
     * @param d
     *            la date
     * @return le jour en francais
     */
    public static String getJourFromDate(final Date d) {
        final Calendar cld = Calendar.getInstance();
        cld.setTime(d);
        final int jour = cld.get(DAY_OF_WEEK);
        for (final Entry < String, Integer > en : JOURS.entrySet()) {
            if (en.getValue().intValue() == jour) {
                return en.getKey();
            }
        }
        return null;
    }

    /**
     * @return un timestamp
     */
    public static String getTimeStamp() {
        final Date d = new Date(System.currentTimeMillis());
        return MY_TIME.format(d);
    }
}
