package net.mauhiz.irc;

import java.text.Normalizer;
import java.util.Random;

import net.mauhiz.irc.base.IrcSpecialChars;

import org.apache.commons.lang3.StringUtils;

/**
 * @author mauhiz
 */
public class MomoStringUtils {
    private static final Random RANDOM = new Random();

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
        return toTest.codePointAt(0) == IrcSpecialChars.CHAN_DEFAULT
                || toTest.codePointAt(0) == IrcSpecialChars.CHAN_LOCAL;
    }

    /**
     * methode pour le wquizz.
     * 
     * @param work
     *            ce que je dois nettoyer
     * @return un string propre.
     */
    public static String nettoieReponse(String work) {
        String temp = normalizeAscii(work).replace('-', ' ').replace('\'', ' ').trim();
        String[] uselessWords = { "l ", "la ", "le ", "les ", "un ", "une ", "des ", "du ", "d ", "a ", "au ", "aux ",
                "en ", "vers ", "chez ", "dans " };
        for (String toRemove : uselessWords) {
            temp = StringUtils.removeStart(temp, toRemove);
        }

        return StringUtils.removeEnd(temp, "?");
    }

    /**
     * @param input
     *            la string a depouiller
     * @return la string sans les accents
     */
    public static String normalizeAscii(String input) {
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        return temp.replaceAll("[^\\p{ASCII}]", "");
    }

    public static void removeCodePoint(StringBuilder sb, int index) {
        int charCount = Character.charCount(sb.codePointAt(index));
        for (int i = 0; i < charCount; i++) {
            sb.deleteCharAt(index + i);
        }
    }

    public static void setCodePointAt(StringBuilder sb, int index, int codePoint) {
        char[] chrs = Character.toChars(codePoint);
        sb.setCharAt(index, chrs[0]);
        for (int i = 1; i < chrs.length; i++) {
            sb.insert(index + i, chrs[i]);
        }
    }

    /**
     * @param seq
     *            une chaine a shaker
     * @return la chaine randomisee
     */
    public static String shuffle(String seq) {
        StringBuilder input = new StringBuilder(seq);
        StringBuilder output = new StringBuilder(seq.length());
        for (int len = input.length(); len > 0; len--) {
            int random = RANDOM.nextInt(len);
            output.appendCodePoint(input.codePointAt(random));
            removeCodePoint(input, random);
        }
        return output.toString();
    }

}
