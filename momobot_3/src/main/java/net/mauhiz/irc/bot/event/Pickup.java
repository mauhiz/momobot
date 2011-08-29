package net.mauhiz.irc.bot.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author mauhiz
 */
public class Pickup extends AbstractChannelEvent {
    /**
     * le nombre de teams.
     */
    private static final int NB_TEAMS = 2;
    /**
     * la taille des teams.
     */
    private static final int SIZE = 5;
    /**
     * les teams.
     */
    private final List<Team> teams = new ArrayList<>(NB_TEAMS);

    // private Server serv = null;
    /**
     * @param channel1
     *            le channel
     */
    public Pickup(IrcChannel channel1) {
        super(channel1);
        for (char nom = 'a'; teams.size() < NB_TEAMS; ++nom) {
            teams.add(new Team(SIZE, Character.toString(nom)));
        }
    }

    /**
     * @param user
     *            l'element e ajouter
     * @param choix
     *            le choix
     * @return un msg
     */
    public String add(IrcUser user, String choix) {
        if (isFull()) {
            return "C'est complet!";
        }
        synchronized (teams) {
            Team assignedTeam = assignTeam(choix);
            Team currentTeam = getTeam(user);
            if (null == currentTeam) {
                /* element n'est pas encore present */
                if (null == assignedTeam) {
                    /* element n'a pas choisi de team en particulier */
                    for (Team tryTeam : teams) {
                        /*
                         * on essaye de le mettre dans la premiereequipe qu'on trouve
                         */
                        if (tryTeam.add(user)) {
                            return user.getNick() + " ajoue a la team " + tryTeam.getNom() + '.';
                        }
                    }
                    /* on ne doit pas arriver la */
                    throw new IllegalStateException("J'ai pas reussi a ajouter " + user.getNick() + " au gather.");
                }
                /* pas de probleme, il a choisi, on l'ajoute */
                assignedTeam.add(user);
                return user.getNick() + " ajoute a la team " + assignedTeam.getNom() + '.';
            }
            /* element est deja present et ne veut pas changer d'equipe */
            if (null == assignedTeam || assignedTeam.equals(currentTeam)) {
                return "Tu es deja inscrit dans la team " + currentTeam.getNom() + '.';
            }
            /* element est deja present et veut changer d'equipe */
            currentTeam.remove(user);
            assignedTeam.add(user);
            return user.getNick() + " deplace vers la team " + assignedTeam.getNom() + '.';
        }
    }

    /**
     * @param choix
     *            le choix
     * @return le numero de la team dans laquelle il est autoassign
     */
    public Team assignTeam(String choix) {
        if (choix == null) {
            return null;
        }
        synchronized (teams) {
            for (Team team : teams) {
                /* le equals est null-safe dans ce sens */
                if (team.getNom().equalsIgnoreCase(choix) && !team.isFull()) {
                    return team;
                }
            }
            return null;
        }
    }

    /**
     * @param a
     *            l'utilisateur e inscrire
     * @return le numero de la team dans laquelle il est inscrit
     */
    public Team getTeam(IrcUser a) {
        for (Team team : teams) {
            if (team.contains(a)) {
                return team;
            }
        }
        return null;
    }

    /**
     * @return si le pickup est full
     */
    public boolean isFull() {
        for (Team team : teams) {
            if (!team.isFull()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param element
     *            element e retirer
     * @return un msg
     */
    public String remove(IrcUser element) {
        if (element == null) {
            return "";
        }
        Team findTeam = getTeam(element);
        if (null == findTeam) {
            return element.getNick() + ": tu n'etais pas inscrit.";
        }
        findTeam.remove(element);
        return element.getNick() + " retire de la team " + findTeam.getNom() + '.';
    }

    /**
     * @return un $status
     */
    public String shake() {
        List<IrcUser> listeUser = new ArrayList<>(NB_TEAMS * SIZE);

        synchronized (teams) {

            // On Ajoute les user dans la liste
            for (Team team : teams) {
                listeUser.addAll(team.getMembers());
                team.clear();
            }

            Collections.shuffle(listeUser);

            // On re-remplit les teams
            int indexTeam = 0;
            for (IrcUser next : listeUser) {
                teams.get(indexTeam).add(next);
                indexTeam = ++indexTeam % NB_TEAMS;
            }
        }
        return toString();
    }

    /**
     * @see net.mauhiz.irc.bot.event.AbstractChannelEvent#toString()
     */
    @Override
    public String toString() {
        StringBuilder retour = new StringBuilder();
        for (Team team : teams) {
            retour.append("Team " + team.getNom() + ": ");
            for (IrcUser user : team) {
                retour.append(user.getNick()).append(". ");
            }
        }
        return retour.toString();
    }

    // public String getServ() {
    // return "Serv: " + serv.getIp() + ":" + serv.getPort() + " - Pass: " +
    // serv.pass;
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
