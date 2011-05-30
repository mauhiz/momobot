package net.mauhiz.irc.gui;

import java.io.IOException;

/**
 * @author mauhiz
 */
public class GuiLauncher {

    /**
     * @param args
     */
    public static void main(String... args) throws IOException {
        new SwtIrcClient().start();
    }
}
