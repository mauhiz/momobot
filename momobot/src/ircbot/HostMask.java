package ircbot;

import org.apache.commons.lang.StringUtils;

/**
 * @author viper
 */
public class HostMask implements IIrcSpecialChars {
    /**
     * .
     */
    private static final char AROBAZ = '@';
    /**
     * .
     */
    private static final char EXCL   = '!';

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
     * @return un nouveau user
     * @throws Exception ?
     */
    public final IrcUser createUser() throws Exception {
        final int exclamation = this.hostmask.indexOf(EXCL);
        final int at = this.hostmask.indexOf(AROBAZ);
        if (exclamation > 0 && at > 0 && exclamation < at) {
            String sourceNick = this.hostmask.substring(1, exclamation);
            String sourceLogin = this.hostmask.substring(exclamation + 1, at);
            String sourceHostname = this.hostmask.substring(at + 1);
            return IrcUser.getUser(sourceNick, sourceLogin, sourceHostname);
        }
        throw new Exception("Invalid HostMask");
    }

    /**
     * @param user
     *            le user
     * @return si le user est matché par le mask
     */
    public final boolean match(final IrcUser user) {
        return localMatch(user.getLogin(), StringUtils.substringBefore(
                this.hostmask, EMPTY + AROBAZ))
                && localMatch(user.getLogin(), StringUtils.substringBetween(
                        this.hostmask, EMPTY + AROBAZ, EMPTY + EXCL))
                && localMatch(user.getLogin(), StringUtils.substringAfter(
                        this.hostmask, EMPTY + EXCL));
    }
}
