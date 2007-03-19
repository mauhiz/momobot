package utils;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

/**
 * @author Administrator
 */
public abstract class MyStringUtils {
    /**
     * mon tableau avec le code morse.
     */
    private static Map < Character, String > codeMorse = new TreeMap < Character, String >();

    static {
        codeMorse.put(new Character('A'), ".-");
        codeMorse.put(new Character('B'), "-...");
        codeMorse.put(new Character('C'), "-.-.");
        codeMorse.put(new Character('D'), "-..");
        codeMorse.put(new Character('E'), ".");
        codeMorse.put(new Character('F'), "..-.");
        codeMorse.put(new Character('G'), "--.");
        codeMorse.put(new Character('H'), "....");
        codeMorse.put(new Character('I'), "..");
        codeMorse.put(new Character('J'), ".---");
        codeMorse.put(new Character('K'), "-.-");
        codeMorse.put(new Character('L'), ".-..");
        codeMorse.put(new Character('M'), "--");
        codeMorse.put(new Character('N'), "-.");
        codeMorse.put(new Character('O'), "---");
        codeMorse.put(new Character('P'), ".--.");
        codeMorse.put(new Character('Q'), "--.-");
        codeMorse.put(new Character('R'), ".-.");
        codeMorse.put(new Character('S'), "...");
        codeMorse.put(new Character('T'), "-");
        codeMorse.put(new Character('U'), "..-");
        codeMorse.put(new Character('V'), "...-");
        codeMorse.put(new Character('W'), ".--");
        codeMorse.put(new Character('X'), "-..-");
        codeMorse.put(new Character('Y'), "-.--");
        codeMorse.put(new Character('Z'), "--..");
        codeMorse.put(new Character('1'), ".----");
        codeMorse.put(new Character('2'), "..---");
        codeMorse.put(new Character('3'), "...--");
        codeMorse.put(new Character('4'), "....-");
        codeMorse.put(new Character('5'), ".....");
        codeMorse.put(new Character('6'), "-....");
        codeMorse.put(new Character('7'), "--...");
        codeMorse.put(new Character('8'), "---..");
        codeMorse.put(new Character('9'), "----.");
        codeMorse.put(new Character('0'), "-----");
        codeMorse.put(new Character('.'), ".-.-.-");
        codeMorse.put(new Character(','), "--..--");
        codeMorse.put(new Character(':'), "---...");
        codeMorse.put(new Character('?'), "..--..");
        codeMorse.put(new Character('\''), ".----.");
        codeMorse.put(new Character('-'), "-....-");
        codeMorse.put(new Character('/'), "-..-.");
        codeMorse.put(new Character('('), "-.--.-");
        codeMorse.put(new Character(')'), "-.--.-");
        codeMorse.put(new Character('"'), ".-..-.");
        codeMorse.put(new Character('@'), ".--.-.");
        codeMorse.put(new Character('='), "-...-");
        codeMorse.put(new Character(' '), " ");
    }

    /**
     * @param work
     *            la string à dépouiller
     * @return la string sans les accents
     */
    public static String effaceAccents(final String work) {
        return work.replace('ô', 'o').replace('î', 'i').replace('ï', 'i')
                .replace('é', 'e').replace('è', 'e').replace('ê', 'e').replace(
                        'ë', 'e').replace('à', 'a').replace('â', 'a').replace(
                        'ä', 'a');
    }

    /**
     * @param seq
     *            la chaine à modifier
     * @param pos
     *            l'index de la lettre à manger.
     * @return la string avec une lettre en moins
     */
    public static String mangeLettre(final String seq, final int pos) {
        final int max = seq.length() - 1;
        if (max < 1) {
            return "";
        }
        if (pos == 0) {
            return seq.substring(1);
        }
        if (pos > 0 && pos < max) {
            return seq.substring(0, pos) + seq.substring(pos + 1);
        }
        if (pos == max) {
            return seq.substring(0, pos);
        }
        return seq;
    }

    /**
     * méthode pour le wquizz.
     * @param work
     *            ce que je dois nettoyer
     * @return un string propre.
     */
    public static String nettoieReponse(final String work) {
        String temp = effaceAccents(work);
        temp = StringUtils.replaceChars(temp, '-', ' ');
        temp = StringUtils.replaceChars(temp, '\'', ' ');
        temp = StringUtils.replaceChars(temp, '^', ' ');
        temp = StringUtils.replaceChars(temp, '¨', ' ');
        temp = StringUtils.trim(temp);
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
    public static StringBuffer shake(final String seq) {
        final Random r = new Random();
        final StringBuffer output = new StringBuffer();
        String temp = seq;
        for (int len = temp.length(); len > 0; len--) {
            final int k = r.nextInt(len);
            output.append(temp.charAt(k));
            temp = mangeLettre(temp, k);
        }
        return output;
    }

    /**
     * @param work
     *            la chaîne à convertir en Morse
     * @return la chaine en Morse
     */
    public static StringBuffer toMorse(final String work) {
        final char[] charArr = work.toUpperCase().toCharArray();
        final StringBuffer output = new StringBuffer();
        for (final char element : charArr) {
            output.append(codeMorse.get(new Character(element)));
        }
        return output;
    }

    /**
     * constructeur caché.
     */
    protected MyStringUtils() {
        throw new UnsupportedOperationException();
    }
}
