package net.mauhiz.irc.bot.automate;

import net.mauhiz.irc.AbstractRunnable;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.AbstractHook;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Privmsg;

/**
 * Cette classe est horriblement moche. TODO virer cette énum qui sert à rien.
 * 
 * @author mauhiz
 */
public abstract class Automate extends AbstractRunnable {
    
    /**
     * @author mauhiz
     * 
     */
    static class AutomateHook extends AbstractHook<IrcUser> {
        
        /**
         * @param hookable
         */
        public AutomateHook(final IrcUser hookable) {
            super(hookable);
        }
        
        /**
         * @return #hook
         */
        IrcUser getUser() {
            return hook;
        }
        
    }
    /**
     * éteint.
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
    protected final IIrcControl control;
    /**
     * l'état, off par défaut.
     */
    private int etat = OFF;
    /**
     * L'user associé.
     */
    private final AutomateHook myHook;
    private final IrcServer server;
    
    /**
     * @param user1
     *            l'user
     * @param control1
     * @param server1
     */
    public Automate(final IrcUser user1, final IIrcControl control1, final IrcServer server1) {
        super();
        control = control1;
        server = server1;
        myHook = new AutomateHook(user1);
    }
    
    /**
     * @return {@link #etat}
     */
    public final int getEtat() {
        return etat;
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
    protected final IrcUser getUser() {
        return myHook.getUser();
    }
    
    /**
     * @param text
     */
    protected void sendMsgToUser(final String text) {
        Privmsg msg = new Privmsg(null, getUser().getNick(), server, text);
        control.sendMsg(msg);
    }
    
    /**
     * @param etat1
     *            l'état
     */
    public final void setEtat(final int etat1) {
        etat = etat1;
    }
}
