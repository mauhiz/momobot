package net.mauhiz.irc.bot.triggers.fun;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.StringUtils;

/**
 * Transforme un texte en KennySpeak.
 * 
 * @author mauhiz
 */
public class KennyTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * longueur d'une kennylettre.
     */
    private static final int KENNY_LETTER_LEN = 3;
    /**
     * The KENNYLETTERS in alphabetical order. Big Letters are the Same with the only difference That the First char is
     * UpperCase
     */
    private static final Map<String, Character> KENNY_TO_NORMAL = new TreeMap<String, Character>();
    /**
     * 
     */
    private static final Map<Character, String> NORMAL_TO_KENNY = new TreeMap<Character, String>();
    
    /**
     * @param toTest
     *            la chaine a tester
     * @return ...
     */
    private static boolean isKenny(String toTest) {
        if (toTest.length() % 3 != 0) {
            return false;
        }
        char ch;
        for (char loopChar : toTest.toCharArray()) {
            if (Character.isLetter(loopChar)) {
                ch = Character.toLowerCase(loopChar);
                if (ch != 'm' && ch != 'f' && ch != 'p') {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * La map kenny est formee ainsi...
     * 
     * <pre>
     * a mpf\r\n
     * </pre>
     */
    private static void loadKenny() {
        try {
            List<String> lignes = FileUtil.readFileInCp("kenny_map.txt", FileUtil.ISO8859_15);
            if (!KENNY_TO_NORMAL.isEmpty()) {
                KENNY_TO_NORMAL.clear();
            }
            if (!NORMAL_TO_KENNY.isEmpty()) {
                NORMAL_TO_KENNY.clear();
            }
            Character chara;
            String mpf;
            for (String ligne : lignes) {
                if (ligne.length() < 2) {
                    continue;
                }
                chara = Character.valueOf(ligne.charAt(0));
                mpf = ligne.substring(2);
                NORMAL_TO_KENNY.put(chara, mpf);
                KENNY_TO_NORMAL.put(mpf, chara);
            }
        } catch (IOException ioe) {
            LOG.warn(ioe, ioe);
        }
    }
    
    /**
     * @param toTranslate
     *            String to Translate
     * @return translated String
     */
    public static StringBuilder translate(String toTranslate) {
        if (isKenny(toTranslate)) {
            return translateKennyToNormal(toTranslate);
        }
        return translateNormalToKenny(toTranslate);
    }
    
    /**
     * @param toTranslate
     *            la string a traduire
     * @return ...
     */
    private static char translateKennyLetterToNormalLetter(String toTranslate) {
        if (KENNY_TO_NORMAL.isEmpty()) {
            loadKenny();
        }
        if (toTranslate.length() != KENNY_LETTER_LEN) {
            throw new IllegalArgumentException(toTranslate + " n'est pas en Kennyspeak");
        }
        char retour = KENNY_TO_NORMAL.get(toTranslate.toLowerCase(Locale.FRANCE)).charValue();
        if (Character.isUpperCase(toTranslate.charAt(0))) {
            return Character.toUpperCase(retour);
        }
        return retour;
    }
    
    /**
     * @param toTranslate
     * @return ...
     */
    private static StringBuilder translateKennyToNormal(String toTranslate) {
        StringBuilder result = new StringBuilder();
        int index = 0;
        while (index < toTranslate.length()) {
            while (Character.toUpperCase(toTranslate.charAt(index)) != 'M'
                    && Character.toUpperCase(toTranslate.charAt(index)) != 'P'
                    && Character.toUpperCase(toTranslate.charAt(index)) != 'F') {
                result.append(toTranslate.charAt(index));
                ++index;
                if (toTranslate.length() == index) {
                    return result;
                }
            }
            try {
                result
                        .append(translateKennyLetterToNormalLetter(toTranslate.substring(index, index
                                + KENNY_LETTER_LEN)));
            } catch (IllegalArgumentException iae) {
                LOG.error(iae, iae);
            }
            index += KENNY_LETTER_LEN;
        }
        return result;
    }
    
    /**
     * @param ch
     *            un caractere.
     * @return une kennylettre.
     */
    private static String translateNormalLetterToKennyLetter(char ch) {
        if (NORMAL_TO_KENNY.isEmpty()) {
            loadKenny();
        }
        String retour = NORMAL_TO_KENNY.get(Character.valueOf(Character.toLowerCase(ch)));
        if (Character.isUpperCase(ch)) {
            return StringUtils.capitalize(retour);
        }
        return retour;
    }
    /**
     * @param toTranslate
     *            un string
     * @return ...
     */
    private static StringBuilder translateNormalToKenny(String toTranslate) {
        StringBuilder result = new StringBuilder();
        for (char ch : toTranslate.toCharArray()) {
            /* Ordered according to optimisations suggested by Stuart */
            if (ch < 'A' || ch > 'Z' && ch < 'a' || ch > 'z') {
                result.append(ch);
            } else {
                result.append(translateNormalLetterToKennyLetter(ch));
            }
        }
        return result;
    }
    
    /**
     * @param trigger
     *            le trigger
     */
    public KennyTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        String args = getArgs(im.getMessage());
        if (StringUtils.isBlank(args)) {
            String kennyResponse = translate(args).toString();
            Privmsg retour = Privmsg.buildAnswer(im, kennyResponse);
            control.sendMsg(retour);
        } else {
            Privmsg retour = Privmsg.buildAnswer(im, getTriggerHelp());
            control.sendMsg(retour);
        }
        
    }
}
