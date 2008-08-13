package net.mauhiz.irc.bot.event;

import net.mauhiz.irc.DateUtils;
import net.mauhiz.irc.base.Color;
import net.mauhiz.irc.base.ColorUtils;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcUser;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Gather extends ChannelEvent {
    /**
     * séparateur entre l'affichage des différents membres du gather.
     */
    private static final String DISPLAY_SEPARATOR = " - ";
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(Gather.class);
    /**
     * La taille d'un gather.
     */
    private static final byte SIZE = 5;
    /**
     * @param channel1
     *            le channel
     */
    private final SeekWar seekWar = new SeekWar();
    // /**
    // * Un serveur?
    // */
    // private Server serv;
    /**
     * le temps où je commence.
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
    public Gather(final Channel channel1) {
        this(channel1, "eule^");
    }
    
    /**
     * @param tag
     *            le tag
     * @param channel1
     *            le channel
     */
    public Gather(final Channel channel1, final String tag) {
        super(channel1);
        sw.start();
        team = new Team(SIZE, tag);
    }
    
    /**
     * @param element
     *            l'element
     * @return le message d'ajout, jamais <code>null</code>.
     */
    public final String add(final IrcUser element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
        final StrBuilder retour = new StrBuilder(String.valueOf(element));
        if (team.isFull()) {
            retour.insert(0, "Désolé ").append(", c'est complet");
        } else if (team.contains(element)) {
            retour.append(": tu es déjà inscrit.");
        } else {
            team.add(element);
            retour.append(" ajouté au gather. ");
            switch (team.remainingPlaces()) {
                case 0 :
                    retour.append("C'est complet! Ready to rock 'n $roll");
                    break;
                case 1 :
                    retour.append("Reste une seule place!");
                    break;
                default :
                    retour.append("Reste ").append(team.remainingPlaces()).append(" places.");
                    break;
            }
        }
        return retour.toString();
    }
    
    /**
     * @return le seekWar
     */
    public final SeekWar getSeek() {
        return seekWar;
    }
    
    // /**
    // * @return l'ip du serv
    // */
    // public final String getServ() {
    // if (null == this.serv) {
    // return "serv off :/";
    // }
    // return new StrBuilder("IP:
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
    // public final boolean haveServ() {
    // return this.serv != null;
    // }
    /**
     * @param element
     *            un type à virer
     * @return un message
     */
    public final String remove(final IrcUser element) {
        if (team.remove(element)) {
            return element + " a été retiré du gather.";
        }
        return element + ": tu n'étais pas inscrit tfaçon.";
    }
    
    /**
     * @return un pauvre mec pris au hasard
     */
    public final String roll() {
        if (team.isEmpty()) {
            return "Personne n'est inscrit au gather.";
        }
        return "Plouf, plouf, ce sera " + team.get(RandomUtils.nextInt(team.size())) + " qui ira seek!";
    }
    
    /**
     * @param string
     *            le nouveau tag
     * @return un msg
     */
    public final String setTag(final String string) {
        team.setNom(string);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Nouveau tag = " + team);
        }
        return "Nouveau tag : " + team;
    }
    
    /**
     * @return un message
     */
    @Override
    public final String toString() {
        final StrBuilder temp = new StrBuilder(ColorUtils.toColor("Gather " + team.size() + '/' + team.getCapacity(),
                Color.BROWN));
        temp.append(" (start: ");
        temp.append(ColorUtils.toColor(DateUtils.getTimeStamp(sw), Color.GREEN));
        temp.append(") (tag: ");
        temp.append(ColorUtils.toColor(team.toString(), Color.RED));
        temp.append(") ");
        if (team.isEmpty()) {
            return temp.toString();
        }
        for (final IrcUser ircUser : team) {
            temp.append(ircUser).append(DISPLAY_SEPARATOR);
        }
        return temp.substring(0, temp.length() - DISPLAY_SEPARATOR.length());
    }
}
