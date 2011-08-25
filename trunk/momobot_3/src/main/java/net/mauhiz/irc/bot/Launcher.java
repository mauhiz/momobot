package net.mauhiz.irc.bot;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import net.mauhiz.irc.base.IIrcClientControl;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcNetwork;
import net.mauhiz.irc.base.data.IrcServerFactory;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.trigger.DefaultTriggerManager;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Launcher {

    private static final Logger LOG = Logger.getLogger(Launcher.class);

    private static void loadProfiles(Launcher launcher, String[] args) {
        for (String arg : args) {
            launcher.loadProfile(arg);
        }
    }

    /**
     * @param args
     * @throws ConfigurationException
     */
    public static void main(String... args) throws ConfigurationException {
        if (ArrayUtils.isEmpty(args)) {
            throw new IllegalArgumentException("Please specify profile names to be loaded");
        }
        LOG.info("Default charset : " + Charset.defaultCharset());
        LOG.info("Default locale : " + Locale.getDefault());
        HierarchicalConfiguration config = new XMLConfiguration("momobot.xml");
        loadProfiles(new Launcher(config), args);
    }

    /**
     * configuration
     */
    private final Configuration config;

    /**
     * @param config
     */
    public Launcher(HierarchicalConfiguration config) {
        this.config = config;
        config.setExpressionEngine(new XPathExpressionEngine());
    }

    private void autoJoin(String profileCriteria, IIrcControl control, IIrcServerPeer peer) {

        String[] joins = config.getStringArray(profileCriteria + "/autoconnect/join");
        LOG.debug(StringUtils.join(joins, ' '));
        for (String chan : joins) {
            control.sendMsg(new Join(peer, peer.getNetwork().findChannel(chan)));
        }
    }

    private void connectToServer(String serverName, String profileCriteria, String nick, String login, String fullName,
            IIrcClientControl control) {
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
        defineIdentity(profileCriteria, nick, login, fullName, peer);

        LOG.info("autoconnect: " + peer.getNetwork().getAlias());
        control.connect(peer);
        autoJoin(profileCriteria, control, peer);
    }

    private void defineIdentity(String profileCriteria, String nick, String login, String fullName, IIrcServerPeer peer) {
        String nickOverride = config.getString(profileCriteria + "/autoconnect/@nick");
        String loginOverride = config.getString(profileCriteria + "/autoconnect/@login");
        String fullNameOverride = config.getString(profileCriteria + "/autoconnect/@fullName");
        peer.introduceMyself(nickOverride == null ? nick : nickOverride, loginOverride == null ? login : loginOverride,
                fullNameOverride == null ? fullName : fullNameOverride);
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
        loadServerProfiles(serverNames, profileCriteria, nick, login, fullName, control, mtm);
    }

    private void loadServerProfiles(String[] serverNames, String profileCriteria, String nick, String login,
            String fullName, IIrcClientControl control, DefaultTriggerManager mtm) {
        for (String serverName : serverNames) {
            connectToServer(serverName, profileCriteria, nick, login, fullName, control);
            String[] packageBundles = config.getStringArray(profileCriteria + "/loadtriggerpack/@bundle");

            for (String bundle : packageBundles) {
                String prefix = config
                        .getString(profileCriteria + "/loadtriggerpack[@bundle='" + bundle + "']/@prefix");
                String packCriteria = "triggerpack[@bundle='" + bundle + "']";
                String pack = config.getString(packCriteria + "/@package") + '.';
                String[] trigClassNames = config.getStringArray(packCriteria + "/trigger/@class");
                loadTriggers(trigClassNames, packCriteria, pack, mtm, prefix);
            }
        }
    }

    private void loadTriggers(String[] trigClassNames, String packCriteria, String pack, DefaultTriggerManager mtm,
            String prefix) {
        for (String trigClassName : trigClassNames) {
            String[] trigTexts = config.getStringArray(packCriteria + "/trigger[@class='" + trigClassName
                    + "']/command");
            String trigClassFull = pack + trigClassName;
            mtm.loadTrigClass(trigClassFull, prefix, Arrays.asList(trigTexts));
        }
    }

}
