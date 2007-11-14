package momobot.event;

import ircbot.AbstractChannelEvent;
import ircbot.Channel;
import ircbot.ColorsUtils;
import ircbot.IrcUser;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

import utils.DateUtils;

/**
 * @author mauhiz
 */
public class Gather extends AbstractChannelEvent {
    /**
     * séparateur entre l'affichage des différents membres du gather.
     */
    private static final String DISPLAY_SEPARATOR = " - ";
    /**
     * logger.
     */
    private static final Logger LOG               = Logger.getLogger(Gather.class);
    /**
     * La taille d'un gather.
     */
    private static final byte   SIZE              = 5;
    // /**
    // * Un serveur?
    // */
    // private Server serv;
    /**
     * le temps où je commence.
     */
    private final String        startTime;
    /**
     * l'ensemble de joueurs. Ne sera jamais <code>null</code>
     */
    private final Team          team;

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
        this.startTime = DateUtils.getTimeStamp();
        this.team = new Team(SIZE, tag);
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
        if (this.team.isFull()) {
            retour.insert(0, "Désolé ").append(", c'est complet");
        } else if (this.team.contains(element)) {
            retour.append(": tu es déjà inscrit.");
        } else {
            this.team.add(element);
            retour.append(" ajouté au gather. ");
            switch (this.team.remainingPlaces()) {
                case 0 :
                    retour.append("C'est complet! Ready to rock 'n $roll");
                    break;
                case 1 :
                    retour.append("Reste une seule place!");
                    break;
                default :
                    retour.append("Reste ").append(this.team.remainingPlaces()).append(" places.");
                    break;
            }
        }
        return retour.toString();
    }

    // /**
    // * @return l'ip du serv
    // */
    // public final String getServ() {
    // if (null == this.serv) {
    // return "serv off :/";
    // }
    // return new StrBuilder("IP: ").append(this.serv.getIp()).append(':').append(this.serv.getPort()).append(
    // " pass: ").append(this.serv.getPass()).toString();
    // }
    // /*
    // * public void setServ(String ipay, String pass){ serv = NetUtils.findServ(ipay); if (null == serv) return;
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
        if (this.team.remove(element)) {
            return element + " a été retiré du gather.";
        }
        return element + ": tu n'étais pas inscrit tfaçon.";
    }

    /**
     * @return un pauvre mec pris au hasard
     */
    public final String roll() {
        if (this.team.isEmpty()) {
            return "Personne n'est inscrit au gather.";
        }
        return "Plouf, plouf, ce sera " + this.team.get(RandomUtils.nextInt(this.team.size())) + " qui ira seek!";
    }

    /**
     * @param string
     *            le nouveau tag
     * @return un msg
     */
    public final String setTag(final String string) {
        this.team.setNom(string);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Nouveau tag = " + this.team);
        }
        return "Nouveau tag : " + this.team;
    }

    /**
     * @return un message
     */
    @Override
    public final String toString() {
        final StrBuilder temp = new StrBuilder(ColorsUtils.toColor("Gather " + this.team.size() + '/'
                + this.team.getCapacity(), ColorsUtils.BROWN));
        temp.append(" (start: ");
        temp.append(ColorsUtils.toColor(this.startTime, ColorsUtils.GREEN));
        temp.append(") (tag: ");
        temp.append(ColorsUtils.toColor(this.team.toString(), ColorsUtils.RED));
        temp.append(") ");
        if (this.team.isEmpty()) {
            return temp.toString();
        }
        for (final IrcUser ircUser : this.team) {
            temp.append(ircUser).append(DISPLAY_SEPARATOR);
        }
        return temp.substring(0, temp.length() - DISPLAY_SEPARATOR.length());
    }
}
