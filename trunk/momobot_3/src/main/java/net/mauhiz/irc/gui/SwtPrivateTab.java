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
        GridLayout gridLayout = new GridLayout(1, false);
        compo.setLayout(gridLayout);

        initReceiveBox();
        initTypeBar();
    }

    protected String getTarget() {
        return user.getNick() + "!" + user.getUser() + "@" + user.getHost();
    }

    protected void initTypeBar() {
        Composite typeZone = new Composite(compo, SWT.BORDER);
        typeZone.setLayout(new GridLayout(3, false));

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
    }
}