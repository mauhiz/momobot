package net.mauhiz.irc.gui;

import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.trigger.IJoinTrigger;
import net.mauhiz.irc.base.trigger.IPartTrigger;

import org.eclipse.swt.widgets.List;

public class ChannelUpdateTrigger implements IJoinTrigger, IPartTrigger {

    static class UserListUpdater implements Runnable {
        private final IrcChannel channel;
        private final List userList;

        UserListUpdater(List userList, IrcChannel channel) {
            this.userList = userList;
            this.channel = channel;
        }

        @Override
        public void run() {
            userList.removeAll();

            for (IrcUser user : channel) {
                userList.add(user.getNick());
            }
        }
    }

    private final Map<IrcChannel, List> userLists = new HashMap<IrcChannel, List>();

    public void addChannel(IrcChannel channel, List list) {
        userLists.put(channel, list);
    }

    @Override
    public void doTrigger(Join im, IIrcControl control) {
        doUpdate(im.getServerPeer(), im.getTo());
    }

    @Override
    public void doTrigger(Part im, IIrcControl control) {
        doUpdate(im.getServerPeer(), im.getTo());
    }

    private void doUpdate(IIrcServerPeer server, IrcChannel channel) {

        if (channel != null) {
            final List userList = userLists.get(channel);

            if (userList != null) {
                userList.getDisplay().syncExec(new UserListUpdater(userList, channel));
            }
        }
    }

    public void removeChannel(IrcChannel channel) {
        userLists.remove(channel);
    }

}
