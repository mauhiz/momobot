package net.mauhiz.irc.gui;

import java.io.IOException;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetServer;
import net.mauhiz.irc.base.trigger.TriggerExecutor;

/**
 * @author mauhiz
 */
public class GuiLauncher {
    public static final IrcServer qnet;
    static {
        qnet = new QnetServer("irc://uk.quakenet.org:6667/");
        qnet.setAlias("Quakenet");

        IrcUser myself = qnet.newUser("momobot3");
        myself.setUser("mmb");
        myself.setFullName("momobot le 3eme");
        qnet.setMyself(myself);
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        new SwtIrcClient().start();
        TriggerExecutor.getInstance().shutdown();
    }
}
