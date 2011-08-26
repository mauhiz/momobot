package net.mauhiz.irc.bot;

import java.util.Arrays;

import net.mauhiz.irc.CommonLauncher;
import net.mauhiz.irc.base.IIrcClientControl;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.trigger.DefaultTriggerManager;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Launcher extends CommonLauncher {

    private static final Logger LOG = Logger.getLogger(Launcher.class);

    /**
     * @param args
     * @throws ConfigurationException
     */
    public static void main(String... args) throws ConfigurationException {
        showDefaults();
        new Launcher(new XMLConfiguration("momobot.xml")).loadProfiles(args);
    }

    /**
     * @param config
     */
    protected Launcher(HierarchicalConfiguration config) {
        super(config);
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
        IIrcServerPeer peer = loadServerClass(serverClass, serverName, uri);
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
    @Override
    protected void loadProfile(String profile) {
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

    @Override
    protected void loadProfiles(String... args) {
        if (ArrayUtils.isEmpty(args)) {
            throw new IllegalArgumentException("Please specify profile names to be loaded");
        }
        super.loadProfiles(args);
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
