package momobot.trigger.fun;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import momobot.MomoBot;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * Transforme un texte en KennySpeak.
 * @author mauhiz
 */
public class KennyTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * longueur d'une kennylettre.
     */
    private static final byte                      KENNY_LETTER_LEN = 3;
    /**
     * The KENNYLETTERS in alphabetical order. Big Letters are the Same with the only difference That the First char is UpperCase
     */
    private static final Map < String, Character > KENNY_TO_NORMAL  = new TreeMap < String, Character >();
    /**
     * LOG.
     */
    private static final Logger                    LOG              = Logger.getLogger(KennyTrigger.class);
    /**
     * 
     */
    private static final Map < Character, String > NORMAL_TO_KENNY  = new TreeMap < Character, String >();

    /**
     * @param toTest
     *            la chaine a tester
     * @return ...
     */
    private static boolean isKenny(final String toTest) {
        char ch;
        for (final char loopChar : toTest.toCharArray()) {
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
     * La map kenny est formé ainsi...
     * 
     * <pre>
     * a mpf\r\n
     * </pre>
     */
    private static void loadKenny() {
        try {
            final List < ? > lignes = FileUtils.readLines(new File("resources" + File.separatorChar + "kenny_map.txt"),
                    "ASCII");
            if (!KENNY_TO_NORMAL.isEmpty()) {
                KENNY_TO_NORMAL.clear();
            }
            if (!NORMAL_TO_KENNY.isEmpty()) {
                NORMAL_TO_KENNY.clear();
            }
            Character chara;
            String mpf;
            for (final Object ligne : lignes) {
                chara = Character.valueOf(((String) ligne).charAt(0));
                mpf = ((String) ligne).substring(2);
                NORMAL_TO_KENNY.put(chara, mpf);
                KENNY_TO_NORMAL.put(mpf, chara);
            }
        } catch (final IOException ioe) {
            LOG.warn(ioe, ioe);
        }
    }

    /**
     * @param toTranslate
     *            String to Translate
     * @return translated String
     */
    public static StrBuilder translate(final String toTranslate) {
        if (isKenny(toTranslate)) {
            return translateKennyToNormal(toTranslate);
        }
        return translateNormalToKenny(toTranslate);
    }

    /**
     * @param toTranslate
     *            la string à traduire
     * @return ...
     */
    private static char translateKennyLetterToNormalLetter(final String toTranslate) {
        if (KENNY_TO_NORMAL.isEmpty()) {
            loadKenny();
        }
        if (toTranslate.length() != KENNY_LETTER_LEN) {
            throw new IllegalArgumentException(toTranslate + " n'est pas en Kennyspeak");
        }
        final char retour = KENNY_TO_NORMAL.get(toTranslate.toLowerCase(Locale.FRANCE)).charValue();
        if (Character.isUpperCase(toTranslate.charAt(0))) {
            return Character.toUpperCase(retour);
        }
        return retour;
    }

    /**
     * @param toTranslate
     * @return ...
     */
    private static StrBuilder translateKennyToNormal(final String toTranslate) {
        final StrBuilder result = new StrBuilder();
        int index = 0;
        while (index < toTranslate.length()) {
            while (Character.toUpperCase(toTranslate.charAt(index)) != 'M' &&
                    Character.toUpperCase(toTranslate.charAt(index)) != 'P' &&
                    Character.toUpperCase(toTranslate.charAt(index)) != 'F') {
                result.append(toTranslate.charAt(index));
                ++index;
                if (toTranslate.length() == index) {
                    return result;
                }
            }
            try {
                result
                        .append(translateKennyLetterToNormalLetter(toTranslate.substring(index, index +
                                KENNY_LETTER_LEN)));
            } catch (final IllegalArgumentException iae) {
                LOG.error(iae, iae);
            }
            index += KENNY_LETTER_LEN;
        }
        return result;
    }

    /**
     * @param ch
     *            un caractère.
     * @return une kennylettre.
     */
    private static String translateNormalLetterToKennyLetter(final char ch) {
        if (NORMAL_TO_KENNY.isEmpty()) {
            loadKenny();
        }
        final String retour = NORMAL_TO_KENNY.get(Character.valueOf(ch));
        if (ch == Character.toUpperCase(ch)) {
            return StringUtils.capitalize(retour);
        }
        return retour;
    }

    /**
     * @param toTranslate
     *            un string
     * @return ...
     */
    private static StrBuilder translateNormalToKenny(final String toTranslate) {
        final StrBuilder result = new StrBuilder();
        for (final char ch : toTranslate.toCharArray()) {
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
    public KennyTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    @Override
    @SuppressWarnings("unused")
    public void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (test(message)) {
            MomoBot.getBotInstance().sendMessage(channel, translate(getArgs(message)).toString());
        }
    }
}
