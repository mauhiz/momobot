package momobot.event.channel;

import ircbot.AColors;
import ircbot.AChannelEvent;
import ircbot.IrcUser;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import momobot.cs.Server;
import utils.Utils;

/**
 * @author Administrator
 */
public class Gather extends AChannelEvent {
    /**
     * La taille d'un gather.
     */
    private static final int             SIZE      = 5;
    /**
     * Un serveur?
     */
    private final Server                 serv      = null;
    /**
     * le temps où je commence.
     */
    private final String                 startTime = Utils.getTimeStamp();
    /**
     * le tag.
     */
    private String                       tag;
    /**
     * l'ensemble de joueurs.
     */
    private final Collection < IrcUser > team      = new HashSet < IrcUser >(
                                                           SIZE);

    /**
     * @param channel1
     *            le channel
     */
    public Gather(final String channel1) {
        this(channel1, "eule^");
    }

    /**
     * @param tag1
     *            le tag
     * @param channel1
     *            le channel
     */
    public Gather(final String channel1, final String tag1) {
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
            return retour.insert(0, "Désolé ").append(", c'est complet");
        }
        if (this.team.contains(element)) {
            return retour.append(": tu es déjà inscrit.");
        }
        this.team.add(element);
        final int k = SIZE - this.team.size();
        retour.append(" ajouté au gather. ");
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
        return "IP: " + this.serv.getIp() + ":" + this.serv.getPort()
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
        if (this.team.size() == 0) {
            return "Personne n'est inscrit au gather.";
        }
        final double s = Math.floor(Math.random() * this.team.size());
        final Iterator < IrcUser > ite = this.team.iterator();
        for (int i = 0; i < s; i++) {
            if (!ite.hasNext()) {
                break;
            }
            ite.next();
        }
        return "Plouf, plouf, ce sera " + ite.next() + " qui ira seek!";
    }

    /**
     * @param string
     *            le nouveau tag
     * @return un msg
     */
    public final String setTag(final String string) {
        this.tag = string;
        Utils.log(getClass(), "Nouveau tag = " + this.tag);
        return "Nouveau tag : " + this.tag;
    }

    /**
     * @return un message
     */
    @Override
    public final String status() {
        final StringBuffer temp = new StringBuffer();
        temp.append(AColors.toColor("Gather " + this.team.size() + "/" + SIZE,
                AColors.BROWN));
        temp.append(" (start: ");
        temp.append(AColors.toColor(this.startTime, AColors.GREEN));
        temp.append(") (tag: ");
        temp.append(AColors.toColor(this.tag, AColors.RED));
        temp.append(") ");
        if (this.team.size() == 0) {
            return temp.toString();
        }
        for (final IrcUser ircUser : this.team) {
            temp.append(ircUser).append(" - ");
        }
        return temp.substring(0, temp.length() - 3);
    }
}
