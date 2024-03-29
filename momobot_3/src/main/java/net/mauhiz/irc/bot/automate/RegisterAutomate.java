package net.mauhiz.irc.bot.automate;

import java.util.Collections;

import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.WhoisRequest;
import net.mauhiz.irc.base.data.qnet.QnetUser;
import net.mauhiz.irc.base.trigger.AbstractAutomate;
import net.mauhiz.irc.bot.triggers.cs.PlayerDB;

import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 *              U : $register
 *              [etape1] B whois U
 *              [etape2] B : quel est ton steamid?
 *              U : STEAM_131231
 * </pre>
 * 
 * @author mauhiz
 */
public class RegisterAutomate extends AbstractAutomate {
    static final int STEAMING = 3;
    static final int WHOISING = 2;

    /**
     * mon SteamID.
     */
    private String steamId;

    /**
     * @param user1
     *            le user
     * @param control1
     * @param server1
     */
    public RegisterAutomate(QnetUser user1, IrcClientControl control1, IIrcServerPeer server1) {
        super(user1, control1, server1, "Register auth");
        etat = STARTED;
    }

    @Override
    protected QnetUser getUser() {
        return (QnetUser) super.getUser();
    }

    /**
     * @param steamid1
     *            le steamid
     */
    public void setSteamId(String steamid1) {
        steamId = steamid1;
    }

    @Override
    public void trun() {
        while (isRunning()) {
            pause(SLEEPTIME);
            switch (etat) {
            case STARTED:
                WhoisRequest.startWhois(getServer(), control, Collections.singleton(getUser().getNick()), null);

                sendMsgToUser("Detection de ton auth Qnet...");
                etat = WHOISING;
                break;
            case WHOISING:
                WhoisRequest whois = WhoisRequest.get(getUser().getNick());
                if (whois != null && whois.isRunning()) {
                    continue;
                }
                if (StringUtils.isEmpty(getUser().getAuth())) {
                    sendMsgToUser("Tu es authe sur Qnet.");
                    sendMsgToUser("Il faut etre auth pour utiliser la commande $register.");
                    return;
                }
                sendMsgToUser("Ton auth Qnet est: " + getUser().getAuth());
                if (PlayerDB.authIsKnown(getUser().getAuth())) {
                    sendMsgToUser("Cet auth est deja associe a une steamid");
                    sendMsgToUser("Pour changer de steamid, commencez par $unregister");
                    return;
                }
                sendMsgToUser("Quel est ton steamID?");
                etat = STEAMING;
                break;
            case STEAMING:
                if (StringUtils.isEmpty(steamId)) {
                    continue;
                }
                if (PlayerDB.steamidIsKnown(steamId)) {
                    sendMsgToUser("Ce steamid est deja associe a un auth");
                    sendMsgToUser("Veuillez contacter le mastah du bot");
                    return;
                }
                // FIXME
                // sendMsgToUser(SqlUtils.registerPlayer(steamid, ((QnetUser) getUser()).getAuth()));
                return;
            default:
                break;
            }
        }
    }
}
