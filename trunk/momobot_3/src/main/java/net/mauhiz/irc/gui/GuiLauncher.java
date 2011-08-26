package net.mauhiz.irc.gui;

import net.mauhiz.irc.CommonLauncher;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * @author mauhiz
 */
public class GuiLauncher extends CommonLauncher {

    /**
     * @param args
     */
    public static void main(String... args) throws ConfigurationException {
        showDefaults();
        new GuiLauncher(new XMLConfiguration("gui.xml")).loadProfiles(args);
    }

    protected GuiLauncher(HierarchicalConfiguration config) {
        super(config);
    }

    @Override
    protected void loadProfile(String arg) {
        new SwtIrcClient().start();
    }
}
