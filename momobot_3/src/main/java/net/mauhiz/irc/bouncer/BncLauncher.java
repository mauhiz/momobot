package net.mauhiz.irc.bouncer;

import net.mauhiz.irc.CommonLauncher;

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
    protected void loadProfile(String arg) {
        AccountStore dummy = new DummyAccountStore();
        dummy.reload();

        BncServerConnection bouncer = new BncServerConnection(dummy, 6667);
        bouncer.tstart();
    }
}
