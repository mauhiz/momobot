package utils;

import static org.apache.commons.lang.StringUtils.removeEnd;
import static org.apache.commons.lang.StringUtils.removeStart;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class MomoStringUtils {
    /**
     * mon tableau avec le code morse.
     */
    private static final Map < Character, String > CODE_MORSE    = new TreeMap < Character, String >();
    /**
     * logger.
     */
    private static final Logger                    LOG           = Logger.getLogger(MomoStringUtils.class);
    /**
     * mon tableau avec le code morse inverse.
     */
    private static final Map < String, Character > REVERSE_MORSE = new TreeMap < String, Character >();

    /**
     * @param work
     *            la string à dépouiller
     * @return la string sans les accents
     */
    public static String effaceAccents(final String work) {
        return work.replace('ô', 'o').replace('î', 'i').replace('ï', 'i').replace('é', 'e').replace('è', 'e').replace(
                'ê', 'e').replace('ë', 'e').replace('à', 'a').replace('â', 'a').replace('ä', 'a');
    }

    /**
     * les lettres doivent être séparées par un espace.
     * @param work
     *            la chaîne en Morse
     * @return la chaine en lettres
     */
    public static StrBuilder fromMorse(final String work) {
        if (REVERSE_MORSE.isEmpty()) {
            loadMorse();
        }
        final StrBuilder output = new StrBuilder();
        for (final String element : new StrTokenizer(work).getTokenArray()) {
            output.append(REVERSE_MORSE.get(element));
        }
        return output;
    }

    /**
     * La map morse est formé ainsi...
     * 
     * <pre>
     * A .-\r\n
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private static void loadMorse() {
        try {
            final String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator;
            final List < String > lignes = FileUtils.readLines(new File(path + "morse_map.txt"), "ASCII");
            /* si on est arrivés jusque là le fichier existe, donc on peut nettoyer nos maps. */
            if (!CODE_MORSE.isEmpty()) {
                CODE_MORSE.clear();
            }
            if (!REVERSE_MORSE.isEmpty()) {
                REVERSE_MORSE.clear();
            }
            Character chara;
            String traitPoint;
            for (final String ligne : lignes) {
                chara = Character.valueOf(ligne.charAt(0));
                traitPoint = ligne.substring(2);
                CODE_MORSE.put(chara, traitPoint);
                REVERSE_MORSE.put(traitPoint, chara);
            }
        } catch (final IOException ioe) {
            LOG.warn(ioe, ioe);
        }
    }

    /**
     * méthode pour le wquizz.
     * @param work
     *            ce que je dois nettoyer
     * @return un string propre.
     */
    public static String nettoieReponse(final String work) {
        final StrBuilder str = new StrBuilder(effaceAccents(work)).replaceAll('-', ' ').replaceAll('\'', ' ')
                .replaceAll('^', ' ').replaceAll('¨', ' ').trim();
        String temp = str.toString();
        temp = removeStart(temp, "l ");
        temp = removeStart(temp, "la ");
        temp = removeStart(temp, "le ");
        temp = removeStart(temp, "les ");
        temp = removeStart(temp, "un ");
        temp = removeStart(temp, "une ");
        temp = removeStart(temp, "des ");
        temp = removeStart(temp, "du ");
        temp = removeStart(temp, "d ");
        temp = removeStart(temp, "a ");
        temp = removeStart(temp, "au ");
        temp = removeStart(temp, "aux ");
        temp = removeStart(temp, "en ");
        temp = removeStart(temp, "vers ");
        temp = removeStart(temp, "chez ");
        temp = removeStart(temp, "dans ");
        return removeEnd(temp, "?");
    }

    /**
     * @param seq
     *            une chaine à shaker
     * @return la chaine randomisee
     */
    public static StrBuilder shuffle(final String seq) {
        final StrBuilder input = new StrBuilder(seq);
        final StrBuilder output = new StrBuilder();
        int random;
        for (int len = input.length(); len > 0; --len) {
            random = RandomUtils.nextInt(len);
            output.append(input.charAt(random));
            input.deleteCharAt(random);
        }
        return output;
    }

    /**
     * @param work
     *            la chaîne à convertir en Morse
     * @return la chaine en Morse
     */
    public static StrBuilder toMorse(final String work) {
        if (CODE_MORSE.isEmpty()) {
            loadMorse();
        }
        final StrBuilder output = new StrBuilder();
        final String upper = work.toUpperCase(Locale.FRANCE);
        for (final char element : upper.toCharArray()) {
            output.append(CODE_MORSE.get(Character.valueOf(element)));
        }
        return output;
    }

    /**
     * constructeur caché.
     */
    protected MomoStringUtils() {
        throw new UnsupportedOperationException();
    }
}
