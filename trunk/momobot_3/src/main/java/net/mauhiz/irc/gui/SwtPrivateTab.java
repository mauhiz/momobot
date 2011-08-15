package net.mauhiz.irc.gui;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.gui.actions.SendAction;
import net.mauhiz.irc.gui.actions.SendMeAction;
import net.mauhiz.irc.gui.actions.SendNoticeAction;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SwtPrivateTab extends AbstractSwtTab {

    class CloseHandler extends CTabFolder2Adapter {

        @Override
        public void close(CTabFolderEvent event) {
            if (event.item == folder) {
                swtIrcClient.privateTabs.remove(user);
                Logger.getLogger(SwtChanTab.class).info("Closed query tab: " + user);
            }
        }
    }

    IIrcServerPeer server;

    final IrcUser user;

    public SwtPrivateTab(final SwtIrcClient swtIrcClient, final IIrcServerPeer server, final IrcUser user) {
        super(swtIrcClient);
        this.server = server;
        this.user = user;

        folder.setText(getFolderName());
        initReceiveBox(compo);
        initTypeBar();

        swtIrcClient.folderBar.addCTabFolder2Listener(new CloseHandler());
    }

    @Override
    protected final String getFolderName() {
        return user.getNick();
    }

    protected final Target getTarget() {
        return user;
    }

    protected final void initTypeBar() {
        Composite typeZone = new Composite(compo, SWT.BORDER);
        typeZone.setLayout(new GridLayout(4, false));

        /* Affichage de la barre de saisie */
        final Text inputBar = new Text(typeZone, SWT.WRAP | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);
        inputBar.setText(StringUtils.EMPTY);
        inputBar.setEditable(true);
        setTextSize(inputBar, 1, 400);
        inputBar.setBackground(inputBar.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));

        Button sendText = new Button(typeZone, SWT.PUSH);
        sendText.setText("Send text");
        sendText.addSelectionListener(new SendAction(inputBar, swtIrcClient.gtm, server, getTarget()));

        inputBar.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.keyCode == '\r') {
                    new SendAction(inputBar, swtIrcClient.gtm, server, user).run();
                }
            }
        });

        Button sendNotice = new Button(typeZone, SWT.PUSH);
        sendNotice.setText("Send notice");
        sendNotice.addSelectionListener(new SendNoticeAction(inputBar, swtIrcClient.gtm, server, getTarget()));

        Button sendAction = new Button(typeZone, SWT.PUSH);
        sendAction.setText("Send action");
        sendAction.addSelectionListener(new SendMeAction(inputBar, swtIrcClient.gtm, server, getTarget()));
    }
}