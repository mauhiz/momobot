package net.mauhiz.util;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.text.StrTokenizer;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public enum Morse {
    ;
    /**
     * mon tableau avec le code morse.
     */
    private static final SortedMap<String, String> CODE_MORSE = new TreeMap<String, String>();
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(Morse.class);
    /**
     * mon tableau avec le code morse inverse.
     */
    private static final SortedMap<String, String> REVERSE_MORSE = new TreeMap<String, String>();

    /**
     * les lettres doivent etre separees par un espace.
     * 
     * @param work
     *            la chaine en Morse
     * @return la chaine en lettres
     */
    public static String fromMorse(String work) {
        if (REVERSE_MORSE.isEmpty()) {
            loadMorse();
        }
        StringBuilder output = new StringBuilder();
        for (String element : new StrTokenizer(work).getTokenArray()) {
            output.append(REVERSE_MORSE.get(element));
        }
        return output.toString();
    }

    /**
     * La map morse est forme ainsi...
     * 
     * <pre>
     * A .-\r\n
     * </pre>
     */
    private static void loadMorse() {
        try {
            List<String> lignes = FileUtil.readFileInCp("morse_map.txt", FileUtil.ISO8859_15);
            /*
             * si on est arrives jusque la le fichier existe, donc on peut nettoyer nos maps.
             */
            if (!CODE_MORSE.isEmpty()) {
                CODE_MORSE.clear();
            }
            if (!REVERSE_MORSE.isEmpty()) {
                REVERSE_MORSE.clear();
            }
            for (String ligne : lignes) {
                String normal = ligne.substring(0, 1);
                String traitPoint = ligne.substring(2);
                CODE_MORSE.put(normal, traitPoint);
                REVERSE_MORSE.put(traitPoint, normal);
            }
        } catch (IOException ioe) {
            LOG.warn(ioe, ioe);
        }
    }

    /**
     * @param work
     *            la chaine a convertir en Morse
     * @return la chaine en Morse
     */
    public static String toMorse(String work) {
        if (CODE_MORSE.isEmpty()) {
            loadMorse();
        }
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < work.length(); i++) {
            String element = work.substring(i, i + 1);
            output.append(CODE_MORSE.get(element.toUpperCase()));
        }
        return output.toString();
    }
}
