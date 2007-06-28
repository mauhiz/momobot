package ircbot;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class HostMask implements IIrcSpecialChars {
    /**
     * arobase.
     */
    private static final char AROBAZ = '@';
    /**
     * !.
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
        /* TODO la méthode */
        return false;
    }

    /**
     * hostmask.
     */
    private final String mask;

    /**
     * @param hostmask1
     *            le masque.
     */
    public HostMask(final String hostmask1) {
        this.mask = hostmask1;
    }

    /**
     * @return un nouveau user
     */
    public final IrcUser createUser() {
        final int exclamation = this.mask.indexOf(EXCL);
        final int at = this.mask.indexOf(AROBAZ);
        if (exclamation > 0 && at > 0 && exclamation < at) {
            final String sourceNick = this.mask.substring(1, exclamation);
            final String sourceLogin = this.mask.substring(exclamation + 1, at);
            final String sourceHostname = this.mask.substring(at + 1);
            return IrcUser.getUser(sourceNick, sourceLogin, sourceHostname);
        }
        throw new IllegalArgumentException("Invalid HostMask");
    }

    /**
     * @param user
     *            le user
     * @return si le user est matché par le mask
     */
    public final boolean match(final IrcUser user) {
        return localMatch(user.getLogin(), StringUtils.substringBefore(this.mask, EMPTY + AROBAZ))
                && localMatch(user.getLogin(), StringUtils
                        .substringBetween(this.mask, EMPTY + AROBAZ, EMPTY + EXCL))
                && localMatch(user.getLogin(), StringUtils.substringAfter(this.mask, EMPTY + EXCL));
    }
}
