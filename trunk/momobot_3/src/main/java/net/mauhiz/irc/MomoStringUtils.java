package net.mauhiz.irc;

import java.text.Normalizer;

import net.mauhiz.irc.base.IrcSpecialChars;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * @author mauhiz
 */
public class MomoStringUtils {
    /**
     * @param input
     *            la string a depouiller
     * @return la string sans les accents
     */
    public static String effaceAccents(String input) {
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
    public static String genereSeekMessage(String seekmsg, int nbPlayer, String server, String level) {
        String str = seekmsg.replace("%P", String.valueOf(nbPlayer));
        str = str.replace("%S", server);
        str = str.replace("%L", level);
        return str;
    }
    /**
     * @param toTest
     *            le nom a tester
     * @return si le nom est un channel ou nom
     */
    public static boolean isChannelName(String toTest) {
        if (StringUtils.isEmpty(toTest) || StringUtils.indexOfAny(toTest, IrcSpecialChars.Z_NOTCHSTRING) > 0) {
            return false;
        }
        return toTest.charAt(0) == IrcSpecialChars.CHAN_DEFAULT || toTest.charAt(0) == IrcSpecialChars.CHAN_LOCAL;
    }
    /**
     * methode pour le wquizz.
     * 
     * @param work
     *            ce que je dois nettoyer
     * @return un string propre.
     */
    public static String nettoieReponse(String work) {
        String temp = effaceAccents(work).replace('-', ' ');
        temp = temp.replace('\'', ' ');
        temp = temp.trim();
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
     *            une chaine a shaker
     * @return la chaine randomisee
     */
    public static String shuffle(String seq) {
        StringBuilder input = new StringBuilder(seq);
        StringBuilder output = new StringBuilder(seq.length());
        for (int len = input.length(); len > 0; --len) {
            int random = RandomUtils.nextInt(len);
            output.append(input.charAt(random));
            input.deleteCharAt(random);
        }
        return output.toString();
    }
    
}
