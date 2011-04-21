package net.mauhiz.irc.gui;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.gui.actions.SendAction;
import net.mauhiz.irc.gui.actions.SendMeAction;
import net.mauhiz.irc.gui.actions.SendNoticeAction;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SwtPrivateTab extends AbstractSwtTab {

    IIrcServerPeer server;
    final IrcUser user;

    public SwtPrivateTab(final SwtIrcClient swtIrcClient, final IIrcServerPeer server, final IrcUser user) {
        super(swtIrcClient);
        this.server = server;
        this.user = user;

        folder.setText(getFolderName());
        initReceiveBox(compo);
        initTypeBar();
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
        Text inputBar = new Text(typeZone, SWT.WRAP | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);
        inputBar.setText(StringUtils.EMPTY);
        inputBar.setEditable(true);
        setTextSize(inputBar, 1, 400);
        inputBar.setBackground(inputBar.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));

        Button sendText = new Button(typeZone, SWT.PUSH);
        sendText.setText("Send text");
        sendText.addSelectionListener(new SendAction(inputBar, swtIrcClient.gtm, server, getTarget()));

        Button sendNotice = new Button(typeZone, SWT.PUSH);
        sendNotice.setText("Send notice");
        sendNotice.addSelectionListener(new SendNoticeAction(inputBar, swtIrcClient.gtm, server, getTarget()));

        Button sendAction = new Button(typeZone, SWT.PUSH);
        sendAction.setText("Send action");
        sendAction.addSelectionListener(new SendMeAction(inputBar, swtIrcClient.gtm, server, getTarget()));
    }
}