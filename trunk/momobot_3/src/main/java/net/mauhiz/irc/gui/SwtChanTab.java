package net.mauhiz.irc.gui;

import java.util.ArrayList;
import java.util.List;

import net.mauhiz.irc.base.data.ArgumentList;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Topic;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Mode;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.gui.actions.SendAction;
import net.mauhiz.irc.gui.actions.SendMeAction;
import net.mauhiz.irc.gui.actions.SendNoticeAction;
import net.mauhiz.util.AbstractAction;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

public class SwtChanTab extends AbstractSwtTab {

    class CloseHandler extends CTabFolder2Adapter {

        @Override
        public void close(CTabFolderEvent event) {
            if (event.item == folder && channel.contains(server.getMyself())) {
                Part msg = new Part(server, "@+", channel);
                swtIrcClient.gtm.client.sendMsg(msg);
                swtIrcClient.cut.removeChannel(channel);
                swtIrcClient.chanTabs.remove(channel);
                Logger.getLogger(SwtChanTab.class).info("Closed channel tab: " + channel);
            }
        }
    }

    class KickAction extends UserMenuAction {

        @Override
        protected void doAction() {
            List<IrcUser> users = findUsers();
            for (IrcUser user : users) {
                Kick kick = new Kick(server, null, channel, user, null);
                swtIrcClient.gtm.client.sendMsg(kick);
            }
        }
    }

    class OpAction extends UserMenuAction {

        @Override
        protected void doAction() {
            List<IrcUser> users = findUsers();
            for (IrcUser user : users) {
                String modeStr = "+o " + user; // TODO make this higher level
                Mode op = new Mode(server, null, channel, new ArgumentList(modeStr));
                swtIrcClient.gtm.client.sendMsg(op);
            }
        }
    }

    class QueryAction extends UserMenuAction {

        @Override
        protected void doAction() {
            List<IrcUser> users = findUsers();
            for (IrcUser user : users) {
                swtIrcClient.createPrivateTab(server, user);
            }
        }

    }

    abstract class UserMenuAction extends AbstractAction {
        protected List<IrcUser> findUsers() {
            List<IrcUser> users = new ArrayList<IrcUser>();

            String[] selectedNicks = usersInChan.getSelection();
            for (String selectedNick : selectedNicks) {
                users.add(server.getNetwork().findUser(selectedNick, false));
            }
            return users;
        }

        @Override
        protected boolean isAsynchronous() {
            return false;
        }
    }

    final IrcChannel channel;
    final IIrcServerPeer server;
    final Text topicBar;
    final org.eclipse.swt.widgets.List usersInChan;

    public SwtChanTab(final SwtIrcClient swtIrcClient, final IIrcServerPeer server, final IrcChannel channel) {
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
        usersInChan = new org.eclipse.swt.widgets.List(compo, SWT.BORDER | SWT.V_SCROLL);
        setListSize(usersInChan, 15, 150);
        initUserActionsMenu();
        initTypeBar();

        // leave channel on close tab
        swtIrcClient.folderBar.addCTabFolder2Listener(new CloseHandler());
    }

    public org.eclipse.swt.widgets.List getUsersInChan() {
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
        buttonZone.setLayout(new GridLayout(3, false));
        Button sendText = new Button(buttonZone, SWT.PUSH);
        sendText.setText("Send text");
        sendText.addSelectionListener(new SendAction(inputBar, swtIrcClient.gtm, server, channel));

        Button sendNotice = new Button(buttonZone, SWT.PUSH);
        sendNotice.setText("Send notice");
        sendNotice.addSelectionListener(new SendNoticeAction(inputBar, swtIrcClient.gtm, server, channel));

        Button sendAction = new Button(buttonZone, SWT.PUSH);
        sendAction.setText("Send action");
        sendAction.addSelectionListener(new SendMeAction(inputBar, swtIrcClient.gtm, server, channel));
    }

    private void initUserActionsMenu() {
        Menu userActions = new Menu(usersInChan);
        MenuItem query = new MenuItem(userActions, SWT.DEFAULT);
        query.setText("&Query");
        query.addSelectionListener(new QueryAction());

        MenuItem kick = new MenuItem(userActions, SWT.DEFAULT);
        kick.setText("&Kick");
        kick.addSelectionListener(new KickAction());

        MenuItem op = new MenuItem(userActions, SWT.DEFAULT);
        op.setText("&Op");
        op.addSelectionListener(new OpAction());

        usersInChan.setMenu(userActions);
    }

    public void setTopicEditable(boolean editable) {
        topicBar.setEditable(editable);
    }

    public void updateTopic(Topic topic) {
        topicBar.setText(topic.toString());
    }
}