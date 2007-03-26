package momobot.event.channel;

import ircbot.AChannelEvent;
import java.io.File;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import utils.MyStringUtils;

/**
 * @author Administrator
 */
public class Pendu extends AChannelEvent {
    /**
     * logger.
     */
    private static final Logger    LOG        = Logger.getLogger(Pendu.class);
    /**
     * un générateur de nombres aléatoires.
     */
    private static final Random    ALEA       = new Random();
    /**
     * un dictionnaire de mots.
     */
    private static List < String > dico       = null;
    /**
     * la taille de mon dictionnaire.
     */
    private static int             dicosize   = 0;
    /**
     * le caractère underscore.
     */
    private static final char      UNDERSCORE = '_';

    /**
     * @param len
     *            la longueur du mot
     * @return le nombre de vies
     */
    private static int calibre(final int len) {
        switch (len) {
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
                return 4;
        }
    }

    /**
     * @return un mot au hasard
     */
    private static String getNextMot() {
        final String mot = dico.get(ALEA.nextInt(dicosize));
        /* pas de mots composés */
        if (mot.indexOf("-") > 0) {
            return getNextMot();
        }
        return mot;
    }
    /**
     * le mot en cours de devinage (négatif).
     */
    private final StringBuffer devinage = new StringBuffer();
    /**
     * Le nombre d'échecs.
     */
    private int                failures = 0;
    /**
     * le mot en cours de devinage (positif).
     */
    private final StringBuffer mot      = new StringBuffer();
    /**
     * la solution.
     */
    private final String       solution;
    /**
     * la solution avec les accents.
     */
    private final String       solutionPure;
    /**
     * le nombre de vies.
     */
    private int                vies     = 0;

    /**
     * @param channel1
     *            le channel
     */
    @SuppressWarnings("unchecked")
    public Pendu(final String channel1) {
        super(channel1);
        if (dico == null) {
            try {
                dico = FileUtils.readLines(new File("dico.txt"));
                dicosize = dico.size();
            } catch (final Exception e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error(e, e);
                }
            }
        }
        this.solutionPure = getNextMot();
        this.solution = MyStringUtils.effaceAccents(this.solutionPure);
        this.mot.append(this.solution);
        this.vies = calibre(this.mot.length());
        for (int k = 0; k < this.mot.length(); k++) {
            this.devinage.append(UNDERSCORE);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(this.solutionPure);
        }
    }

    /**
     * @param c
     *            la lettre
     * @return si la lettre est present ans le mot
     */
    public final boolean findLetter(final char c) {
        boolean present = false;
        for (int k = 0; k < this.solution.length(); k++) {
            if (c == this.mot.charAt(k)) {
                present = true;
                this.devinage.setCharAt(k, this.solutionPure.charAt(k));
                this.mot.setCharAt(k, UNDERSCORE);
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
     * @return un msg
     * @see ircbot.AChannelEvent#status()
     */
    @Override
    public final String status() {
        return "-> " + this.devinage;
    }

    /**
     * @param c
     *            la lettre
     * @return un message
     */
    public final String submitLettre(final char c) {
        if (Character.isLetter(c)) {
            final StringBuffer penduMsg = new StringBuffer("La lettre " + c
                    + " est ");
            final boolean present = findLetter(c);
            if (present) {
                penduMsg.append("présente! ");
                if (this.devinage.equals(this.solutionPure)) {
                    /* arrêter le pendu */
                    stop();
                    return penduMsg.append("Bravo! Vous avez trouvé le mot ")
                            .append(this.solutionPure).append('.').toString();
                }
                return penduMsg.append("-> ").append(this.devinage).toString();
            }
            penduMsg.append("absente! ");
            final int v = this.vies - this.failures++;
            switch (v) {
                case 0:
                    /* arrêter le pendu */
                    stop();
                    return penduMsg.append("Vous avez perdu! le mot était: ")
                            .append(this.solutionPure).toString();
                case 1:
                    return penduMsg.append("Plus qu'un essai!").toString();
                default:
            }
            return penduMsg.append("Encore ").append(v).append(" essais.")
                    .toString();
        }
        return "";
    }

    /**
     * @param test
     *            le mot à tester
     * @return un msg
     */
    public final String submitMot(final String test) {
        if (!StringUtils.isAlpha(test)) {
            return "";
        }
        if (test.length() != this.solution.length()) {
            return "";
        }
        if (test.equals(this.solution) || test.equals(this.solutionPure)) {
            /* arrêter le pendu */
            stop();
            return "Bravo! Vous avez trouvé le mot " + this.solutionPure;
        }
        final int v = this.vies - this.failures++;
        final String penduMsg = "Non, le mot n'est pas: " + test + ". ";
        switch (v) {
            case 0:
                stop();
                return penduMsg + "Vous avez perdu! le mot était: "
                        + this.solutionPure;
                /* arrêter le pendu */
            case 1:
                return penduMsg + "Plus qu'un essai!";
            default:
        }
        return penduMsg + "Encore " + v + " essais.";
    }
}
// TODO : ajouter comptage de points (ou pas?)
