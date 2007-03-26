package momobot.event.channel;

import ircbot.AChannelEvent;
import ircbot.Channel;
import ircbot.IrcUser;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Administrator
 */
public class Pickup extends AChannelEvent {
    /**
     * le nombre de teams.
     */
    private static final short             NBTEAMS = 2;
    /**
     * les noms de teams.
     */
    private final String[]                 nom     = new String[NBTEAMS];
    /**
     * la taille des teams.
     */
    private final short                    size    = 5;
    /**
     * les teams.
     */
    @SuppressWarnings("unchecked")
    private final Collection < IrcUser >[] team    = new Collection[NBTEAMS];

    // private Server serv = null;
    /**
     * @param channel1
     *            le channel
     */
    public Pickup(final Channel channel1) {
        super(channel1);
        for (int k = 0; k < NBTEAMS; k++) {
            this.nom[k] = "" + (char) ('a' + k);
            this.team[k] = new ArrayList < IrcUser >(this.size);
        }
    }

    /**
     * @param element
     *            l'élément à ajouter
     * @param choix
     *            le choix
     * @return un msg
     */
    public final String add(final IrcUser element, final String choix) {
        if (isFull()) {
            return "C'est complet!";
        }
        final int j = determine(choix.toLowerCase());
        final int k = estInscrit(element);
        if (k == NBTEAMS) {
            if (j == NBTEAMS) {
                for (int i = 0; i < NBTEAMS; i++) {
                    if (this.team[i].add(element)) {
                        return element + " ajouté a la team " + this.nom[i]
                                + '.';
                    }
                }
            }
            this.team[j].add(element);
            return element + " ajouté a la team " + this.nom[j] + '.';
        }
        if (j == k || j == NBTEAMS) {
            return "Tu es déjà inscrit dans la team " + this.nom[k] + '.';
        }
        this.team[k].remove(element);
        this.team[j].add(element);
        return element + " deplacé vers la team " + this.nom[j] + '.';
    }

    /**
     * @param choix
     *            le choix
     * @return le numero de la team dans laquelle il est autoassign
     */
    public final int determine(final String choix) {
        for (int k = 0; k < NBTEAMS; k++) {
            if (choix.equals(this.nom[k]) && this.team[k].size() < this.size) {
                return k;
            }
        }
        return NBTEAMS;
    }

    /**
     * @param element
     *            l'utilisateur à inscrire
     * @return le numero de la team dans laquelle il est inscrit
     */
    public final int estInscrit(final IrcUser element) {
        for (int k = 0; k < NBTEAMS; k++) {
            if (this.team[k].contains(element)) {
                return k;
            }
        }
        return NBTEAMS;
    }

    /**
     * @return si le pickup est full
     */
    public final boolean isFull() {
        int j = 0;
        for (int k = 0; k < NBTEAMS; k++) {
            j += this.team[k].size();
        }
        return j == NBTEAMS * this.size;
    }

    /**
     * @param element
     *            élément à retirer
     * @return un msg
     */
    public final String remove(final IrcUser element) {
        final int k = estInscrit(element);
        if (k == NBTEAMS) {
            return element + ": tu n'étais pas inscrit.";
        }
        this.team[k].remove(element);
        return element + " retiré de la team " + this.nom[k] + '.';
    }

    /**
     * @return un msg
     */
    @Override
    public final String status() {
        final StringBuffer temp = new StringBuffer();
        for (int k = 0; k < NBTEAMS; k++) {
            temp.append("Team " + this.nom[k] + ": ");
            for (IrcUser u : this.team[k]) {
                temp.append(u).append(". ");
            }
        }
        return temp.toString();
    }
    // public String getServ() { return "Serv: " + serv.getIp() + ":" +
    // serv.getPort() + " - Pass: " + serv.pass; }
    //
    // public void setServ(String ipay, String pass, String rcon) {
    // serv = NetUtils.findServ(ipay);
    // if (serv == null) return; serv.pass = pass;
    // serv.setRcon(rcon); }
    //
    // public void setServ(String ipay, String pass) { setServ(ipay, pass, "");
    // }
}
