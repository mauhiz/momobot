package net.mauhiz.irc.bot.automate;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.trigger.AbstractAutomate;

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
    public AddservAutomate(IrcUser user1, IIrcControl control, IIrcServerPeer server) {
        super(user1, control, server, "Add server");
        etat = STARTED;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void trun() {
        while (isRunning()) {
            pause(SLEEPTIME);
            switch (etat) {
            case STARTED:
                break;
            default:
                break;
            }
        }
    }
}
