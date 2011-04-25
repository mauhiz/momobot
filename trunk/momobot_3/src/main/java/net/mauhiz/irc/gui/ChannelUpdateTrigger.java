package net.mauhiz.irc.gui;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.base.trigger.IJoinTrigger;
import net.mauhiz.irc.base.trigger.IKickTrigger;
import net.mauhiz.irc.base.trigger.IPartTrigger;
import net.mauhiz.irc.base.trigger.IQuitTrigger;

public class ChannelUpdateTrigger implements IJoinTrigger, IPartTrigger, IQuitTrigger, IKickTrigger {

    static class UserListUpdater implements Runnable {
        private final IrcChannel channel;
        private final org.eclipse.swt.widgets.List userList;

        UserListUpdater(org.eclipse.swt.widgets.List userList, IrcChannel channel) {
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

    private final Map<IrcChannel, org.eclipse.swt.widgets.List> userLists = new HashMap<IrcChannel, org.eclipse.swt.widgets.List>();

    public void addChannel(IrcChannel channel, org.eclipse.swt.widgets.List list) {
        userLists.put(channel, list);
    }

    @Override
    public void doTrigger(Join im, IIrcControl control) {
        doUpdate(im.getChans());
    }

    @Override
    public void doTrigger(Kick im, IIrcControl control) {
        doUpdate(im.getChans());
    }

    @Override
    public void doTrigger(Part im, IIrcControl control) {
        doUpdate(im.getChans());
    }

    @Override
    public void doTrigger(Quit im, IIrcControl control) {
        doUpdate(null);
    }

    private void doUpdate(IrcChannel[] channels) {
        Collection<IrcChannel> chans = channels == null ? userLists.keySet() : Arrays.asList(channels);
        for (IrcChannel channel : chans) {
            org.eclipse.swt.widgets.List userList = userLists.get(channel);

            if (userList != null) {
                userList.getDisplay().asyncExec(new UserListUpdater(userList, channel));
            }
        }
    }

    public void removeChannel(IrcChannel channel) {
        userLists.remove(channel);
    }

}
