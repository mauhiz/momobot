package net.mauhiz.irc.base.data;

import java.net.URI;

import net.mauhiz.irc.base.data.defaut.DefaultServer;
import net.mauhiz.irc.base.data.qnet.QnetServer;

import org.apache.commons.lang.StringUtils;

public class IrcServerFactory {
    public static IrcServer createServer(String uriStr) {
        URI uri = URI.create(uriStr);
        if (StringUtils.endsWith(uri.getHost(), "quakenet.org")) {
            IrcServer server = new QnetServer(uri);
            server.setAlias("Quakenet");
            return server;
        }
        return new DefaultServer(uri);
    }
}
