package net.mauhiz.irc.base.trigger;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.util.AbstractDaemon;
import net.mauhiz.util.Hooks;

/**
 * Cette classe est horriblement moche
 * 
 * @author mauhiz
 */
public abstract class AbstractAutomate extends AbstractDaemon {

    /**
     * @author mauhiz
     * 
     */
    static class AutomateHook {
        private final IrcUser hook;

        /**
         * @param user
         */
        public AutomateHook(IrcUser user) {
            Hooks.addHook(user, this);
            hook = user;
        }

        /**
         * @return #hook
         */
        IrcUser getUser() {
            return hook;
        }

    }

    /**
     * eteint.
     */
    protected static final int OFF = 0;
    /**
     * le temps d'attente dans un thread.
     */
    protected static final long SLEEPTIME = 1000;
    /**
     * indique que l automate est demarre.
     */
    protected static final int STARTED = 1;
    protected IIrcControl control;
    /**
     * l'etat, off par defaut.
     */
    protected int etat = OFF;
    /**
     * L'user associe.
     */
    private final AutomateHook myHook;
    private final IIrcServerPeer server;

    /**
     * @param user
     *            l'user
     */
    public AbstractAutomate(IrcUser user, IIrcControl control, IIrcServerPeer server, String name) {
        super("Automate: " + name);
        this.control = control;
        this.server = server;
        myHook = new AutomateHook(user);
    }

    /**
     * @return {@link #server}
     */
    public IIrcServerPeer getServer() {
        return server;
    }

    /**
     * @return le user
     */
    protected IrcUser getUser() {
        return myHook.getUser();
    }

    /**
     * @param text
     */
    protected void sendMsgToUser(String text) {
        Privmsg msg = new Privmsg(server, null, getUser(), text);
        control.sendMsg(msg);
    }
}
