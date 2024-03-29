package momobot.event.channel;

import ircbot.AChannelEvent;
import ircbot.AColors;
import ircbot.Channel;
import ircbot.IrcUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import momobot.cs.Server;
import org.apache.log4j.Logger;
import utils.Utils;

/**
 * @author Administrator
 */
public class Gather extends AChannelEvent {
    /**
     * un g�n�rateur al�atoire.
     */
    private static final Random    ALEA      = new Random();
    /**
     * logger.
     */
    private static final Logger    LOG       = Logger.getLogger(Gather.class);
    /**
     * La taille d'un gather.
     */
    private static final short     SIZE      = 5;
    /**
     * Un serveur?
     */
    private Server                 serv      = null;
    /**
     * le temps o� je commence.
     */
    private final String           startTime = Utils.getTimeStamp();
    /**
     * le tag.
     */
    private String                 tag       = "eule^";
    /**
     * l'ensemble de joueurs.
     */
    private final List < IrcUser > team      = new ArrayList < IrcUser >(SIZE);

    /**
     * @param channel1
     *            le channel
     */
    public Gather(final Channel channel1) {
        super(channel1);
    }

    /**
     * @param tag1
     *            le tag
     * @param channel1
     *            le channel
     */
    public Gather(final Channel channel1, final String tag1) {
        super(channel1);
        this.tag = tag1;
    }

    /**
     * @param element
     *            l'element
     * @return rien de bien interessant
     */
    public final StringBuffer add(final IrcUser element) {
        final StringBuffer retour = new StringBuffer(element.getNick());
        if (isFull()) {
            return retour.insert(0, "D�sol� ").append(", c'est complet");
        }
        if (this.team.contains(element)) {
            return retour.append(": tu es d�j� inscrit.");
        }
        this.team.add(element);
        final int k = SIZE - this.team.size();
        retour.append(" ajout� au gather. ");
        switch (k) {
            case 0:
                return retour.append("C'est complet!");
            case 1:
                return retour.append("Reste une seule place!");
            default:
        }
        return retour.append("Reste ").append(k).append(" places.");
    }

    /**
     * @return l'ip du serv
     */
    public final String getServ() {
        if (!haveServ()) {
            return "serv off :/";
        }
        return "IP: " + this.serv.getIp() + ':' + this.serv.getPort()
                + " pass: " + this.serv.getPass();
    }

    /*
     * public void setServ(String ipay, String pass){ serv =
     * NetUtils.findServ(ipay); if (serv == null) return; serv.pass = pass; }
     */
    /**
     * @return si on a un serv
     */
    public final boolean haveServ() {
        return this.serv != null;
    }

    /**
     * @return si c'est full
     */
    public final boolean isFull() {
        return this.team.size() == SIZE;
    }

    /**
     * @param element
     *            un type � virer
     * @return un message
     */
    public final String remove(final IrcUser element) {
        if (this.team.remove(element)) {
            return element + " a �t� retir� du gather.";
        }
        return element + ": tu n'�tais pas inscrit tfa�on.";
    }

    /**
     * @return un pauvre mec pris au hasard
     */
    public final String roll() {
        if (this.team.isEmpty()) {
            return "Personne n'est inscrit au gather.";
        }
        return "Plouf, plouf, ce sera "
                + this.team.get(ALEA.nextInt(this.team.size()))
                + " qui ira seek!";
    }

    /**
     * @param string
     *            le nouveau tag
     * @return un msg
     */
    public final String setTag(final String string) {
        this.tag = string;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Nouveau tag = " + this.tag);
        }
        return "Nouveau tag : " + this.tag;
    }

    /**
     * @return un message
     */
    @Override
    public final String status() {
        final StringBuffer temp = new StringBuffer();
        temp.append(AColors.toColor("Gather " + this.team.size() + '/' + SIZE,
                AColors.BROWN));
        temp.append(" (start: ");
        temp.append(AColors.toColor(this.startTime, AColors.GREEN));
        temp.append(") (tag: ");
        temp.append(AColors.toColor(this.tag, AColors.RED));
        temp.append(") ");
        if (this.team.isEmpty()) {
            return temp.toString();
        }
        for (final IrcUser ircUser : this.team) {
            temp.append(ircUser).append(" - ");
        }
        return temp.substring(0, temp.length() - 3);
    }
}
