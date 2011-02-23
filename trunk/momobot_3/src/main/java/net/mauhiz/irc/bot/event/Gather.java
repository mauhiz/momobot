package net.mauhiz.irc.bot.event;

import net.mauhiz.irc.base.Color;
import net.mauhiz.irc.base.ColorUtils;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.util.DateUtil;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author mauhiz
 */
public class Gather extends ChannelEvent {
    /**
     * La taille d'un gather.
     */
    public static final int DEFAULT_SIZE = 5;
    /**
     * separateur entre l'affichage des differents membres du gather.
     */
    private static final String DISPLAY_SEPARATOR = " - ";

    /**
     * 
     */
    private SeekWar seekWar;
    // /**
    // * Un serveur?
    // */
    // private Server serv;
    /**
     * le temps ou je commence.
     */
    private final StopWatch sw = new StopWatch();
    /**
     * l'ensemble de joueurs. Ne sera jamais <code>null</code>
     */
    private final Team team;

    /**
     * @param channel1
     *            le channel
     */
    public Gather(IrcChannel channel1) {
        this(channel1, "eule^", DEFAULT_SIZE);
    }

    /**
     * @param channel1
     * @param nbPlayers
     */
    public Gather(IrcChannel channel1, int nbPlayers) {
        this(channel1, "eule^", nbPlayers);
    }

    /**
     * @param tag
     *            le tag
     * @param channel1
     *            le channel
     * @param size
     */
    public Gather(IrcChannel channel1, String tag, int size) {
        super(channel1);
        sw.start();
        team = new Team(size, tag);
    }

    /**
     * @param element
     *            l'element
     * @return le message d'ajout, jamais <code>null</code>.
     */
    public String add(IrcUser element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
        StringBuilder retour = new StringBuilder();
        retour.append(element.getNick());

        if (team.contains(element)) {
            retour.append(": tu es deja inscrit.");
        } else if (team.isFull()) {
            retour.insert(0, "Desole ").append(", c'est complet");
        } else {
            team.add(element);
            retour.append(" ajoute au gather. ");
            switch (team.remainingPlaces()) {
                case 0:
                    retour.append("C'est complet! Ready to rock 'n $roll");
                    break;
                case 1:
                    retour.append("Reste une seule place!");
                    break;
                default:
                    retour.append("Reste ").append(team.remainingPlaces()).append(" places.");
                    break;
            }
        }
        return retour.toString();
    }

    /**
     * 
     */
    public SeekWar createSeekWar() {
        if (seekWar == null) {
            seekWar = new SeekWar();
        }
        return seekWar;
    }

    /**
     * @return le nombre de joueur dans la team
     */
    public int getNumberPlayers() {
        return team.size();
    }

    /**
     * @return le seekWar
     */
    public SeekWar getSeek() {
        return seekWar;
    }

    // /**
    // * @return l'ip du serv
    // */
    // public String getServ() {
    // if (null == this.serv) {
    // return "serv off :/";
    // }
    // return new StringBuilder("IP:
    // ").append(this.serv.getIp()).append(':').append(this.serv.getPort()).append(
    // " pass: ").append(this.serv.getPass()).toString();
    // }
    // /*
    // * public void setServ(String ipay, String pass){ serv =
    // NetUtils.findServ(ipay); if (null == serv) return;
    // serv.pass = pass; }
    // */
    // /**
    // * @return si on a un serv
    // */
    // public boolean haveServ() {
    // return this.serv != null;
    // }
    /**
     * @param user
     *            un type a virer
     * @return un message
     */
    public String remove(IrcUser user) {
        if (user == null) {
            return "";
        }
        if (team.remove(user)) {
            return user.getNick() + " a ete retire du gather.";
        }
        return user.getNick() + ": tu n'etais pas inscrit tfacon.";
    }

    /**
     * @return un pauvre mec pris au hasard
     */
    public String roll() {
        if (team.isEmpty()) {
            return "Personne n'est inscrit au gather.";
        }
        return "Plouf, plouf, ce sera " + team.randomPlayer().getNick() + " qui ira seek!";
    }

    /**
     * 
     */
    public void setSeekToNull() {
        seekWar = null;
    }

    /**
     * @param string
     *            le nouveau tag
     * @return un msg
     */
    public String setTag(String string) {
        team.setNom(string);
        LOG.debug("Nouveau tag = " + team.getNom());
        return "Nouveau tag : " + team.getNom();
    }

    /**
     * @return un message
     * @see net.mauhiz.irc.bot.event.ChannelEvent#toString()
     */
    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder(ColorUtils.toColor("Gather " + team.size() + '/' + team.getCapacity(),
                Color.BROWN));
        temp.append(" (start: ");
        temp.append(ColorUtils.toColor(DateUtil.getTimeStamp(sw), Color.DARK_GREEN));
        temp.append(") (tag: ");
        temp.append(ColorUtils.toColor(team.getNom(), Color.RED));
        temp.append(") ");
        if (team.isEmpty()) {
            return temp.toString();
        }
        for (IrcUser ircUser : team) {
            temp.append(ircUser.getNick()).append(DISPLAY_SEPARATOR);
        }
        return temp.substring(0, temp.length() - DISPLAY_SEPARATOR.length());
    }
}
