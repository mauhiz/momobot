package net.mauhiz.irc.base.data.qnet;

import java.util.Arrays;
import java.util.Collection;

import net.mauhiz.irc.base.data.ArgumentList;
import net.mauhiz.irc.base.data.HostMask;
import net.mauhiz.irc.base.data.defaut.DefaultServer;
import net.mauhiz.irc.base.msg.NumericReplies;
import net.mauhiz.irc.base.msg.ServerMsg;

/**
 * @author mauhiz
 */
public class QnetServer extends DefaultServer {

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
    public Collection<String> getServiceNicks() {
        return Arrays.asList("Q", "L");
    }

    @Override
    public void handleSpecific(ServerMsg message, NumericReplies reply) {
        switch (reply) {
            case RPL_WHOISAUTH:
                handleWhois(message.getArgs());
                break;
            default:
                super.handleSpecific(message, reply);
        }
    }

    public void handleWhois(ArgumentList args) {
        String nick = args.poll();
        QnetUser ircUser = findUser(nick, true);
        ircUser.setAuth(args.poll());
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
