package net.mauhiz.irc.gui;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.gui.actions.SendAction;
import net.mauhiz.irc.gui.actions.SendNoticeAction;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SwtPrivateTab extends SwtTab {

    IrcServer server;
    final IrcUser user;

    public SwtPrivateTab(final SwtIrcClient swtIrcClient, final IrcServer server, final IrcUser user) {
        super(swtIrcClient);
        this.server = server;
        this.user = user;
        folder.setText(user.getNick());

        /* layout */
        GridLayout gridLayout = new GridLayout(2, false);
        compo.setLayout(gridLayout);

        initReceiveBox();
        initTypeBar();
    }

    protected String getTarget() {
        return user.getNick() + "!" + user.getUser() + "@" + user.getHost();
    }

    protected void initTypeBar() {
        /* Affichage de la barre de saisie */
        Text inputBar = new Text(compo, SWT.WRAP | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);
        inputBar.setText(StringUtils.EMPTY);
        inputBar.setEditable(true);
        setTextSize(inputBar, 1, 400);
        inputBar.setBackground(inputBar.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));

        Composite buttonZone = new Composite(compo, SWT.BORDER);
        buttonZone.setLayout(new GridLayout(1, false));
        Button sendText = new Button(buttonZone, SWT.PUSH);
        sendText.setText("Send text");
        sendText.addSelectionListener(new SendAction(inputBar, swtIrcClient.gtm, server, getTarget()));

        Button sendNotice = new Button(buttonZone, SWT.PUSH);
        sendNotice.setText("Send notice");
        sendNotice.addSelectionListener(new SendNoticeAction(inputBar, swtIrcClient.gtm, server, getTarget()));
    }
}