package net.mauhiz.irc.base.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public final class Channels extends ConcurrentSkipListMap<String, Channel> implements IrcSpecialChars {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(Channels.class);
    /**
     * serial
     */
    private static final long serialVersionUID = 1L;
    /**
     * map ircServer > channels
     */
    static Map<IrcServer, Channels> serverMap = new HashMap<IrcServer, Channels>();
    /**
     * @param server
     * @return users
     */
    public static Channels get(final IrcServer server) {
        Channels servChannels = serverMap.get(server);
        if (servChannels == null) {
            servChannels = new Channels();
            serverMap.put(server, servChannels);
        }
        return servChannels;
    }
    
    /**
     * @param toTest
     *            le nom à tester
     * @return si le nom est un channel ou nom
     */
    public static boolean isChannelName(final String toTest) {
        if (StringUtils.isEmpty(toTest) || StringUtils.indexOfAny(toTest, Z_NOTCHSTRING) > 0) {
            return false;
        }
        return toTest.charAt(0) == CHAN_DEFAULT || toTest.charAt(0) == CHAN_LOCAL;
    }
    
    /**
     * private ctor.
     */
    private Channels() {
        super();
    }
    
    /**
     * @param channel
     *            le nom du channel
     * @return le channel
     */
    public Channel getChannel(final String channel) {
        // LOG.debug("getChannel : " + channel);
        final String chanLowerCase = channel.toLowerCase(Locale.FRANCE);
        Channel found = get(chanLowerCase);
        if (found != null) {
            LOG.debug("channel '" + chanLowerCase + "' found.");
        } else if (isChannelName(chanLowerCase)) {
            LOG.debug("channel '" + chanLowerCase + "' not found, adding");
            found = new Channel(chanLowerCase);
            put(chanLowerCase, found);
        } else {
            LOG.debug("'" + chanLowerCase + "' : not a channel name");
        }
        return found;
    }
    
    /**
     * @param smith
     * @return channels for user
     */
    public Set<Channel> getChannels(final IrcUser smith) {
        Set<Channel> chans = new HashSet<Channel>();
        for (Channel chan : values()) {
            if (chan.contains(smith)) {
                chans.add(chan);
            }
        }
        return chans;
    }
    
    /**
     * @param channel
     *            le channel
     */
    public void removeChannel(final String channel) {
        remove(channel.toLowerCase(Locale.FRANCE));
    }
}
