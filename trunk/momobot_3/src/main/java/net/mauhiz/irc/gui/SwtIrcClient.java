package net.mauhiz.irc.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.gui.actions.ConnectAction;
import net.mauhiz.irc.gui.actions.ExitAction;
import net.mauhiz.irc.gui.actions.JoinAction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class SwtIrcClient {
    protected final ChannelUpdateTrigger cut = new ChannelUpdateTrigger();
    protected final Display display = Display.getDefault();
    protected CTabFolder folderBar;
    protected Map<String, SwtChanTab> folders = new HashMap<String, SwtChanTab>();
    protected final GuiTriggerManager gtm = new GuiTriggerManager();
    protected final Shell shell;

    public SwtIrcClient() {
        super();
        shell = new Shell(display);
        shell.setLayout(new FillLayout());
        folderBar = new CTabFolder(shell, SWT.BORDER);
    }

    public SwtChanTab createTab(String channel) {
        return new SwtChanTab(this, channel);
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
        MenuItem fileConnectItem = new MenuItem(fileMenu, SWT.PUSH);
        fileConnectItem.setText("&Connect Quakenet");
        fileConnectItem.addSelectionListener(new ConnectAction(gtm, GuiLauncher.qnet));

        MenuItem fileJoinItem = new MenuItem(fileMenu, SWT.PUSH);
        fileJoinItem.setText("&Join #tsi.fr");
        fileJoinItem.addSelectionListener(new JoinAction(gtm, GuiLauncher.qnet, "#tsi.fr", cut, this));

        MenuItem fileJoinItem2 = new MenuItem(fileMenu, SWT.PUSH);
        fileJoinItem2.setText("&Join #-duCk-");
        fileJoinItem2.addSelectionListener(new JoinAction(gtm, GuiLauncher.qnet, "#-duCk-", cut, this));

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
        SwtLogTab defaut = initDefaultTab();

        /* go afficher */
        initShell();

        while (!shell.isDisposed()) {
            swtLoop(defaut);

            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();
        gtm.getClient().exit();
    }

    private void swtLoop(final SwtLogTab logTab) {
        final IIrcMessage msg = gtm.nextMsg();
        if (msg != null) {
            display.syncExec(new Runnable() {

                @Override
                public void run() {
                    logTab.appendText(msg.getIrcForm());
                    String to = msg.getTo();
                    if (MomoStringUtils.isChannelName(to)) {
                        SwtChanTab chanTab = folders.get(to);
                        if (chanTab != null) {
                            chanTab.appendText(msg.toString());
                        }
                    }
                }
            });
        }
    }
}
