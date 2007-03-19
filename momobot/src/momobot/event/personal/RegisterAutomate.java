/**
 * <pre>
 *              U : $register
 *              [etape1] B whois U
 *              [etape2] B : quel est ton steamid?
 *              U : STEAM_131231
 * </pre>
 */
package momobot.event.personal;

import ircbot.APersonalEvent;
import ircbot.QnetUser;
import momobot.Db;
import momobot.MomoBot;
import momobot.Whois;

/**
 * @author viper
 */
public class RegisterAutomate extends APersonalEvent {
    /**
     * mon SteamID.
     */
    private String steamid = "";

    // private static final int LIFETIME = ?
    /**
     * mon Whois.
     */
    private Whois  w       = null;

    /**
     * @param user1
     *            le user
     */
    public RegisterAutomate(final QnetUser user1) {
        super(user1);
        setEtat(ETAT.ETAT1);
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public final void run() {
        setRunning(true);
        while (isRunning()) {
            sleep(SLEEPTIME);
            switch (getEtat()) {
                case ETAT1:
                    this.w = new Whois(getUser().getNick(), true, null);
                    this.w.execute();
                    MomoBot.getInstance().sendMessage(getUser().getNick(),
                            "Detection de ton auth Qnet...");
                    setEtat(ETAT.ETAT2);
                    break;
                case ETAT2:
                    if (this.w.isRunning()) {
                        continue;
                    }
                    if (((QnetUser) getUser()).getQnetAuth().length() == 0) {
                        MomoBot.getInstance().sendMessage(getUser().getNick(),
                                "Tu getInstance()s authe sur Qnet.");
                        MomoBot
                                .getInstance()
                                .sendMessage(getUser().getNick(),
                                        "Il faut etre auth pour utiliser la commande $register.");
                        setRunning(false);
                        break;
                    }
                    MomoBot.getInstance().sendMessage(
                            getUser().getNick(),
                            "Ton auth Qnet est "
                                    + ((QnetUser) getUser()).getQnetAuth());
                    if (Db.authIsKnown(((QnetUser) getUser()).getQnetAuth())) {
                        MomoBot.getInstance().sendMessage(getUser().getNick(),
                                "Cet auth est deja associe a une steamid");
                        MomoBot
                                .getInstance()
                                .sendMessage(getUser().getNick(),
                                        "Pour changer de steamid, getInstance()z par $unregister");
                        setRunning(false);
                        break;
                    }
                    MomoBot.getInstance().sendMessage(getUser().getNick(),
                            "Quel est ton steamID?");
                    setEtat(ETAT.ETAT3);
                    break;
                case ETAT3:
                    if (this.steamid.length() == 0) {
                        continue;
                    }
                    if (Db.steamidIsKnown(this.steamid)) {
                        MomoBot.getInstance().sendMessage(getUser().getNick(),
                                "Ce steamid est déjà associé à un auth");
                        MomoBot.getInstance().sendMessage(getUser().getNick(),
                                "Veuillez contacter le mastah du bot");
                        setRunning(false);
                        break;
                    }
                    MomoBot.getInstance().sendMessage(
                            getUser().getNick(),
                            Db.registerPlayer(this.steamid,
                                    ((QnetUser) getUser()).getQnetAuth()));
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
