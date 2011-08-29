package net.mauhiz.irc;

import java.nio.charset.Charset;
import java.util.Locale;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcNetwork;
import net.mauhiz.irc.base.data.IrcServerFactory;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.log4j.Logger;

public abstract class CommonLauncher {
    private static final Logger LOG = Logger.getLogger(CommonLauncher.class);

    protected static void showDefaults() {
        LOG.info("Default charset: " + Charset.defaultCharset());
        LOG.info("Default locale: " + Locale.getDefault());
    }

    /**
     * configuration
     */
    protected final Configuration config;

    /**
     * @param config
     */
    protected CommonLauncher(HierarchicalConfiguration config) {
        this.config = config;
        config.setExpressionEngine(new XPathExpressionEngine());
    }

    protected abstract void loadProfile(String arg);

    protected void loadProfiles(String... args) {
        for (String arg : args) {
            loadProfile(arg);
        }
    }

    protected IIrcServerPeer loadServerClass(String serverClass, String serverName, String uri) {
        if (serverClass != null) {
            try {
                Class<? extends IrcNetwork> clazz = Class.forName(serverClass).asSubclass(IrcNetwork.class);
                IrcServerFactory.registerServerClass(serverName, clazz);
            } catch (ClassNotFoundException cnfe) {
                LOG.error(cnfe, cnfe);
            }
        }
        return IrcServerFactory.createServer(serverName, uri);
    }
}
