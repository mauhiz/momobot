package net.mauhiz.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang.time.StopWatch;

/**
 * @author mauhiz
 */
public class DateUtil {
    /**
     * mon format de date local.
     */
    public static final Format DATE_FORMAT = FastDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE);
    
    /**
     * mon format d'heure local.
     */
    private static final FastDateFormat TIME_FORMAT = FastDateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.FRANCE);
    
    private static int dayStrToInt(String day, Locale loc) {
        String[] days = DateFormatSymbols.getInstance(loc).getWeekdays();
        
        for (int dayOfWeek = Calendar.getInstance(loc).getMinimum(Calendar.DAY_OF_WEEK); dayOfWeek < days.length; ++dayOfWeek) {
            if (day.equalsIgnoreCase(days[dayOfWeek])) {
                return dayOfWeek;
            }
        }
        
        throw new IllegalArgumentException("Invalid day: " + day);
    }
    /**
     * @param jour
     *            le jour
     * @return la date dans le futur
     */
    public static Calendar getDateFromJour(String jour, Locale loc) {
        
        int argDay = dayStrToInt(jour, loc);
        
        /* ajd */
        Calendar jourNow = Calendar.getInstance(loc);
        /* update : le +DAYS_PER_WEEK pour eviter les resultats negatifs */
        int daysPerWeek = jourNow.getMaximum(Calendar.DAY_OF_WEEK);
        int offset = (argDay - jourNow.get(Calendar.DAY_OF_WEEK) + daysPerWeek) % daysPerWeek;
        
        /* plus tard */
        jourNow.add(Calendar.DAY_OF_YEAR, offset);
        return jourNow;
    }
    
    /**
     * @param date
     *            la date
     * @return le jour en francais
     */
    public static String getJourFromDate(Calendar date, Locale loc) {
        return DateFormatSymbols.getInstance(loc).getWeekdays()[date.get(Calendar.DAY_OF_WEEK)];
    }
    
    /**
     * @param date
     *            la date
     * @return le jour en francais
     */
    public static String getJourFromDate(Date date, Locale loc) {
        Calendar cal = Calendar.getInstance(loc);
        cal.setTime(date);
        return getJourFromDate(cal, loc);
    }
    
    /**
     * @return un timestamp
     */
    public static String getTimeStamp(Locale loc) {
        return FastDateFormat.getTimeInstance(DateFormat.MEDIUM, loc).format(Calendar.getInstance(loc));
    }
    
    /**
     * @param sw
     * @return un timestamp
     */
    public static String getTimeStamp(StopWatch sw) {
        return TIME_FORMAT.format(System.currentTimeMillis() - sw.getTime());
    }
    
    public static String[] getWeekDays(Locale loc) {
        String[] weekdays = DateFormatSymbols.getInstance(loc).getWeekdays();
        return (String[]) ArrayUtils.subarray(weekdays, 1, weekdays.length); // 1-based
    }
}
