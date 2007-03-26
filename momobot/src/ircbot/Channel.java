package ircbot;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.commons.lang.StringUtils;

/**
 * @author Administrator
 */
public class Channel implements IIrcSpecialChars {
    /**
     * tous les channels ou je suis (string = nom).
     */
    private static final ConcurrentMap < String, Channel > CHANNELS = new ConcurrentSkipListMap < String, Channel >();

    /**
     * @return un itérateur sur tous les channels
     */
    public static Iterable < Channel > getAll() {
        return CHANNELS.values();
    }

    /**
     * @param channel
     *            le nom du channel
     * @return le channel
     */
    public static Channel getChannel(final String channel) {
        final String chanLowerCase = channel.toLowerCase();
        if (CHANNELS.containsKey(chanLowerCase)) {
            return CHANNELS.get(chanLowerCase);
        }
        return CHANNELS.put(channel, new Channel(channel));
    }

    /**
     * @param toTest
     *            le nom à tester
     * @return si le nom est un channel ou nom
     */
    public static final boolean isChannelName(final String toTest) {
        if (StringUtils.indexOfAny(toTest, Z_NOTCHSTRING) > 0) {
            return false;
        }
        return toTest.charAt(0) == CHAN_DEFAULT
                || toTest.charAt(0) == CHAN_LOCAL;
    }

    /**
     * retire tous les channels.
     */
    public static void removeAll() {
        CHANNELS.clear();
    }

    /**
     * @param channel
     *            le channel
     */
    public static void removeChannel(final String channel) {
        CHANNELS.remove(channel);
    }
    /**
     * liste des évènements enregistrés sur ce chan.
     */
    private AChannelEvent                         event      = null;
    /**
     * Si le channel est en mode +i.
     */
    private boolean                               inviteOnly = false;
    /**
     * la key du channel.
     */
    private String                                key        = "";
    /**
     * le nom.
     */
    private final String                          nom;
    /**
     * le topic du channel.
     */
    private Topic                                 topic      = null;
    /**
     * les modes des users sur le channel.
     */
    private final ConcurrentMap < IrcUser, Mode > users      = new ConcurrentHashMap < IrcUser, Mode >();

    /**
     * @param nom1
     *            le nom
     */
    Channel(final String nom1) {
        this.nom = nom1;
    }

    /**
     * @param user
     *            l'user
     */
    public final void addUser(final IrcUser user) {
        addUser(user, "");
    }

    /**
     * @param user
     *            l'user
     * @param mode
     *            le mode
     */
    public final void addUser(final IrcUser user, final String mode) {
        synchronized (this.users) {
            if (this.users.containsKey(user)) {
                return;
            }
            this.users.put(user, new Mode(mode));
        }
    }

    /**
     * @return la liste des users (sans prefixe)
     */
    public final Iterator < IrcUser > getAllUsers() {
        return this.users.keySet().iterator();
    }

    /**
     * @return this.event
     */
    public final AChannelEvent getEvent() {
        synchronized (this.event) {
            return this.event;
        }
    }

    /**
     * @return Returns the key.
     */
    public final String getKey() {
        return this.key;
    }

    /**
     * @return le nom
     */
    public final String getNom() {
        return this.nom;
    }

    /**
     * @return Returns the myTopic.
     */
    public final Topic getTopic() {
        return this.topic;
    }

    /**
     * @param user
     *            le user
     */
    public final void giveOp(final String user) {
        if (!this.users.containsKey(user)) {
            return;
        }
        this.users.get(user).giveOp();
    }

    /**
     * @param user
     *            le user
     */
    public final void giveVoice(final String user) {
        if (!this.users.containsKey(user)) {
            return;
        }
        this.users.get(user).giveVoice();
    }

    /**
     * @param user
     *            le user
     * @return si l'user est voice
     */
    public final boolean hasOp(final String user) {
        if (!this.users.containsKey(user)) {
            return false;
        }
        return this.users.get(user).isOp();
    }

    /**
     * @return si le chan a un evenement en cours
     */
    public final synchronized boolean hasRunningEvent() {
        return this.event != null;
    }

    /**
     * @param user
     *            l'user
     * @return si l'user est voice
     */
    public final boolean hasVoice(final String user) {
        if (!this.users.containsKey(user)) {
            return false;
        }
        return this.users.get(user).isVoice();
    }

    /**
     * @return Returns the inviteOnly.
     */
    public final boolean isInviteOnly() {
        return this.inviteOnly;
    }

    /**
     * @param e
     *            l'event
     */
    public final void registerEvent(final AChannelEvent e) {
        this.event = e;
    }

    /**
     * @param user
     *            l'user
     */
    public final void removeOp(final String user) {
        if (!this.users.containsKey(user)) {
            return;
        }
        this.users.get(user).removeOp();
    }

    /**
     * @param user
     *            l'user
     */
    public final void removeUser(final String user) {
        this.users.remove(user);
    }

    /**
     * @param user
     *            l'user
     */
    public final void removeVoice(final String user) {
        if (!this.users.containsKey(user)) {
            return;
        }
        this.users.get(user).removeVoice();
    }

    /**
     * @param inviteOnly1
     *            The inviteOnly to set.
     */
    public final void setInviteOnly(final boolean inviteOnly1) {
        this.inviteOnly = inviteOnly1;
    }

    /**
     * @param key1
     *            The key to set.
     */
    public final void setKey(final String key1) {
        this.key = key1;
    }

    /**
     * @param myTopic
     *            The myTopic to set.
     */
    public final void setTopic(final Topic myTopic) {
        this.topic = myTopic;
    }

    /**
     * détruit un event.
     */
    public final void unRegisterEvent() {
        this.event = null;
    }

    /**
     * retire la key.
     */
    public final void unSetKey() {
        this.key = "";
    }
}
