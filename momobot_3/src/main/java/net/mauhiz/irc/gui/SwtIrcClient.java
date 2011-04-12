package net.mauhiz.irc.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
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
    protected final Map<IrcServer, SwtServerTab> serverTabs = new HashMap<IrcServer, SwtServerTab>();
    protected final Shell shell;

    public SwtIrcClient() {
        super();
        shell = new Shell(Display.getDefault());
        shell.setLayout(new FillLayout());
        folderBar = new CTabFolder(shell, SWT.BORDER);
    }

    public SwtChanTab createChanTab(IrcServer server, IrcChannel channel) {
        SwtChanTab tab = new SwtChanTab(this, server, channel);
        chanTabs.put(channel, tab);
        return tab;
    }

    public SwtPrivateTab createPrivateTab(IrcServer server, IrcUser user) {
        SwtPrivateTab tab = new SwtPrivateTab(this, server, user);
        privateTabs.put(user, tab);
        return tab;
    }

    public SwtServerTab createServerTab(IrcServer server) {
        SwtServerTab tab = new SwtServerTab(this, server);
        serverTabs.put(server, tab);
        return tab;
    }

    public void doConnect(IrcServer server) {
        gtm.getClient().connect(server);
        createServerTab(server);
    }

    public void doDisconnect(IrcServer server) {
        gtm.getClient().quit(server);
        serverTabs.remove(server);
        // TODO remove channels too
    }

    void doJoin(IrcServer server, String chanName) {
        Join msg = new Join(server, chanName);
        IIrcControl control = gtm.getClient();
        assert control != null;
        control.sendMsg(msg);
        IrcChannel channel = server.findChannel(chanName, true);
        SwtChanTab tab = createChanTab(server, channel);
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
        String chanName;

        if (MomoStringUtils.isChannelName(msg.getTo())) {
            chanName = msg.getTo();

        } else if (msg instanceof Part) {
            chanName = ((Part) msg).getChan();

        } else if (msg instanceof Join) {
            chanName = ((Join) msg).getChan();

        } else if (msg instanceof Kick) {
            chanName = ((Kick) msg).getChan();

        } else {
            return false;
        }

        IrcChannel channel = msg.getServer().findChannel(chanName);
        SwtChanTab chanTab = chanTabs.get(channel);

        if (chanTab == null) {
            LOG.warn("Missing chan tab: " + channel);
            return false;
        }

        chanTab.appendText(msg.toString());

        if (msg instanceof SetTopic) {
            chanTab.updateTopic(channel.getProperties().getTopic());
        }

        return true;
    }

    private boolean processPrivateLog(IIrcMessage msg) {
        String from = msg.getFrom();
        Mask mask = new Mask(from);
        if (mask.getNick() == null) {
            return false;
        }
        IrcServer server = msg.getServer();
        IrcUser user = server.findUser(mask, true);
        SwtPrivateTab privateTab = privateTabs.get(user);
        if (privateTab == null) {
            // create tab
            privateTab = createPrivateTab(server, user);
        }
        privateTab.appendText(msg.toString());
        return true;
    }

    private boolean processServerLog(IIrcMessage msg) {
        IrcServer server = msg.getServer();
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
        SwtTab defaut = initDefaultTab();

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

    private void swtLoop(SwtTab logTab) {
        final IIrcMessage msg = gtm.nextMsg();

        if (msg == null) { // booh, no new message
            return;
        }

        if (logTab != null) {
            logTab.appendText(msg.getIrcForm());
        }

        boolean processed = processChanLog(msg);

        if (!processed) {
            if (msg instanceof Notice || msg instanceof Privmsg) {
                // TODO private msgs
                processed = processPrivateLog(msg);
            }

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
