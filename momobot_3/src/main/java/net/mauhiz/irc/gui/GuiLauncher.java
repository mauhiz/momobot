package net.mauhiz.irc.gui;

import java.io.IOException;

import net.mauhiz.irc.base.trigger.TriggerExecutor;

/**
 * @author mauhiz
 */
public class GuiLauncher {

    /**
     * @param args
     */
    public static void main(String... args) throws IOException {
        new SwtIrcClient().start();
        TriggerExecutor.getInstance().shutdown();
    }
}
