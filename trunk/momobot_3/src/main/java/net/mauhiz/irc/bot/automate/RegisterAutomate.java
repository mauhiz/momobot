package net.mauhiz.irc.bot.automate;

import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.qnet.QnetUser;
import net.mauhiz.irc.base.model.WhoisRequest;
import net.mauhiz.irc.bot.triggers.cs.PlayerDB;

import org.apache.commons.lang.StringUtils;

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
    private String steamid;
    
    /**
     * @param user1
     *            le user
     * @param control1
     * @param server1
     */
    public RegisterAutomate(QnetUser user1, IrcControl control1, IrcServer server1) {
        super(user1, control1, server1);
        etat = STARTED;
    }
    
    /**
     * @see Runnable#run()
     */
    public void run() {
        WhoisRequest whois = null;
        while (isRunning()) {
            pause(SLEEPTIME);
            switch (etat) {
                case STARTED :
                    whois = new WhoisRequest(getUser().getNick(), getServer(), control);
                    whois.startAs("Whois");
                    sendMsgToUser("Detection de ton auth Qnet...");
                    etat = WHOISING;
                    break;
                case WHOISING :
                    if (whois != null && whois.isRunning()) {
                        continue;
                    }
                    if (StringUtils.isEmpty(((QnetUser) getUser()).getAuth())) {
                        sendMsgToUser("Tu es authe sur Qnet.");
                        sendMsgToUser("Il faut etre auth pour utiliser la commande $register.");
                        return;
                    }
                    sendMsgToUser("Ton auth Qnet est: " + ((QnetUser) getUser()).getAuth());
                    if (PlayerDB.authIsKnown(((QnetUser) getUser()).getAuth())) {
                        sendMsgToUser("Cet auth est deja associe a une steamid");
                        sendMsgToUser("Pour changer de steamid, commencez par $unregister");
                        return;
                    }
                    sendMsgToUser("Quel est ton steamID?");
                    etat = STEAMING;
                    break;
                case STEAMING :
                    if (StringUtils.isEmpty(steamid)) {
                        continue;
                    }
                    if (PlayerDB.steamidIsKnown(steamid)) {
                        sendMsgToUser("Ce steamid est deja associe a un auth");
                        sendMsgToUser("Veuillez contacter le mastah du bot");
                        return;
                    }
                    // FIXME
                    // sendMsgToUser(SqlUtils.registerPlayer(steamid, ((QnetUser) getUser()).getAuth()));
                    return;
                default :
                    break;
            }
        }
    }
    
    /**
     * @param steamid1
     *            le steamid
     */
    public void setSteamID(String steamid1) {
        steamid = steamid1;
    }
}
