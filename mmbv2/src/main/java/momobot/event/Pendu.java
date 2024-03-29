package momobot.event;

import ircbot.AbstractChannelEvent;
import ircbot.Channel;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

import utils.MomoStringUtils;

/**
 * @author mauhiz
 */
@SuppressWarnings("unchecked")
public class Pendu extends AbstractChannelEvent {
    /**
     * un dictionnaire de mots.
     */
    private static final List < String > DICO       = new LinkedList < String >();
    /**
     * logger.
     */
    private static final Logger          LOG        = Logger.getLogger(Pendu.class);
    /**
     * le caract�re underscore.
     */
    private static final char            UNDERSCORE = '_';
    static {
        try {
            DICO.addAll(FileUtils.readLines(new File("resources" + File.separatorChar + "dico.txt"), "ASCII"));
        } catch (final IOException ioe) {
            LOG.error(ioe, ioe);
        }
    }

    /**
     * @param len
     *            la longueur du mot
     * @return le nombre de vies
     */
    private static byte calibre(final int len) {
        switch (len) {
            case 0:
                throw new IllegalArgumentException();
            case 1:
                return 15;
            case 2:
                return 14;
            case 3:
                return 12;
            case 4:
                return 7;
            case 5:
                return 6;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return 5;
            default:
                break;
        }
        return 4;
    }

    /**
     * @return un mot au hasard
     */
    private static String getNextMot() {
        final String mot = DICO.get(RandomUtils.nextInt(DICO.size()));
        /* pas de mots compos�s */
        if (mot.indexOf('-') > 0) {
            return getNextMot();
        }
        return mot;
    }

    /**
     * le mot en cours de devinage (n�gatif).
     */
    private final StrBuilder devinage = new StrBuilder();
    /**
     * le mot en cours de devinage (positif).
     */
    private final StrBuilder mot      = new StrBuilder();
    /**
     * la solution.
     */
    private final String     solution;
    /**
     * la solution avec les accents.
     */
    private final String     solutionPure;
    /**
     * le nombre de vies.
     */
    private byte             vies;

    /**
     * @param channel1
     *            le channel
     */
    public Pendu(final Channel channel1) {
        super(channel1);
        this.solutionPure = getNextMot();
        this.solution = MomoStringUtils.effaceAccents(this.solutionPure);
        this.mot.append(this.solution);
        this.vies = calibre(this.mot.length());
        for (byte k = 0; k < this.mot.length(); ++k) {
            this.devinage.append(UNDERSCORE);
        }
        LOG.debug(this.solutionPure);
    }

    /**
     * @param car
     *            la lettre
     * @return si la lettre est present ans le mot
     */
    public final boolean findLetter(final char car) {
        boolean present = false;
        for (byte index = 0; index < this.solution.length(); ++index) {
            if (car == this.mot.charAt(index)) {
                present = true;
                this.devinage.setCharAt(index, this.solutionPure.charAt(index));
                this.mot.setCharAt(index, UNDERSCORE);
            }
        }
        return present;
    }

    /**
     * @return Returns the devinage.
     */
    public final String getDevinage() {
        return this.devinage.toString();
    }

    /**
     * @param toSubmit
     *            la lettre
     * @return un message
     */
    public final StrBuilder submitLettre(final char toSubmit) {
        final StrBuilder penduMsg = new StrBuilder();
        if (!Character.isLetter(toSubmit)) {
            return penduMsg;
        }
        penduMsg.append("La lettre ").append(toSubmit).append(" est ");
        if (findLetter(toSubmit)) {
            penduMsg.append("pr�sente! ");
            if (this.devinage.toString().equals(this.solutionPure)) {
                /* arr�ter le pendu */
                stop();
                penduMsg.append("Bravo! Vous avez trouv� le mot ").append(this.solutionPure).append('.');
            } else {
                penduMsg.append("-> ").append(this.devinage);
            }
        } else {
            penduMsg.append("absente! ");
            switch (--this.vies) {
                case 0:
                    /* arr�ter le pendu */
                    stop();
                    penduMsg.append("Vous avez perdu! le mot �tait: ").append(this.solutionPure);
                    break;
                case 1:
                    penduMsg.append("Plus qu'un essai!");
                    break;
                default:
                    penduMsg.append("Encore ").append(this.vies).append(" essais.");
            }
        }
        return penduMsg;
    }

    /**
     * @param test
     *            le mot � tester
     * @return un msg
     */
    public final StrBuilder submitMot(final String test) {
        final StrBuilder retour = new StrBuilder();
        if (!StringUtils.isAlpha(test) || test.length() != this.solution.length()) {
            return retour;
        }
        if (test.equals(this.solution) || test.equals(this.solutionPure)) {
            retour.append("Bravo! Vous avez trouv� le mot ").append(this.solutionPure);
            /* arr�ter le pendu */
            stop();
        } else {
            retour.append("Non, le mot n'est pas: ").append(test).append(". ");
            switch (--this.vies) {
                case 0:
                    stop();
                    retour.append("Vous avez perdu! le mot �tait: ").append(this.solutionPure);
                    break;
                /* arr�ter le pendu */
                case 1:
                    retour.append("Plus qu'un essai!");
                    break;
                default:
                    retour.append("Encore ").append(this.vies).append(" essais.");
            }
        }
        return retour;
    }

    /**
     * @return un msg
     * @see ircbot.AbstractChannelEvent#toString()
     */
    @Override
    public final String toString() {
        return "-> " + this.devinage;
    }
}
// TODO : ajouter comptage de points (ou pas?)
