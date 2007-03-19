package ircbot;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Les utilisateurs sont rangés dans une Map (nickLowerCase, object) permettant
 * un accès rapide lorsqu'un évènement associé à un utilisateur apparaît.
 * @author Administrator
 */
public class IrcUser {
    /**
     * tous les users que je connais (string = nick).
     */
    private static final ConcurrentMap < String, IrcUser > USERS;
    static {
        USERS = new ConcurrentSkipListMap < String, IrcUser >();
    }

    /**
     * @return le nombre de users connus
     */
    public static int countUsers() {
        return USERS.size();
    }

    /**
     * renvoie null si la map ne contient pas nick.
     * @param nick1
     *            le nick
     * @return l'User
     */
    public static IrcUser getUser(final String nick1) {
        final String nickLowerCase = nick1.toLowerCase();
        System.out.println("User " + nick1 + " requested");
        return USERS.get(nickLowerCase);
    }

    /**
     * factory d'users Renvoie l'user défini par ces 3 paramètres, et le crée
     * s'il le faut.
     * @param sourceNick
     *            le nick
     * @param sourceLogin
     *            le login
     * @param sourceHostname
     *            le hostname
     * @return un user
     */
    public static IrcUser getUser(final String sourceNick,
            final String sourceLogin, final String sourceHostname) {
        IrcUser iu = getUser(sourceNick);
        if (iu != null) {
            return iu;
        }
        iu = new IrcUser(sourceNick, sourceLogin, sourceHostname);
        System.out.println("User " + sourceNick + " created");
        USERS.put(iu.getNick().toLowerCase(), iu);
        return iu;
    }

    /**
     * @param nick1
     *            le nick
     * @return le momouser qui a quit
     */
    public static IrcUser removeUser(final String nick1) {
        return USERS.remove(nick1);
    }

    /**
     * @param user
     *            le user
     * @param newnick
     *            le nouveau nick
     * @return le IrcUser en question
     */
    protected static IrcUser updateUser(final IrcUser user, final String newnick) {
        final String nickLowerCase = newnick.toLowerCase();
        USERS.remove(user.getNick().toLowerCase(), user);
        user.setNick(newnick);
        return USERS.put(nickLowerCase, user);
    }

    /**
     * @param nick1
     *            le nick
     * @return si l'utilisateur existe
     */
    public static boolean userExists(final String nick1) {
        return USERS.containsKey(nick1.toLowerCase());
    }
    /**
     * Si je suis admin.
     */
    private boolean        admin     = false;
    /**
     * L'automate.
     */
    private APersonalEvent auto      = null;
    /**
     * Le hostname.
     */
    private String         hostname  = "";
    /**
     * la dernière fois que je l'ai whois.
     */
    private long           lastWhois = 0;
    /**
     * Le login.
     */
    private String         login     = "";
    /**
     * Le nick.
     */
    private String         nick;

    /**
     * @param nick1
     *            le nick
     */
    public IrcUser(final String nick1) {
        this.nick = nick1;
    }

    /**
     * @param nick1
     *            le nick
     * @param login1
     *            le login
     * @param hostname1
     *            le hostname
     */
    public IrcUser(final String nick1, final String login1,
            final String hostname1) {
        this(nick1);
        setLogin(login1);
        setHostname(hostname1);
    }

    /**
     * @return Returns the hostname.
     */
    public final String getHostname() {
        return this.hostname;
    }

    /**
     * @return the lastWhois
     */
    public final long getLastWhois() {
        return this.lastWhois;
    }

    /**
     * @return Returns the login.
     */
    public final String getLogin() {
        return this.login;
    }

    /**
     * @return le nick
     */
    public final String getNick() {
        return this.nick;
    }

    /**
     * @return si un automate est en cours avec l'user
     */
    public final boolean hasAutomateRunning() {
        return this.auto != null;
    }

    /**
     * @return si l'user est admin
     */
    public final boolean isAdmin() {
        return this.admin;
    }

    /**
     * @param automate
     *            l'automate
     */
    public final void registerAutomate(final APersonalEvent automate) {
        this.auto = automate;
    }

    /**
     * @param b
     *            le booleen
     */
    public final void setAdmin(final boolean b) {
        this.admin = b;
    }

    /**
     * @param hostname1
     *            The hostname to set.
     */
    protected void setHostname(final String hostname1) {
        this.hostname = hostname1;
    }

    /**
     * @param lastWhois1
     *            the lastWhois to set
     */
    public final void setLastWhois(final long lastWhois1) {
        this.lastWhois = lastWhois1;
    }

    /**
     * @param login1
     *            The login to set.
     */
    protected final void setLogin(final String login1) {
        this.login = login1;
    }

    /**
     * @param newnick
     *            le nouveau nick
     */
    protected final void setNick(final String newnick) {
        this.nick = newnick;
    }

    /**
     * @see java.lang.Object#toString()
     * @return un string
     */
    @Override
    public final String toString() {
        return getNick();
    }

    /**
     * Efface l'automate.
     */
    public final void unregisterAutomate() {
        this.auto = null;
    }
}
