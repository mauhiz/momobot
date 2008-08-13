package net.mauhiz.irc.bot.automate;

import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.AbstractRunnable;
import net.mauhiz.irc.base.IrcControl;
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
     * 
     */
    static final Map<IrcUser, Automate> AUTOMATES = new HashMap<IrcUser, Automate>();
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
    private final IrcControl control;
    /**
     * l'état, off par défaut.
     */
    private int etat = OFF;
    private final IrcServer server;
    /**
     * L'user associé.
     */
    private final IrcUser user;
    
    /**
     * @param user1
     *            l'user
     * @param control1
     * @param server1
     */
    public Automate(final IrcUser user1, final IrcControl control1, final IrcServer server1) {
        super();
        user = user1;
        control = control1;
        server = server1;
        AUTOMATES.put(user1, this);
    }
    
    /**
     * @return {@link #control}
     */
    public IrcControl getControl() {
        return control;
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
    public final IrcUser getUser() {
        return user;
    }
    
    /**
     * @param text
     */
    protected void sendMsgToUser(final String text) {
        Privmsg msg = new Privmsg(null, user.getNick(), server, text);
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
