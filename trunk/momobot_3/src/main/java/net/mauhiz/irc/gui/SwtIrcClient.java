package net.mauhiz.irc.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.IrcChannelMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.SetTopic;
import net.mauhiz.irc.gui.actions.ExitAction;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class SwtIrcClient {
    static final Logger LOG = Logger.getLogger(SwtIrcClient.class);
    final Map<IrcChannel, SwtChanTab> chanTabs = new HashMap<IrcChannel, SwtChanTab>();
    protected final ChannelUpdateTrigger cut = new ChannelUpdateTrigger();
    protected CTabFolder folderBar;
    protected final GuiTriggerManager gtm = new GuiTriggerManager();
    protected final Map<IrcUser, SwtPrivateTab> privateTabs = new HashMap<IrcUser, SwtPrivateTab>();
    protected final Map<IIrcServerPeer, SwtServerTab> serverTabs = new HashMap<IIrcServerPeer, SwtServerTab>();
    protected final Shell shell;

    public SwtIrcClient() {
        super();
        shell = new Shell(Display.getDefault());
        shell.setLayout(new FillLayout());
        folderBar = new CTabFolder(shell, SWT.BORDER);
    }

    public SwtChanTab createChanTab(IIrcServerPeer server, IrcChannel channel) {
        SwtChanTab tab = chanTabs.get(channel);
        if (tab != null) {
            return tab;
        }
        tab = new SwtChanTab(this, server, channel);
        chanTabs.put(channel, tab);
        return tab;
    }

    public SwtPrivateTab createPrivateTab(IIrcServerPeer server, IrcUser user) {
        SwtPrivateTab tab = privateTabs.get(user);
        if (tab != null) {
            return tab;
        }
        tab = new SwtPrivateTab(this, server, user);
        privateTabs.put(user, tab);
        return tab;
    }

    public SwtServerTab createServerTab(IIrcServerPeer server) {
        SwtServerTab tab = serverTabs.get(server);
        if (tab != null) {
            return tab;
        }
        tab = new SwtServerTab(this, server);
        serverTabs.put(server, tab);
        return tab;
    }

    public void doConnect(IIrcServerPeer server) {
        createServerTab(server);
        gtm.getClient().connect(server);
    }

    public void doDisconnect(IIrcServerPeer server) {
        serverTabs.remove(server);
        gtm.getClient().quit(server);
        // TODO remove channels too
    }

    void doJoin(IIrcServerPeer server, IrcChannel channel) {
        Join msg = new Join(server, channel);
        SwtChanTab tab = createChanTab(server, channel);
        IIrcControl control = gtm.getClient();
        control.sendMsg(msg);
        cut.addChannel(channel, tab.getUsersInChan());
    }

    public Shell getShell() {
        return shell;
    }

    private SwtLogTab initDefaultTab() {
        return new SwtLogTab(this);
    }

    private void initMenus() {

        /* menu */
        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);

        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileExitItem.setText("E&xit");
        fileExitItem.addSelectionListener(new ExitAction(shell));
        shell.setMenuBar(menuBar);
    }

    private void initShell() {
        shell.setSize(800, 600);
        shell.open();
        // let's not pack() ...
    }

    private boolean processChanLog(IIrcMessage msg) {
        IrcChannel[] channels;

        if (msg instanceof IrcChannelMessage) {
            channels = ((IrcChannelMessage) msg).getChans();

        } else {
            return false;
        }

        if (channels == null || channels.length == 0) {
            return false;
        }

        for (IrcChannel channel : channels) {
            SwtChanTab chanTab = chanTabs.get(channel);

            if (chanTab == null) {
                if (msg instanceof Part && msg.getServerPeer().getMyself().equals(msg.getFrom())) {
                    // this is me parting
                    return true;
                } else if (msg instanceof Kick && msg.getServerPeer().getMyself().equals(((Kick) msg).getTarget())) {
                    // this is me being kicked
                    return true;
                }

                LOG.warn("Missing chan tab: " + channel);
                return false;
            }

            chanTab.appendText(msg.toString());

            if (msg instanceof SetTopic) {
                chanTab.updateTopic(channel.getProperties().getTopic());
            }
        }

        return true;
    }

    private boolean processPrivateLog(IIrcMessage msg) {
        Target from = msg.getFrom();
        if (!(from instanceof IrcUser)) {
            return false;
        }
        IrcUser user = (IrcUser) from;
        SwtPrivateTab privateTab = privateTabs.get(user);
        if (privateTab == null) { // create tab
            privateTab = createPrivateTab(msg.getServerPeer(), user);
        }
        privateTab.appendText(msg.toString());
        return true;
    }

    private boolean processServerLog(IIrcMessage msg) {
        IIrcServerPeer server = msg.getServerPeer();
        SwtServerTab serverTab = serverTabs.get(server);
        if (serverTab == null) {
            LOG.warn("Missing server tab: " + server);
            return false;
        }

        serverTab.appendText("[" + msg.getClass().getSimpleName() + "] " + msg.toString());
        return true;
    }

    /**
     * SWT GUI.
     * One default CTabFolder + 1 per channel/private conversation
     */
    public void start() throws IOException {
        gtm.addTrigger(cut);
        initMenus();

        /* Affichage des logs */
        SwtLogTab defaut = initDefaultTab();

        /* go afficher */
        initShell();
        Display display = shell.getDisplay();

        while (!shell.isDisposed()) {
            swtLoop(defaut);

            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
        gtm.getClient().exit();
    }

    private void swtLoop(SwtLogTab logTab) {
        final IIrcMessage msg = gtm.nextMsg();

        if (msg == null) { // booh, no new message
            return;
        }

        if (logTab != null) {
            logTab.appendText(msg.getIrcForm());
        }

        boolean processed = processChanLog(msg);

        if (!processed) {
            processed = processPrivateLog(msg);
        }
        if (!processed) {
            processed = processServerLog(msg);
        }
        if (!processed) {

            // TODO private msgs
            LOG.warn("Unhandled msg on GUI: " + msg.getIrcForm());
        }
    }
}
