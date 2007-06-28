/**
 * <pre>
 *              U : $register
 *              [etape1] B whois U
 *              [etape2] B : quel est ton steamid?
 *              U : STEAM_131231
 * </pre>
 */
package momobot.automate;

import ircbot.AbstractPersonalEvent;
import ircbot.QnetUser;
import momobot.SqlUtils;
import momobot.MomoBot;
import momobot.whois.Whois;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class RegisterAutomate extends AbstractPersonalEvent {
    /**
     * mon SteamID.
     */
    private String steamid;
    /**
     * mon Whois.
     */
    private Whois  whois;

    /**
     * @param user1
     *            le user
     */
    public RegisterAutomate(final QnetUser user1) {
        super(user1);
        setEtat(ETAT.ETAT1);
    }

    /**
     * @see Runnable#run()
     */
    @Override
    public final void run() {
        setRunning(true);
        while (isRunning()) {
            pause(SLEEPTIME);
            switch (getEtat()) {
                case ETAT1:
                    this.whois = new Whois(getUser(), true, null);
                    this.whois.execute();
                    MomoBot.getBotInstance().sendMessage(getUser(), "Detection de ton auth Qnet...");
                    setEtat(ETAT.ETAT2);
                    break;
                case ETAT2:
                    if (this.whois.isRunning()) {
                        continue;
                    }
                    if (StringUtils.isEmpty(((QnetUser) getUser()).getQnetAuth())) {
                        MomoBot.getBotInstance().sendMessage(getUser(), "Tu es authe sur Qnet.");
                        MomoBot.getBotInstance().sendMessage(getUser(),
                                "Il faut etre auth pour utiliser la commande $register.");
                        setRunning(false);
                        break;
                    }
                    MomoBot.getBotInstance().sendMessage(getUser(),
                            "Ton auth Qnet est: " + ((QnetUser) getUser()).getQnetAuth());
                    if (SqlUtils.authIsKnown(((QnetUser) getUser()).getQnetAuth())) {
                        MomoBot.getBotInstance().sendMessage(getUser(), "Cet auth est deja associe a une steamid");
                        MomoBot.getBotInstance().sendMessage(getUser(),
                                "Pour changer de steamid, commencez par $unregister");
                        setRunning(false);
                        break;
                    }
                    MomoBot.getBotInstance().sendMessage(getUser(), "Quel est ton steamID?");
                    setEtat(ETAT.ETAT3);
                    break;
                case ETAT3:
                    if (StringUtils.isEmpty(this.steamid)) {
                        continue;
                    }
                    if (SqlUtils.steamidIsKnown(this.steamid)) {
                        MomoBot.getBotInstance().sendMessage(getUser(), "Ce steamid est déjà associé à un auth");
                        MomoBot.getBotInstance().sendMessage(getUser(), "Veuillez contacter le mastah du bot");
                        setRunning(false);
                        break;
                    }
                    MomoBot.getBotInstance().sendMessage(getUser(),
                            SqlUtils.registerPlayer(this.steamid, ((QnetUser) getUser()).getQnetAuth()));
                    setRunning(false);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @param steamid1
     *            le steamid
     */
    public final void setSteamID(final String steamid1) {
        this.steamid = steamid1;
    }
}
