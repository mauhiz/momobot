/**
 * <pre>
 *              U : $register
 *              [etape1] B whois U
 *              [etape2] B : quel est ton steamid?
 *              U : STEAM_131231
 * </pre>
 */
package net.mauhiz.irc.bot.automate;

import net.mauhiz.irc.SqlUtils;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.qnet.QnetUser;
import net.mauhiz.irc.base.model.WhoisRequest;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class RegisterAutomate extends Automate {
    static final int STEAMING = 3;
    static final int WHOISING = 2;
    /**
     * mon SteamID.
     */
    private String steamid;
    /**
     * mon Whois.
     */
    private WhoisRequest whois;
    
    /**
     * @param user1
     *            le user
     * @param control1
     * @param server1
     */
    public RegisterAutomate(final QnetUser user1, final IrcControl control1, final IrcServer server1) {
        super(user1, control1, server1);
        setEtat(STARTED);
    }
    
    /**
     * @see Runnable#run()
     */
    public final void run() {
        setRunning(true);
        while (isRunning()) {
            pause(SLEEPTIME);
            switch (getEtat()) {
                case STARTED :
                    whois = new WhoisRequest(getUser().getNick(), getServer(), control);
                    whois.execute("Whois");
                    sendMsgToUser("Detection de ton auth Qnet...");
                    setEtat(WHOISING);
                    break;
                case WHOISING :
                    if (whois.isRunning()) {
                        continue;
                    }
                    if (StringUtils.isEmpty(((QnetUser) getUser()).getAuth())) {
                        sendMsgToUser("Tu es authe sur Qnet.");
                        sendMsgToUser("Il faut etre auth pour utiliser la commande $register.");
                        setRunning(false);
                        break;
                    }
                    sendMsgToUser("Ton auth Qnet est: " + ((QnetUser) getUser()).getAuth());
                    if (SqlUtils.authIsKnown(((QnetUser) getUser()).getAuth())) {
                        sendMsgToUser("Cet auth est deja associe a une steamid");
                        sendMsgToUser("Pour changer de steamid, commencez par $unregister");
                        setRunning(false);
                        break;
                    }
                    sendMsgToUser("Quel est ton steamID?");
                    setEtat(STEAMING);
                    break;
                case STEAMING :
                    if (StringUtils.isEmpty(steamid)) {
                        continue;
                    }
                    if (SqlUtils.steamidIsKnown(steamid)) {
                        sendMsgToUser("Ce steamid est déjà associé à un auth");
                        sendMsgToUser("Veuillez contacter le mastah du bot");
                        setRunning(false);
                        break;
                    }
                    sendMsgToUser(SqlUtils.registerPlayer(steamid, ((QnetUser) getUser()).getAuth()));
                    setRunning(false);
                    break;
                default :
                    break;
            }
        }
    }
    
    /**
     * @param steamid1
     *            le steamid
     */
    public final void setSteamID(final String steamid1) {
        steamid = steamid1;
    }
}
