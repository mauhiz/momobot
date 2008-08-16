package net.mauhiz.irc.gui;

import java.io.IOException;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.gui.actions.ConnectAction;
import net.mauhiz.irc.gui.actions.ExitAction;
import net.mauhiz.irc.gui.actions.JoinAction;
import net.mauhiz.irc.gui.actions.SendAction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author mauhiz
 */
public class GuiLauncher {
    static IrcServer qnet;
    static {
        qnet = new IrcServer("irc://uk.quakenet.org:6667/");
        qnet.setMyLogin("mmb");
        qnet.setMyFullName("momobot le 3eme");
        qnet.setAlias("Quakenet");
        qnet.setMyNick("momobot3");
    }
    
    /**
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        final GuiTriggerManager gtm = new GuiTriggerManager();
        Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setMinimumSize(800, 600);
        /* layout */
        GridLayout gridLayout = new GridLayout(3, false);
        shell.setLayout(gridLayout);
        /* menu */
        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);
        MenuItem fileConnectItem = new MenuItem(fileMenu, SWT.PUSH);
        fileConnectItem.setText("&Connect Quakenet");
        fileConnectItem.addSelectionListener(new ConnectAction(gtm, qnet));
        MenuItem fileJoinItem = new MenuItem(fileMenu, SWT.PUSH);
        fileJoinItem.setText("&Join #tsi.fr");
        fileJoinItem.addSelectionListener(new JoinAction(gtm, qnet, "#tsi.fr"));
        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileExitItem.setText("E&xit");
        fileExitItem.addSelectionListener(new ExitAction(shell));
        shell.setMenuBar(menuBar);
        /* Affichage des logs */
        Label label = new Label(shell, SWT.NONE);
        label.setText("Log");
        Text logInfo = new Text(shell, SWT.WRAP | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        logInfo.setLayoutData(gridData);
        logInfo.setText("");
        logInfo.setEditable(false);
        /* Affichage de la barre de saisie */
        Label label2 = new Label(shell, SWT.NONE);
        label2.setText("Input");
        Text inputBar = new Text(shell, SWT.WRAP | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);
        GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        gridData2.horizontalSpan = 1;
        gridData2.grabExcessHorizontalSpace = true;
        inputBar.setLayoutData(gridData2);
        inputBar.setText("");
        inputBar.setEditable(true);
        Button send = new Button(shell, SWT.PUSH);
        send.setText("envoyer");
        send.addSelectionListener(new SendAction(inputBar, gtm, qnet, "#tsi.fr"));
        /* go afficher */
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            IIrcMessage msg = gtm.nextMsg();
            if (msg != null) {
                logInfo.setText(logInfo.getText() + "\r\n" + msg.toString());
                continue;
            }
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
        gtm.getClient().exit();
    }
}
