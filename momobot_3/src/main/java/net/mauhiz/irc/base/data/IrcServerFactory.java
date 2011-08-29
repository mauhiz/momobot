package net.mauhiz.irc.base.data;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.mauhiz.irc.base.data.defaut.DefaultServer;

import org.apache.commons.lang.NotImplementedException;

public enum IrcServerFactory {
    ;
    private static final Map<String, Class<? extends IrcNetwork>> NETWORK_TYPES = new HashMap<>();

    public static IrcNetwork createNetwork(String alias, URI uri) {
        String name = Objects.toString(alias, guessAlias(uri));
        Class<? extends IrcNetwork> registeredClass = NETWORK_TYPES.get(name);

        if (registeredClass == null) {
            return new DefaultServer(alias);
        }

        try {
            return registeredClass.getConstructor(String.class).newInstance(name);

        } catch (InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new NotImplementedException(e);
        }
    }

    public static IIrcServerPeer createServer(String uriStr) {
        return createServer(null, uriStr);
    }

    public static IIrcServerPeer createServer(String alias, String uriStr) {
        URI uri = URI.create(uriStr);
        IrcNetwork server = createNetwork(alias, uri);
        server.setDefaultUri(uri);
        return server.newServerPeer(uri);
    }

    public static String guessAlias(URI uri) {
        return uri.getHost();
    }

    public static void registerServerClass(String alias, Class<? extends IrcNetwork> serverClass) {
        if (!NETWORK_TYPES.containsKey(alias)) {
            NETWORK_TYPES.put(alias, serverClass);
        }
    }
}
