package ircbot;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class Channel implements IIrcSpecialChars, Comparable < Channel > {
    /**
     * tous les channels ou je suis (string = nom).
     */
    private static final ConcurrentMap < String, Channel > CHANNELS         = new ConcurrentSkipListMap < String, Channel >();
    /**
     * 
     */
    private static final long                              serialVersionUID = 1;

    /**
     * @return un itérateur sur tous les channels
     */
    public static final Iterable < Channel > getAll() {
        return CHANNELS.values();
    }

    /**
     * @param channel
     *            le nom du channel
     * @return le channel
     */
    public static final Channel getChannel(final String channel) {
        final String chanLowerCase = channel.toLowerCase(Locale.FRANCE);
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
        return toTest.charAt(0) == CHAN_DEFAULT || toTest.charAt(0) == CHAN_LOCAL;
    }

    /**
     * retire tous les channels.
     */
    public static final void removeAll() {
        CHANNELS.clear();
    }

    /**
     * @param channel
     *            le channel
     */
    public static final void removeChannel(final String channel) {
        CHANNELS.remove(channel);
    }

    /**
     * liste des évènements enregistrés sur ce chan.
     */
    private IChannelEvent                  event;
    /**
     * Si le channel est en mode +i.
     */
    private boolean                               inviteOnly;
    /**
     * la key du channel.
     */
    private String                                key;
    /**
     * la limite.
     */
    private int                                   limit;
    /**
     * 
     */
    private final ConcurrentMap < IrcUser, Mode > modes = new ConcurrentHashMap < IrcUser, Mode >();
    /**
     * le nom.
     */
    private final String                          nom;
    /**
     * le topic du channel.
     */
    private Topic                                 topic;

    /**
     * @param nom1
     *            le nom
     */
    public Channel(final String nom1) {
        super();
        this.nom = nom1;
    }

    /**
     * @param user
     *            l'user
     */
    public final void addUser(final IrcUser user) {
        addUser(user, null);
    }

    /**
     * @param user
     *            l'user
     * @param mode
     *            le mode
     */
    public final void addUser(final IrcUser user, final String mode) {
        if (!this.modes.containsKey(user)) {
            this.modes.put(user, new Mode(mode));
        }
    }

    /**
     * @param autrChan
     *            le channel à comparer.
     * @return + ou - 1
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final Channel autrChan) {
        if (equals(autrChan)) {
            return 0;
        }
        return this.nom.compareToIgnoreCase(autrChan.getNom());
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof Channel) {
            return this.nom.equalsIgnoreCase(((Channel) other).getNom());
        }
        return false;
    }

    /**
     * @return this.event
     */
    public final IChannelEvent getEvent() {
        return this.event;
    }

    /**
     * @return Returns the key.
     */
    public final String getKey() {
        return this.key;
    }

    /**
     * @return the limit
     */
    public int getLimit() {
        return this.limit;
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
     * @return les users.
     */
    public Iterable < IrcUser > getUsers() {
        return this.modes.keySet();
    }

    /**
     * @param user
     *            le user
     */
    public final void giveOp(final String user) {
        final Mode mode = this.modes.get(user);
        if (mode != null) {
            mode.giveOp();
        }
    }

    /**
     * @param user
     *            le user
     */
    public final void giveVoice(final String user) {
        final Mode mode = this.modes.get(user);
        if (mode != null) {
            mode.giveVoice();
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getNom().hashCode();
    }

    /**
     * @param user
     *            le user
     * @return si l'user est voice
     */
    public final boolean hasOp(final String user) {
        final Mode mode = this.modes.get(user);
        if (mode != null) {
            return mode.isOp();
        }
        return false;
    }

    /**
     * @return si le chan a un evenement en cours
     */
    public final boolean hasRunningEvent() {
        return this.event != null;
    }

    /**
     * @param user
     *            l'user
     * @return si l'user est voice
     */
    public final boolean hasVoice(final String user) {
        final Mode mode = this.modes.get(user);
        if (mode != null) {
            return mode.isVoice();
        }
        return false;
    }

    /**
     * @return Returns the inviteOnly.
     */
    public final boolean isInviteOnly() {
        return this.inviteOnly;
    }

    /**
     * @param ace
     *            l'event
     */
    public final void registerEvent(final IChannelEvent ace) {
        this.event = ace;
    }

    /**
     * @param nick
     */
    public void remove(final String nick) {
        this.modes.remove(IrcUser.getUser(nick));
    }

    /**
     * @param user
     *            l'user
     */
    public final void removeOp(final String user) {
        final Mode mode = this.modes.get(user);
        if (mode != null) {
            mode.removeOp();
        }
    }

    /**
     * @param user
     *            l'user
     */
    public final void removeVoice(final String user) {
        final Mode mode = this.modes.get(user);
        if (mode != null) {
            mode.removeVoice();
        }
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
     * @param limit1
     *            the limit to set
     */
    public void setLimit(final int limit1) {
        this.limit = limit1;
    }

    /**
     * @param myTopic
     *            The myTopic to set.
     */
    public final void setTopic(final Topic myTopic) {
        this.topic = myTopic;
    }

    /**
     * @see java.lang.Object#toString()
     * @return le nom du channel
     */
    @Override
    public final String toString() {
        return this.nom;
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
        this.key = null;
    }
}
