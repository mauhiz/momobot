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
    private static final int               DAYS_PER_WEEK = 7;

    /**
     * les jours de la semaine.
     */
    private static Map < String, Integer > jours         = new HashMap < String, Integer >(
                                                                 DAYS_PER_WEEK);

    /**
     * mon format de date local.
     */
    public static final DateFormat         MY_DATE       = DateFormat
                                                                 .getDateInstance(
                                                                         DateFormat.MEDIUM,
                                                                         Locale.FRANCE);

    /**
     * mon format d'heure local.
     */
    private static final DateFormat        TIME          = DateFormat
                                                                 .getTimeInstance(
                                                                         DateFormat.MEDIUM,
                                                                         Locale.FRANCE);

    /**
     * remplit les jours de la semaine.
     */
    static {
        jours.put("lundi", Integer.valueOf(MONDAY));
        jours.put("mardi", Integer.valueOf(TUESDAY));
        jours.put("mercredi", Integer.valueOf(WEDNESDAY));
        jours.put("jeudi", Integer.valueOf(THURSDAY));
        jours.put("vendredi", Integer.valueOf(FRIDAY));
        jours.put("samedi", Integer.valueOf(SATURDAY));
        jours.put("dimanche", Integer.valueOf(SUNDAY));
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
        final int entierFutur = jours.get(jour).intValue();
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
        for (final Entry < String, Integer > en : jours.entrySet()) {
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
        return TIME.format(d);
    }

    /**
     * @param c
     *            la classe qui a un message à faire passer
     * @param msg
     *            ledit message
     */
    public static void log(final Class < ? > c, final String msg) {
        System.out.println("[" + getTimeStamp() + "] [" + c.getSimpleName()
                + "] " + msg);
    }

    /**
     * @param c
     *            la classe qui a merdé
     * @param t
     *            sa merde
     */
    public static void logError(final Class < ? > c, final Throwable t) {
        log(c, t.getMessage());
        t.printStackTrace();
    }
}
