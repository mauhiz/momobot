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
        return seekmsg.replace("%P", String.valueOf(nbPlayer)).replace("%S", server).replace("%L", level);
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
        String temp = effaceAccents(work).replace('-', ' ').replace('\'', ' ').trim();
        String[] uselessWords = { "l ", "la ", "le ", "les ", "un ", "une ", "des ", "du ", "d ", "a ", "au ", "aux ",
                "en ", "vers ", "chez ", "dans " };
        for (String toRemove : uselessWords) {
            temp = StringUtils.removeStart(temp, toRemove);
        }

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
