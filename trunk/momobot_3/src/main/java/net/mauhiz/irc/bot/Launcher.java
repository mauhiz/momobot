package net.mauhiz.irc.bot;

import java.nio.charset.Charset;

import net.mauhiz.irc.base.IIrcClientControl;
import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcNetwork;
import net.mauhiz.irc.base.data.IrcServerFactory;
import net.mauhiz.irc.base.msg.Join;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Launcher {

    /**
     * logger
     */
    private static final Logger LOG = Logger.getLogger(Launcher.class);

    /**
     * @param args
     * @throws ConfigurationException
     */
    public static void main(String... args) throws ConfigurationException {
        if (ArrayUtils.isEmpty(args)) {
            throw new IllegalArgumentException("Please specify profile names to be loaded");
        }
        LOG.info("Default charset : " + Charset.defaultCharset());
        HierarchicalConfiguration config = new XMLConfiguration("momobot.xml");
        Launcher launcher = new Launcher(config);
        for (String arg : args) {
            launcher.loadProfile(arg);
        }
    }

    /**
     * configuration
     */
    private final Configuration config;

    /**
     * @param config1
     */
    public Launcher(HierarchicalConfiguration config1) {
        config = config1;
        config1.setExpressionEngine(new XPathExpressionEngine());
    }

    /**
     * @param profile
     */
    private void loadProfile(String profile) {
        LOG.info("loading profile: " + profile);
        String profileCriteria = "profil[@name='" + profile + "']";
        String nick = config.getString(profileCriteria + "/@nick");
        LOG.debug("nick=" + nick);
        String login = config.getString(profileCriteria + "/@login");
        LOG.debug("login=" + login);
        String fullName = config.getString(profileCriteria + "/@fullName");
        LOG.debug("fullName=" + fullName);

        MmbTriggerManager mtm = new MmbTriggerManager();
        IIrcClientControl control = new IrcClientControl(mtm);

        String[] serverNames = config.getStringArray(profileCriteria + "/autoconnect/@server");
        for (String serverName : serverNames) {
            String uri = config.getString("server[@alias='" + serverName + "']/@uri");
            LOG.debug("uri=" + uri);
            String serverClass = config.getString("server[@alias='" + serverName + "']/@class");
            if (serverClass != null) {
                try {
                    Class<? extends IrcNetwork> clazz = Class.forName(serverClass).asSubclass(IrcNetwork.class);
                    IrcServerFactory.registerServerClass(serverName, clazz);
                } catch (ClassNotFoundException cnfe) {
                    LOG.error(cnfe, cnfe);
                }
            }
            IIrcServerPeer peer = IrcServerFactory.createServer(serverName, uri);
            IrcNetwork server = peer.getNetwork();

            String nickOverride = config.getString(profileCriteria + "/autoconnect/@nick");
            String loginOverride = config.getString(profileCriteria + "/autoconnect/@login");
            String fullNameOverride = config.getString(profileCriteria + "/autoconnect/@fullName");
            peer.introduceMyself(nickOverride == null ? nick : nickOverride, loginOverride == null ? login
                    : loginOverride, fullNameOverride == null ? fullName : fullNameOverride);
            LOG.info("autoconnect: " + server.getAlias());
            control.connect(peer);
            String[] joins = config.getStringArray(profileCriteria + "/autoconnect/join");
            LOG.debug(StringUtils.join(joins, ' '));
            for (String chan : joins) {
                control.sendMsg(new Join(peer, server.findChannel(chan)));
            }

            String[] packageBundles = config.getStringArray(profileCriteria + "/loadtriggerpack/@bundle");
            for (String bundle : packageBundles) {
                String prefix = config
                        .getString(profileCriteria + "/loadtriggerpack[@bundle='" + bundle + "']/@prefix");
                String packCriteria = "triggerpack[@bundle='" + bundle + "']";
                String pack = config.getString(packCriteria + "/@package") + '.';
                String[] trigClassNames = config.getStringArray(packCriteria + "/trigger/@class");
                for (String trigClassName : trigClassNames) {
                    String[] trigTexts = config.getStringArray(packCriteria + "/trigger[@class='" + trigClassName
                            + "']/command");
                    String trigClassFull = pack + trigClassName;
                    mtm.loadTrigClass(trigClassFull, prefix, trigTexts);
                }
            }
        }
    }

}
