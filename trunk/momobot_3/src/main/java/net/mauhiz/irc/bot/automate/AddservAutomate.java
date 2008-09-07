package net.mauhiz.irc.bot.automate;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;

/**
 * TODO : le faire.
 * 
 * @author mauhiz
 */
public class AddservAutomate extends Automate {
    /**
     * @param user1
     *            le user
     * @param control
     * @param server
     */
    public AddservAutomate(final IrcUser user1, final IIrcControl control, final IrcServer server) {
        super(user1, control, server);
        setEtat(STARTED);
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public final void run() {
        setRunning(true);
        while (isRunning()) {
            pause(SLEEPTIME);
            switch (getEtat()) {
                case STARTED :
                    break;
                default :
                    break;
            }
        }
    }
}
