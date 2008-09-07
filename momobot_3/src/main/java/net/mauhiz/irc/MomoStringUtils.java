package net.mauhiz.irc;

import java.text.Normalizer;

import net.mauhiz.irc.base.IrcSpecialChars;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
public class MomoStringUtils {
    /**
     * @param input
     *            la string à dépouiller
     * @return la string sans les accents
     */
    public static String effaceAccents(final String input) {
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        return temp.replaceAll("[^\\p{ASCII}]", "");
    }
    
    /**
     * TODO utiliser un MessageFormat
     * 
     * @param seekmsg
     * @param nbPlayer
     * @param server
     * @param level
     *            de seek, %P, %P = Nombre de joueur, %S = server, %L = level
     * @return la chaine randomisee
     */
    public static String genereSeekMessage(final String seekmsg, final int nbPlayer, final String server,
            final String level) {
        String str = seekmsg;
        str = str.replace("%P", String.valueOf(nbPlayer));
        str = str.replace("%S", server);
        str = str.replace("%L", level);
        return str;
    }
    /**
     * @param toTest
     *            le nom à tester
     * @return si le nom est un channel ou nom
     */
    public static boolean isChannelName(final String toTest) {
        if (StringUtils.isEmpty(toTest) || StringUtils.indexOfAny(toTest, IrcSpecialChars.Z_NOTCHSTRING) > 0) {
            return false;
        }
        return toTest.charAt(0) == IrcSpecialChars.CHAN_DEFAULT || toTest.charAt(0) == IrcSpecialChars.CHAN_LOCAL;
    }
    /**
     * méthode pour le wquizz.
     * 
     * @param work
     *            ce que je dois nettoyer
     * @return un string propre.
     */
    public static String nettoieReponse(final String work) {
        final StrBuilder str = new StrBuilder(effaceAccents(work));
        str.replaceAll('-', ' ');
        str.replaceAll('\'', ' ');
        str.replaceAll('^', ' ');
        str.replaceAll('¨', ' ');
        str.trim();
        String temp = str.toString();
        temp = StringUtils.removeStart(temp, "l ");
        temp = StringUtils.removeStart(temp, "la ");
        temp = StringUtils.removeStart(temp, "le ");
        temp = StringUtils.removeStart(temp, "les ");
        temp = StringUtils.removeStart(temp, "un ");
        temp = StringUtils.removeStart(temp, "une ");
        temp = StringUtils.removeStart(temp, "des ");
        temp = StringUtils.removeStart(temp, "du ");
        temp = StringUtils.removeStart(temp, "d ");
        temp = StringUtils.removeStart(temp, "a ");
        temp = StringUtils.removeStart(temp, "au ");
        temp = StringUtils.removeStart(temp, "aux ");
        temp = StringUtils.removeStart(temp, "en ");
        temp = StringUtils.removeStart(temp, "vers ");
        temp = StringUtils.removeStart(temp, "chez ");
        temp = StringUtils.removeStart(temp, "dans ");
        return StringUtils.removeEnd(temp, "?");
    }
    
    /**
     * @param seq
     *            une chaine à shaker
     * @return la chaine randomisee
     */
    public static String shuffle(final String seq) {
        final StringBuilder input = new StringBuilder(seq);
        final StringBuilder output = new StringBuilder(seq.length());
        for (int len = input.length(); len > 0; --len) {
            int random = RandomUtils.nextInt(len);
            output.append(input.charAt(random));
            input.deleteCharAt(random);
        }
        return output.toString();
    }
    
}
