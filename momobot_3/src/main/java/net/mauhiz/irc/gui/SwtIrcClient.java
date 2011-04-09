package net.mauhiz.irc.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.gui.actions.ConnectAction;
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

    public SwtServerTab createServerTab(IrcServer server) {
        SwtServerTab tab = new SwtServerTab(this, server);
        serverTabs.put(server, tab);
        return tab;
    }

    public void doConnect(IrcServer server) {
        gtm.getClient().connect(server);
        createServerTab(server);
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

    private SwtTab initDefaultTab() {
        return null; // TODO log tag
    }

    private void initMenus() {

        /* menu */
        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);
        MenuItem fileConnectItem = new MenuItem(fileMenu, SWT.PUSH);
        fileConnectItem.setText("&Connect Quakenet");
        fileConnectItem.addSelectionListener(new ConnectAction(this, GuiLauncher.qnet));

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
        if (msg == null) {
            return;
        }
        if (logTab != null) {
            logTab.appendText(msg.getIrcForm());
        }

        String to = msg.getTo();
        if (MomoStringUtils.isChannelName(to)) {
            SwtChanTab chanTab = chanTabs.get(to.toLowerCase(Locale.ENGLISH));
            if (chanTab != null) {
                chanTab.appendText(msg.toString());
            } else {
                LOG.warn("Missing chan tab: " + to);
            }
        } else if (msg.getFrom() != null && msg.getFrom().indexOf('.') == 0) {
            // TODO private msgs
            LOG.warn("Unhandled msg on GUI: " + msg.getIrcForm());
        } else {
            IrcServer server = msg.getServer();
            SwtServerTab serverTab = serverTabs.get(server);
            if (serverTab != null) {
                serverTab.appendText(msg.toString());
            } else {
                LOG.warn("Missing server tab: " + server);
            }
        }
    }
}
