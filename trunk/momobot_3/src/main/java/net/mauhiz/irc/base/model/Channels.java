package net.mauhiz.irc.base.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Channels implements IrcSpecialChars {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(Channel.class);
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
    public static final boolean isChannelName(final String toTest) {
        
        if (StringUtils.isEmpty(toTest) || StringUtils.indexOfAny(toTest, Z_NOTCHSTRING) > 0) {
            return false;
        }
        return toTest.charAt(0) == CHAN_DEFAULT || toTest.charAt(0) == CHAN_LOCAL;
    }
    /**
     * tous les channels ou je suis (string = nom).
     */
    private final ConcurrentMap<String, Channel> channels = new ConcurrentSkipListMap<String, Channel>();
    
    /**
     * private ctor.
     */
    private Channels() {
        super();
    }
    
    /**
     * @return un itérateur sur tous les channels
     */
    public final Collection<Channel> getAll() {
        return channels.values();
    }
    
    /**
     * @param channel
     *            le nom du channel
     * @return le channel
     */
    public final Channel getChannel(final String channel) {
        // LOG.debug("getChannel : " + channel);
        final String chanLowerCase = channel.toLowerCase(Locale.FRANCE);
        if (channels.containsKey(chanLowerCase)) {
            LOG.debug("channel '" + chanLowerCase + "' found.");
            return channels.get(chanLowerCase);
        }
        LOG.debug("channel '" + chanLowerCase + "' not found, adding");
        Channel newChan = new Channel(channel);
        channels.put(chanLowerCase, newChan);
        return newChan;
    }
    
    /**
     * retire tous les channels.
     */
    public final void removeAll() {
        channels.clear();
    }
    
    /**
     * @param channel
     *            le channel
     */
    public final void removeChannel(final String channel) {
        channels.remove(channel.toLowerCase(Locale.FRANCE));
    }
}
