package momobot.event.personal;

import ircbot.APersonalEvent;
import ircbot.IrcUser;

/**
 * TODO : le faire.
 * @author Administrator
 */
public class AddservAutomate extends APersonalEvent {
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
    @Override
    public final void run() {
        setRunning(true);
        while (isRunning()) {
            sleep(SLEEPTIME);
            switch (getEtat()) {
                case ETAT1:
                    break;
                default:
                    break;
            }
        }
    }
}
