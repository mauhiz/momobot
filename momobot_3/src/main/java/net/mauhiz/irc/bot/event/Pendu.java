package net.mauhiz.irc.bot.event;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.util.FileUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * @author mauhiz
 */
public class Pendu extends AbstractChannelEvent {
    /**
     * un dictionnaire de mots.
     */
    private static final List<String> DICO = new LinkedList<String>();
    /**
     * le caractere underscore.
     */
    private static final char UNDERSCORE = '_';
    static {
        try {
            List<String> lignes = FileUtil.readFileInCp("dico.txt", FileUtil.UTF8);
            DICO.addAll(lignes);
        } catch (IOException ioe) {
            throw new ExceptionInInitializerError(ioe);
        }
    }

    /**
     * @param len
     *            la longueur du mot
     * @return le nombre de vies
     */
    private static int calibre(int len) {
        switch (len) {
            case 0:
                throw new IllegalArgumentException("Invalid length: " + len);
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
        String mot = DICO.get(RandomUtils.nextInt(DICO.size()));
        /* pas de mots composes */
        if (mot.indexOf('-') > 0) {
            return getNextMot();
        }
        return mot;
    }

    /**
     * lettres deja proposees.
     */
    private final Set<Character> alreadyTried = new TreeSet<Character>();
    /**
     * le mot en cours de devinage (negatif).
     */
    private final StringBuilder devinage = new StringBuilder();
    /**
     * le mot en cours de devinage (positif).
     */
    private final StringBuilder mot = new StringBuilder();
    /**
     * la solution.
     */
    private final String solution;
    /**
     * la solution avec les accents.
     */
    private final String solutionPure;
    /**
     * le nombre de vies.
     */
    private int vies;

    /**
     * @param channel1
     *            le channel
     */
    public Pendu(IrcChannel channel1) {
        super(channel1);
        solutionPure = getNextMot();
        solution = MomoStringUtils.effaceAccents(solutionPure);
        mot.append(solution);
        vies = calibre(mot.length());
        devinage.append(StringUtils.rightPad("", mot.length(), UNDERSCORE));
        LOG.debug(solutionPure);
    }

    /**
     * @param car
     *            la lettre
     * @return si la lettre est present ans le mot
     */
    public boolean findLetter(char car) {
        boolean present = false;
        for (int index = 0; index < solution.length(); index++) {
            if (car == mot.charAt(index)) {
                present = true;
                devinage.setCharAt(index, solutionPure.charAt(index));
                mot.setCharAt(index, UNDERSCORE);
            }
        }
        return present;
    }

    /**
     * @return Returns the devinage.
     */
    public String getDevinage() {
        return devinage.toString();
    }

    /**
     * @param toSubmit
     *            la lettre
     * @return un message
     */
    public String submitLettre(char toSubmit) {
        if (Character.isLetter(toSubmit)) {
            if (!alreadyTried.add(Character.valueOf(toSubmit))) {
                return "La lettre " + toSubmit + " a deja ete essayee";
            }
        } else {
            return "";
        }

        StringBuilder penduMsg = new StringBuilder();
        penduMsg.append("La lettre ").append(toSubmit).append(" est ");
        if (findLetter(toSubmit)) {
            penduMsg.append("presente! ");
            if (devinage.toString().equals(solutionPure)) {
                /* arreter le pendu */
                stop();
                penduMsg.append("Bravo! Vous avez trouve le mot ").append(solutionPure).append(". Il vous restait ")
                        .append(vies).append('.');
            } else {
                penduMsg.append("-> ").append(devinage);
            }
        } else {
            penduMsg.append("absente! ");
            switch (--vies) {
                case 0:
                    /* arreter le pendu */
                    stop();
                    penduMsg.append("Vous avez perdu! le mot etait: ").append(solutionPure);
                    break;
                case 1:
                    penduMsg.append("Plus qu'un essai!");
                    break;
                default:
                    penduMsg.append("Encore ").append(vies).append(" essais.");
            }
        }
        return penduMsg.toString();
    }

    /**
     * @param test
     *            le mot a tester
     * @return un msg
     */
    public StringBuilder submitMot(String test) {
        StringBuilder retour = new StringBuilder();
        if (!StringUtils.isAlpha(test) || test.length() != solution.length()) {
            return retour;
        }
        if (test.equals(solution) || test.equals(solutionPure)) {
            retour.append("Bravo! Vous avez trouve le mot ").append(solutionPure);
            /* arreter le pendu */
            stop();
        } else {
            retour.append("Non, le mot n'est pas: ").append(test).append(". ");
            switch (--vies) {
                case 0:
                    stop();
                    retour.append("Vous avez perdu! le mot etait: ").append(solutionPure);
                    break;
                /* arreter le pendu */
                case 1:
                    retour.append("Plus qu'un essai!");
                    break;
                default:
                    retour.append("Encore ").append(vies).append(" essais.");
            }
        }
        return retour;
    }

    /**
     * @see net.mauhiz.irc.bot.event.AbstractChannelEvent#toString()
     */
    @Override
    public String toString() {
        return "-> " + devinage;
    }
}
// TODO : ajouter comptage de points (ou pas?)
