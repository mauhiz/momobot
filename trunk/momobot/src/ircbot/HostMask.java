package ircbot;

import org.apache.commons.lang.StringUtils;

/**
 * @author viper
 */
public class HostMask {
    /**
     * .
     */
    private static final char TOKEN1 = '@';
    /**
     * .
     */
    private static final char TOKEN2 = '!';

    /**
     * @param test
     *            le test
     * @param mask
     *            le masque
     * @return si ça match
     */
    private static boolean localMatch(final String test, final String mask) {
        // TODO la méthode
        return false;
    }
    /**
     * .
     */
    private final String hostmask;

    /**
     * @param hostmask1
     *            le masque.
     */
    public HostMask(final String hostmask1) {
        this.hostmask = hostmask1;
    }

    /**
     * @param user
     *            le user
     * @return si le user est matché par le mask
     */
    public final boolean match(final IrcUser user) {
        return localMatch(user.getLogin(), StringUtils.substringBefore(
                this.hostmask, "" + TOKEN1))
                && localMatch(user.getLogin(), StringUtils.substringBetween(
                        this.hostmask, "" + TOKEN1, "" + TOKEN2))
                && localMatch(user.getLogin(), StringUtils.substringAfter(
                        this.hostmask, "" + TOKEN2));
    }
}
