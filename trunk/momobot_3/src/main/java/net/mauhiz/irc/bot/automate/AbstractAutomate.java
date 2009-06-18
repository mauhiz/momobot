package net.mauhiz.irc.bot.automate;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.util.AbstractRunnable;
import net.mauhiz.util.Hooks;

/**
 * Cette classe est horriblement moche. TODO virer cette enum qui sert a rien.
 * 
 * @author mauhiz
 */
public abstract class AbstractAutomate extends AbstractRunnable {
    
    /**
     * @author mauhiz
     * 
     */
    static class AutomateHook {
        private final IrcUser hook;
        
        /**
         * @param hookable
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
    private final IrcServer server;
    
    /**
     * @param user1
     *            l'user
     * @param control1
     * @param server1
     */
    public AbstractAutomate(IrcUser user1, IIrcControl control1, IrcServer server1) {
        super();
        control = control1;
        server = server1;
        myHook = new AutomateHook(user1);
    }
    
    /**
     * @return {@link #server}
     */
    public IrcServer getServer() {
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
        Privmsg msg = new Privmsg(null, getUser().getNick(), server, text);
        control.sendMsg(msg);
    }
}
