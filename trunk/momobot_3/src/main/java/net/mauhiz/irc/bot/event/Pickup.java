package net.mauhiz.irc.bot.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcUser;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
public class Pickup extends ChannelEvent {
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
    private final List<Team> teams = new ArrayList<Team>(NB_TEAMS);
    
    // private Server serv = null;
    /**
     * @param channel1
     *            le channel
     */
    public Pickup(final Channel channel1) {
        super(channel1);
        for (char nom = 'a'; teams.size() < NB_TEAMS; ++nom) {
            teams.add(new Team(SIZE, Character.toString(nom)));
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
        synchronized (teams) {
            final Team assignedTeam = assignTeam(choix.toLowerCase(Locale.FRANCE));
            final Team currentTeam = getTeam(element);
            if (null == currentTeam) {
                /* element n'est pas encore pr�sent */
                if (null == assignedTeam) {
                    /* element n'a pas choisi de team en particulier */
                    for (final Team tryTeam : teams) {
                        /*
                         * on essaye de le mettre dans la premi�re �quipe qu'on trouve
                         */
                        if (tryTeam.add(element)) {
                            return element + " ajout� a la team " + tryTeam + '.';
                        }
                    }
                    /* on ne doit pas arriver l� */
                    throw new IllegalStateException("J'ai pas r�ussi � ajouter " + element + " au gather.");
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
        synchronized (teams) {
            for (final Team team : teams) {
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
        for (final Team team : teams) {
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
        for (final Team team : teams) {
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
     * @return un $status
     */
    public String shake() {
        /* TODO shaking */
        List<IrcUser> listeUser = new ArrayList<IrcUser>();
        
        // On Ajoute les user dans la liste
        for (final Team team : teams) {
            listeUser.addAll(team);
        }
        
        // RandomUtils.nextInt(team.size())
        int NombrePermutation = RandomUtils.nextInt(listeUser.size()) - 1;
        if (NombrePermutation < 0) {
            NombrePermutation = 0;
        }
        
        // On fait N permutation
        for (int index = 0; index < NombrePermutation; index++) {
            
            IrcUser userTmp = listeUser.get(index);
            listeUser.remove(index);
            
            int NombreIndexPermutation = RandomUtils.nextInt(listeUser.size()) - 1;
            if (NombreIndexPermutation < 0) {
                NombreIndexPermutation = 0;
            }
            
            listeUser.add(NombreIndexPermutation, userTmp);
            
        }
        
        // On re-remplit les teams
        for (final Team team : teams) {
            team.clear();
        }
        int IndexTeam = 0;
        while (!listeUser.isEmpty()) {
            if (IndexTeam > NB_TEAMS - 1) {
                IndexTeam = 0;
            }
            teams.get(IndexTeam).add(listeUser.get(0));
            listeUser.remove(0);
            IndexTeam++;
        }
        
        return "Shake R�ussi!";
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.ChannelEvent#toString()
     */
    @Override
    public final String toString() {
        final StrBuilder retour = new StrBuilder();
        for (final Team team : teams) {
            retour.append("Team " + team + ": ");
            for (final IrcUser user : team) {
                retour.append(user).append(". ");
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
