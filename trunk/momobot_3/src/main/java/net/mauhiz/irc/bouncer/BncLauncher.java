package net.mauhiz.irc.bouncer;

import net.mauhiz.irc.CommonLauncher;
import net.mauhiz.irc.base.data.IIrcServerPeer;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class BncLauncher extends CommonLauncher {

    private static final Logger LOG = Logger.getLogger(BncLauncher.class);

    /**
     * @param args
     */
    public static void main(String... args) throws ConfigurationException {
        showDefaults();
        new BncLauncher(new XMLConfiguration("bouncer.xml")).loadProfiles(args);
    }

    /**
     * @param config
     */
    protected BncLauncher(HierarchicalConfiguration config) {
        super(config);
    }

    @Override
    protected void loadProfile(String profile) {
        String uri = config.getString("server/@uri");
        String serverName = config.getString("server/@alias");
        String serverClass = config.getString("server/@class");
        IIrcServerPeer serverPeer = loadServerClass(serverClass, serverName, uri);
        AccountStore dummy = new DummyAccountStore(serverPeer);
        dummy.reload();

        LOG.info("loading profile: " + profile);
        String profileCriteria = "profil[@name='" + profile + "']";

        int port = config.getInt(profileCriteria + "/@port");
        BncServerConnection bouncer = new BncServerConnection(dummy, port);
        bouncer.tstart();
    }
}
