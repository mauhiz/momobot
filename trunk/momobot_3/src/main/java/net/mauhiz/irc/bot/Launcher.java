package net.mauhiz.irc.bot;

import java.net.URISyntaxException;

import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.bot.triggers.ITrigger;

import org.apache.commons.configuration.Configuration;
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
     * @param trigClassFull
     * @param prefix
     * @param mtm
     * @param trigTexts
     * @return success
     */
    public static boolean loadTrigClass(final String trigClassFull, final String prefix, final MmbTriggerManager mtm,
            final String[] trigTexts) {
        Class<ITrigger> trigClass;
        try {
            Class<?> wannabe = Class.forName(trigClassFull);
            if (!ITrigger.class.isAssignableFrom(wannabe)) {
                LOG.warn("Not a trigger: " + wannabe.getName());
                return false;
            }
            trigClass = (Class<ITrigger>) wannabe;
        } catch (ClassNotFoundException e) {
            LOG.warn(e);
            return false;
        }
        
        if (ArrayUtils.isEmpty(trigTexts)) {
            mtm.addTrigger(trigClass, (Object[]) null);
            LOG.debug("loading trigger: " + trigClass.getSimpleName());
        } else {
            for (String trigText : trigTexts) {
                String fullTrigText = prefix + trigText;
                mtm.addTrigger(trigClass, fullTrigText);
                LOG.debug("loading trigger with command '" + fullTrigText + "': " + trigClass.getSimpleName());
            }
        }
        return true;
    }
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
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
    public Launcher(final HierarchicalConfiguration config1) {
        config = config1;
        config1.setExpressionEngine(new XPathExpressionEngine());
    }
    /**
     * @param profile
     */
    private void loadProfile(final String profile) {
        LOG.info("loading profile: " + profile);
        String profileCriteria = "profil[@name='" + profile + "']";
        String nick = config.getString(profileCriteria + "/@nick");
        LOG.debug("nick=" + nick);
        String login = config.getString(profileCriteria + "/@login");
        LOG.debug("login=" + login);
        String fullName = config.getString(profileCriteria + "/@fullName");
        LOG.debug("fullName=" + fullName);
        
        MmbTriggerManager mtm = new MmbTriggerManager();
        IrcControl control = new IrcControl(mtm);
        
        String[] serverNames = config.getStringArray(profileCriteria + "/autoconnect/@server");
        for (String serverName : serverNames) {
            String uri = config.getString("server[@alias='" + serverName + "']/@uri");
            LOG.debug("uri=" + uri);
            IrcServer server;
            try {
                server = new IrcServer(uri);
            } catch (URISyntaxException e) {
                LOG.error(e);
                continue;
            }
            
            server.setAlias(serverName);
            String nickOverride = config.getString(profileCriteria + "/autoconnect/@nick");
            if (nickOverride == null) {
                nickOverride = nick;
            }
            String loginOverride = config.getString(profileCriteria + "/autoconnect/@login");
            if (loginOverride == null) {
                loginOverride = login;
            }
            String fullNameOverride = config.getString(profileCriteria + "/autoconnect/@fullName");
            if (fullNameOverride == null) {
                fullNameOverride = fullName;
            }
            
            server.setMyNick(nickOverride);
            server.setMyLogin(loginOverride);
            server.setMyFullName(fullNameOverride);
            LOG.info("autoconnect: " + server);
            control.connect(server);
            String[] joins = config.getStringArray(profileCriteria + "/autoconnect/join");
            LOG.debug(StringUtils.join(joins, ' '));
            for (String chan : joins) {
                control.sendMsg(new Join(server, chan));
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
                    loadTrigClass(trigClassFull, prefix, mtm, trigTexts);
                }
            }
        }
    }
    
}
