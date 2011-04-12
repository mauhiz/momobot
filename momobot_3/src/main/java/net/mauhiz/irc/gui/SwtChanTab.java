package net.mauhiz.irc.gui;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Topic;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.gui.actions.SendAction;
import net.mauhiz.irc.gui.actions.SendNoticeAction;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class SwtChanTab extends SwtTab {

    final IrcChannel channel;
    IrcServer server;
    final Text topicBar;
    private final List usersInChan;

    public SwtChanTab(final SwtIrcClient swtIrcClient, final IrcServer server, final IrcChannel channel) {
        super(swtIrcClient);
        this.server = server;
        this.channel = channel;
        folder.setText(channel.fullName());

        /* layout */
        GridLayout gridLayout = new GridLayout(2, false);
        compo.setLayout(gridLayout);

        // topic bar
        topicBar = new Text(compo, SWT.WRAP | SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);

        // control buttons (chan flags...)
        Composite controlGroup = new Composite(compo, SWT.BORDER);
        controlGroup.setBackground(controlGroup.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));

        initReceiveBox();
        usersInChan = new List(compo, SWT.BORDER | SWT.V_SCROLL);
        setListSize(usersInChan, 15, 150);
        initTypeBar();

        // leave channel on close tab
        swtIrcClient.folderBar.addCTabFolder2Listener(new CTabFolder2Adapter() {
            @Override
            public void close(CTabFolderEvent event) {
                if (event.item == folder && channel.contains(server.getMyself())) {
                    Part msg = new Part(server, channel, "@+");
                    IIrcControl control = swtIrcClient.gtm.getClient();
                    assert control != null;
                    control.sendMsg(msg);
                    swtIrcClient.cut.removeChannel(channel);
                }
            }
        });
    }

    public List getUsersInChan() {
        return usersInChan;
    }

    protected final void initTypeBar() {
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
        sendText.addSelectionListener(new SendAction(inputBar, swtIrcClient.gtm, server, channel));

        Button sendNotice = new Button(buttonZone, SWT.PUSH);
        sendNotice.setText("Send notice");
        sendNotice.addSelectionListener(new SendNoticeAction(inputBar, swtIrcClient.gtm, server, channel));
    }

    public void setTopicEditable(boolean editable) {
        topicBar.setEditable(editable);
    }

    public void updateTopic(Topic topic) {
        topicBar.setText(topic.toString());
    }
}