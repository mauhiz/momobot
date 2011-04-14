package net.mauhiz.irc.base.data.qnet;

import java.util.Arrays;
import java.util.List;

import net.mauhiz.irc.base.data.AbstractIrcNetwork;
import net.mauhiz.irc.base.data.HostMask;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class QnetServer extends AbstractIrcNetwork {

    public QnetServer(String alias) {
        super(alias);
    }

    @Override
    public QnetUser findUser(HostMask mask, boolean addIfNotFound) {
        return (QnetUser) super.findUser(mask, addIfNotFound);
    }

    @Override
    public QnetUser findUser(String nick, boolean addIfNotFound) {
        return (QnetUser) super.findUser(nick, addIfNotFound);
    }

    @Override
    public int getLineMaxLength() {
        return 255; // confirmed
    }

    @Override
    public List<String> getServiceNicks() {
        return Arrays.asList("Q", "L");
    }

    public void handleWhois(String msg) {
        String nick = StringUtils.substringBefore(msg, " ");
        QnetUser ircUser = findUser(nick, true);

        String remaining = StringUtils.substringAfter(msg, " ");
        String auth = StringUtils.substringBefore(remaining, " :");
        ircUser.setAuth(auth);
    }

    /**
     * @see net.mauhiz.irc.base.data.AbstractIrcNetwork#newChannel(java.lang.String)
     */
    @Override
    public QnetChannel newChannel(String chanLowerCase) {
        return new QnetChannel(chanLowerCase);
    }

    /**
     * @see net.mauhiz.irc.base.data.AbstractIrcNetwork#newUser(java.lang.String)
     */
    @Override
    public QnetUser newUser(String nick) {
        return new QnetUser(nick);
    }
}
