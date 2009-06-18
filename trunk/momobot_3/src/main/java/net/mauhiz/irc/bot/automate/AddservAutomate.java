package net.mauhiz.irc.bot.automate;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;

/**
 * TODO le faire.
 * 
 * @author mauhiz
 */
public class AddservAutomate extends AbstractAutomate {
    /**
     * @param user1
     *            le user
     * @param control
     * @param server
     */
    public AddservAutomate(IrcUser user1, IIrcControl control, IrcServer server) {
        super(user1, control, server);
        etat = STARTED;
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (isRunning()) {
            pause(SLEEPTIME);
            switch (etat) {
                case STARTED :
                    break;
                default :
                    break;
            }
        }
    }
}
