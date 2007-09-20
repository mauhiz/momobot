package momobot.automate;

import ircbot.AbstractPersonalEvent;
import ircbot.IrcUser;

/**
 * TODO : le faire.
 * 
 * @author mauhiz
 */
public class AddservAutomate extends AbstractPersonalEvent {
    /**
     * @param user1
     *            le user
     */
    public AddservAutomate(final IrcUser user1) {
        super(user1);
        setEtat(ETAT.ETAT1);
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public final void run() {
        setRunning(true);
        while (isRunning()) {
            pause(SLEEPTIME);
            switch (getEtat()) {
                case ETAT1 :
                    break;
                default :
                    break;
            }
        }
    }
}
