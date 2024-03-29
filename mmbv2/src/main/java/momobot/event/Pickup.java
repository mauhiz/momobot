package momobot.event;

import ircbot.AbstractChannelEvent;
import ircbot.Channel;
import ircbot.IrcUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
public class Pickup extends AbstractChannelEvent {
    /**
     * le nombre de teams.
     */
    private static final byte   NB_TEAMS = 2;
    /**
     * la taille des teams.
     */
    private static final byte   SIZE     = 5;
    /**
     * les teams.
     */
    private final List < Team > teams    = new ArrayList < Team >(NB_TEAMS);

    // private Server serv = null;
    /**
     * @param channel1
     *            le channel
     */
    public Pickup(final Channel channel1) {
        super(channel1);
        for (char nom = 'a'; this.teams.size() < NB_TEAMS; ++nom) {
            this.teams.add(new Team(SIZE, Character.toString(nom)));
        }
    }

    /**
     * @param element
     *            l'�l�ment � ajouter
     * @param choix
     *            le choix
     * @return un msg
     */
    public final String add(final IrcUser element, final String choix) {
        if (isFull()) {
            return "C'est complet!";
        }
        synchronized (this.teams) {
            final Team assignedTeam = assignTeam(choix.toLowerCase(Locale.FRANCE));
            final Team currentTeam = getTeam(element);
            if (null == currentTeam) {
                /* element n'est pas encore pr�sent */
                if (null == assignedTeam) {
                    /* element n'a pas choisi de team en particulier */
                    for (final Team tryTeam : this.teams) {
                        /* on essaye de le mettre dans la premi�re �quipe qu'on trouve */
                        if (tryTeam.add(element)) {
                            return element + " ajout� a la team " + tryTeam + '.';
                        }
                    }
                    /* on ne doit pas arriver l� */
                    throw new AssertionError("J'ai pas r�ussi � ajouter " + element + " au gather.");
                }
                /* pas de probl�me, il a choisi, on l'ajoute */
                assignedTeam.add(element);
                return element + " ajout� a la team " + assignedTeam + '.';
            }
            /* element est d�j� pr�sent et ne veut pas changer d'�quipe */
            if (null == assignedTeam || assignedTeam.equals(currentTeam)) {
                return "Tu es d�j� inscrit dans la team " + currentTeam + '.';
            }
            /* element est d�j� pr�sent et veut changer d'�quipe */
            currentTeam.remove(element);
            assignedTeam.add(element);
            return element + " d�plac� vers la team " + assignedTeam + '.';
        }
    }

    /**
     * @param choix
     *            le choix
     * @return le numero de la team dans laquelle il est autoassign
     */
    public final Team assignTeam(final String choix) {
        synchronized (this.teams) {
            for (final Team team : this.teams) {
                /* le equals est null-safe dans ce sens */
                if (team.toString().equals(choix) && !team.isFull()) {
                    return team;
                }
            }
            return null;
        }
    }

    /**
     * @param element
     *            l'utilisateur � inscrire
     * @return le numero de la team dans laquelle il est inscrit
     */
    public final Team getTeam(final IrcUser element) {
        for (final Team team : this.teams) {
            if (team.contains(element)) {
                return team;
            }
        }
        return null;
    }

    /**
     * @return si le pickup est full
     */
    public final boolean isFull() {
        for (final Team team : this.teams) {
            if (!team.isFull()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param element
     *            �l�ment � retirer
     * @return un msg
     */
    public final String remove(final IrcUser element) {
        final Team findTeam = getTeam(element);
        if (null == findTeam) {
            return element + ": tu n'�tais pas inscrit.";
        }
        findTeam.remove(element);
        return element + " retir� de la team " + findTeam + '.';
    }

    /**
     * @return un msg
     */
    @Override
    public final String toString() {
        final StrBuilder retour = new StrBuilder();
        for (final Team team : this.teams) {
            retour.append("Team " + team + ": ");
            for (final IrcUser user : team) {
                retour.append(user).append(". ");
            }
        }
        return retour.toString();
    }
    // public String getServ() {
    // return "Serv: " + serv.getIp() + ":" + serv.getPort() + " - Pass: " + serv.pass;
    // }
    //
    // public void setServ(String ipay, String pass, String rcon) {
    // serv = NetUtils.findServ(ipay);
    // if (null == serv)
    // return;
    // serv.pass = pass;
    // serv.setRcon(rcon);
    // }
    //
    // public void setServ(String ipay, String pass) {
    // setServ(ipay, pass, null);
    // }
}
