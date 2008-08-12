package net.mauhiz.irc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Morse {
    /**
     * mon tableau avec le code morse.
     */
    private static final Map<Character, String> CODE_MORSE = new TreeMap<Character, String>();
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(Morse.class);
    /**
     * mon tableau avec le code morse inverse.
     */
    private static final Map<String, Character> REVERSE_MORSE = new TreeMap<String, Character>();
    
    /**
     * les lettres doivent être séparées par un espace.
     * 
     * @param work
     *            la chaîne en Morse
     * @return la chaine en lettres
     */
    public static String fromMorse(final String work) {
        if (REVERSE_MORSE.isEmpty()) {
            loadMorse();
        }
        final StrBuilder output = new StrBuilder();
        for (final String element : new StrTokenizer(work).getTokenArray()) {
            output.append(REVERSE_MORSE.get(element));
        }
        return output.toString();
    }
    
    /**
     * La map morse est formé ainsi...
     * 
     * <pre>
     * A .-\r\n
     * </pre>
     */
    private static void loadMorse() {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("morse_map.txt");
            final List<String> lignes = IOUtils.readLines(is, "ASCII");
            /*
             * si on est arrivés jusque là le fichier existe, donc on peut
             * nettoyer nos maps.
             */
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
     * @param work
     *            la chaîne à convertir en Morse
     * @return la chaine en Morse
     */
    public static String toMorse(final String work) {
        if (CODE_MORSE.isEmpty()) {
            loadMorse();
        }
        final StrBuilder output = new StrBuilder();
        final String upper = work.toUpperCase(Locale.FRANCE);
        for (final char element : upper.toCharArray()) {
            output.append(CODE_MORSE.get(Character.valueOf(element)));
        }
        return output.toString();
    }
}
