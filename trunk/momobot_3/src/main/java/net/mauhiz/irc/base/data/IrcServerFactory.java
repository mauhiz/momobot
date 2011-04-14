package net.mauhiz.irc.base.data;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.data.defaut.DefaultServer;
import net.mauhiz.irc.base.data.qnet.QnetServer;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;

public class IrcServerFactory {
    private static final Map<String, Class<? extends IrcNetwork>> NETWORK_TYPES = new HashMap<String, Class<? extends IrcNetwork>>();

    static {
        // built-in
        registerServerClass("Quakenet", QnetServer.class);
    }

    public static IIrcServerPeer createServer(String uriStr) {
        URI uri = URI.create(uriStr);
        if (StringUtils.endsWith(uri.getHost(), "quakenet.org")) {
            QnetServer server = new QnetServer("Quakenet");
            return server.newServerPeer(uri);
        }
        DefaultServer ds = new DefaultServer(uri.getHost());
        return ds.newServerPeer(uri);
    }

    public static IIrcServerPeer createServer(String alias, String uriStr) {
        URI uri = URI.create(uriStr);
        Class<? extends IrcNetwork> registeredClass = NETWORK_TYPES.get(alias);
        IrcNetwork server;
        if (registeredClass == null) {
            server = new DefaultServer(alias);

        } else {
            try {
                server = registeredClass.getConstructor(String.class).newInstance(alias);
                server.setDefaultUri(uri);
            } catch (InvocationTargetException ite) {
                throw new IllegalArgumentException(ite);
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(e);
            } catch (IllegalAccessException e) {
                throw new NotImplementedException(e);
            } catch (NoSuchMethodException e) {
                throw new NotImplementedException(e);
            }
        }
        return server.newServerPeer(uri);
    }

    public static void registerServerClass(String alias, Class<? extends IrcNetwork> serverClass) {
        if (!NETWORK_TYPES.containsKey(alias)) {
            NETWORK_TYPES.put(alias, serverClass);
        }
    }
}
