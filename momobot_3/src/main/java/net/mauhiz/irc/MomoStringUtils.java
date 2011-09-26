package net.mauhiz.irc;

import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.util.StringUtil;
import net.mauhiz.util.UtfChar;

import org.apache.commons.lang3.StringUtils;

/**
 * @author mauhiz
 */
public enum MomoStringUtils {
    ;

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
        if (StringUtils.isEmpty(toTest) || StringUtil.containsAny(toTest, IrcSpecialChars.Z_NOTCHSTRING)) {
            return false;
        }
        UtfChar first = UtfChar.charAt(toTest, 0);
        return IrcSpecialChars.CHAN_DEFAULT.equals(first) || IrcSpecialChars.CHAN_LOCAL.equals(first);
    }

    /**
     * methode pour le wquizz.
     * 
     * @param work
     *            ce que je dois nettoyer
     * @return un string propre.
     */
    public static String nettoieReponse(String work) {
        String temp = StringUtil.normalizeToAscii(work).replace('-', ' ').replace('\'', ' ').trim();
        String[] uselessWords = { "l ", "la ", "le ", "les ", "un ", "une ", "des ", "du ", "d ", "a ", "au ", "aux ",
                "en ", "vers ", "chez ", "dans " };
        for (String toRemove : uselessWords) {
            temp = StringUtils.removeStart(temp, toRemove);
        }

        return StringUtils.removeEnd(temp, "?");
    }

}
