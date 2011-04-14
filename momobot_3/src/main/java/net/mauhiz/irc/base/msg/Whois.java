package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class Whois extends AbstractIrcMessage {

    private final String[] targets;

    public Whois(Target from, Target to, IIrcServerPeer server, String... targets) {
        super(from, to, server);
        this.targets = targets;
    }

    @Override
    public Whois copy() {
        return new Whois(from, to, server, targets);
    }

    @Override
    public String getIrcForm() {
        return IrcCommands.WHOIS + " " + StringUtils.join(targets, ',');
    }

    @Override
    public String toString() {
        return "* Whoising " + StringUtils.join(targets, ' ');
    }
}
