package net.mauhiz.irc.gui;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Part;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class SwtChanTab extends SwtTab {

    private final List usersInChan;

    public SwtChanTab(final SwtIrcClient swtIrcClient, final String channel) {
        super(swtIrcClient);

        folder.setText(channel);

        compo = new Composite(swtIrcClient.folderBar, SWT.DEFAULT);

        /* layout */
        GridLayout gridLayout = new GridLayout(2, false);
        compo.setLayout(gridLayout);

        folder.setControl(compo);

        initReceiveBox(compo);
        usersInChan = new List(compo, SWT.BORDER | SWT.V_SCROLL);
        initTypeBar(compo);
        swtIrcClient.folders.put(channel, this);

        // leave channel on close tab
        swtIrcClient.folderBar.addCTabFolder2Listener(new CTabFolder2Adapter() {
            @Override
            public void close(CTabFolderEvent event) {
                IrcServer server = GuiLauncher.qnet;
                Part msg = new Part(server, channel, "@+");
                IIrcControl control = swtIrcClient.gtm.getClient();
                assert control != null;
                control.sendMsg(msg);
                swtIrcClient.cut.removeChannel(server.findChannel(channel, true));
            }
        });
    }

    public List getUsersInChan() {
        return usersInChan;
    }
}