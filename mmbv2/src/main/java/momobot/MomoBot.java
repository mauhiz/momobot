package momobot;

import ircbot.AbstractIrcBot;
import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.dcc.DccChat;
import ircbot.dcc.DccFileTransfer;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IJoinTrigger;
import ircbot.trigger.INoticeTrigger;
import ircbot.trigger.IPriveTrigger;
import ircbot.trigger.IPublicTrigger;
import ircbot.trigger.ITrigger;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import momobot.whois.Whois;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.log4j.Logger;

/**
 * Cette classe suit le design pattern Factory.
 * 
 * @author mauhiz
 */
@SuppressWarnings("unused")
public class MomoBot extends AbstractIrcBot {
    /**
     * la liste des channels à autojoin.
     */
    public static final Collection<String> AUTOJOIN = new TreeSet<String>();
    /**
     * référence au bot.
     */
    private static MomoBot                 instance;
    /**
     * logger.
     */
    private static final Logger            LOG      = Logger.getLogger(MomoBot.class);

    /**
     * @param channel
     *            le channel à autojoin
     */
    public static void addToAutoJoin(final String channel) {
        AUTOJOIN.add(channel.toLowerCase(Locale.FRANCE));
    }

    /**
     * @return une instance de momobot
     */
    public static MomoBot getBotInstance() {
        if (null == instance) {
            instance = new MomoBot();
        }
        return instance;
    }
    /**
     * la liste des whois en cours.
     */
    private final Map<String, Whois> whois = new ConcurrentHashMap<String, Whois>();

    /**
     * @param nick
     *            le nom à whoiser
     * @param whois1
     *            The whois to set.
     */
    public final void addWhois(final String nick, final Whois whois1) {
        this.whois.put(nick, whois1);
    }

    /**
     * @param string
     *            le nom
     * @return si ce whois est en cours
     */
    public final boolean isBeingWhoised(final String string) {
        return this.whois.containsKey(string);
    }

    /**
     * @see AbstractIrcBot#onAction(IrcUser, String, String)
     * @param action
     *            l'action
     */
    public final void onAction(final IrcUser user, final String target, final String action) {
        if (LOG.isInfoEnabled()) {
            LOG.info(user.getNick() + SPC + target + "%" + action);
        }
    }

    /**
     * @see ircbot.IIrcBot#onChannelInfo(String, int, String)
     */
    public void onChannelInfo(final String channel, final int userCount, final String topic) {
        // TODO Auto-generated method stub
    }

    /**
     * @see AbstractIrcBot#onConnect()
     */
    @Override
    public final void onConnect() {
        for (final String string : AUTOJOIN) {
            joinChannel(string);
        }
    }

    /**
     * @see ircbot.IIrcBot#onFileTransferFinished(DccFileTransfer, Exception)
     */
    public void onFileTransferFinished(final DccFileTransfer transfer, final Exception exception) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onIncomingChatRequest(DccChat)
     */
    public void onIncomingChatRequest(final DccChat chat) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onIncomingFileTransfer(DccFileTransfer)
     */
    public void onIncomingFileTransfer(final DccFileTransfer transfer) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onInvite(String, IrcUser, String)
     */
    public void onInvite(final String targetNick, final IrcUser user, final String channel) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onJoin(Channel, IrcUser)
     * @param channel
     *            le channel
     * @param user
     *            le user
     */
    @Override
    public final void onJoin(final Channel channel, final IrcUser user) {
        if (channel == null) {
            LOG.error("onJoin : channel is null!");
            return;
        }
        for (final ITrigger trigger : AbstractTrigger.getTriggers()) {
            if (!(trigger instanceof IJoinTrigger)) {
                continue;
            }
            ((IJoinTrigger) trigger).executeJoinTrigger(channel, user);
        }
    }

    /**
     * @see ircbot.IIrcBot#onMessage(Channel, IrcUser, String)
     * @param channel
     *            le channel
     */
    public final void onMessage(final Channel channel, final IrcUser user, final String message) {
        if (LOG.isInfoEnabled()) {
            LOG.info(user.getNick() + channel + ":" + message);
        }
        for (final ITrigger trigger : AbstractTrigger.getTriggers()) {
            if (!(trigger instanceof IPublicTrigger)) {
                continue;
            }
            if (trigger.isActivatedBy(message)) {
                ((IPublicTrigger) trigger).executePublicTrigger(user, channel, message);
            }
        }
    }

    /**
     * @see ircbot.IIrcBot#onMode(Channel, IrcUser, String)
     */
    public void onMode(final Channel channel, final IrcUser user, final String mode) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onNickChange(IrcUser, String)
     */
    public void onNickChange(final IrcUser user, final String newNick) {
        // TODO Auto-generated method stub
    }

    /**
     * @see AbstractIrcBot#onNotice(IrcUser, String, String)
     * @param user
     *            le user
     * @param target
     *            les destinataires
     * @param notice
     *            le message
     */
    public final void onNotice(final IrcUser user, final String target, final String notice) {
        if (null == user) {
            if (LOG.isInfoEnabled()) {
                LOG.info('&' + notice);
            }
            return;
        }
        if (LOG.isInfoEnabled()) {
            LOG.info(new StrBuilder().append(user).append('&').append(notice));
        }
        for (final ITrigger trigger : AbstractTrigger.getTriggers()) {
            if (!(trigger instanceof INoticeTrigger)) {
                continue;
            }
            if (trigger.isActivatedBy(notice)) {
                ((INoticeTrigger) trigger).executeNoticeTrigger(user, notice);
            }
        }
    }

    /**
     * @see AbstractIrcBot#onPrivateMessage(IrcUser, String)
     */
    public void onPrivateMessage(final IrcUser user, final String message) {
        if (LOG.isInfoEnabled()) {
            LOG.info(user + ":" + message);
        }
        for (final ITrigger trigger : AbstractTrigger.getTriggers()) {
            if (!(trigger instanceof IPriveTrigger)) {
                continue;
            }
            if (trigger.isActivatedBy(message)) {
                ((IPriveTrigger) trigger).executePrivateTrigger(user, message);
            }
        }
    }

    /**
     * @see ircbot.IIrcBot#onRemoveChannelBan(Channel, IrcUser, String)
     */
    public void onRemoveChannelBan(final Channel channel, final IrcUser user, final String hostmask) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onRemoveModerated(Channel, IrcUser)
     */
    public void onRemoveModerated(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onRemoveNoExternalMessages(Channel, IrcUser)
     */
    public void onRemoveNoExternalMessages(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onRemovePrivate(Channel, IrcUser)
     */
    public void onRemovePrivate(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onRemoveSecret(Channel, IrcUser)
     */
    public void onRemoveSecret(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onServerResponse(int, java.lang.String)
     * @param code
     *            le code rfc
     * @param response
     *            le message du serveur
     */
    public void onServerResponse(final int code, final String response) {
        StrTokenizer st;
        String nick;
        Whois localWhois;
        switch (code) {
            case RPL_WHOISUSER :
                /* rien */
                break;
            case RPL_WHOISCHANNELS :
                /* osef */
                break;
            case RPL_WHOISSERVER :
                /* osef */
                break;
            case RPL_WHOISAUTH :
                st = new StrTokenizer(response, SPC);
                st.nextToken();
                nick = st.nextToken();
                localWhois = this.whois.get(nick);
                localWhois.setQnetAuth(st.nextToken());
                break;
            case RPL_WHOISIDLE :
                /* osef */
                break;
            case RPL_ENDOFWHOIS :
                st = new StrTokenizer(response, SPC);
                st.nextToken();
                nick = st.nextToken();
                localWhois = this.whois.get(nick);
                localWhois.setDone(true);
                /* le thread va s'arreter tout seul tfacon */
                this.whois.remove(nick);
                break;
            default :
                break;
        }
    }

    /**
     * @see ircbot.IIrcBot#onSetChannelBan(Channel, IrcUser, String)
     */
    public void onSetChannelBan(final Channel channel, final IrcUser user, final String hostmask) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onSetModerated(Channel, IrcUser)
     */
    public void onSetModerated(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onSetNoExternalMessages(Channel, IrcUser)
     */
    public void onSetNoExternalMessages(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onSetPrivate(Channel, IrcUser)
     */
    public void onSetPrivate(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onSetSecret(Channel, IrcUser)
     */
    public void onSetSecret(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onUnknown(String)
     */
    public void onUnknown(final String line) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ircbot.IIrcBot#onUserList(Channel, Collection)
     * @param channel
     *            le channel
     * @param users
     *            les users
     */
    public final void onUserList(final Channel channel, final Collection<Entry<String, String>> users) {
        LOG.debug("onUserList channel : " + channel);
        for (final Entry<String, String> user : users) {
            LOG.debug("onUserList user : " + user.getKey() + " | " + user.getValue());
            final IrcUser iu = IrcUser.getUser(user.getKey());
            if (null == channel) {
                return;
            } else if (iu == null) {
                LOG.warn("onUserList : user is null");
                return;
            }
            if (AUTOJOIN.contains(channel.getNom().toLowerCase(Locale.US))) {
                new Whois(iu).execute();
            }
            channel.addUser(iu, user.getValue());
        }
    }

    /**
     * @see ircbot.IIrcBot#onUserMode(String, IrcUser, String)
     */
    public final void onUserMode(final String targetNick, final IrcUser user, final String mode) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(user + " sets mode " + targetNick + " " + mode);
        }
    }

    /**
     * @param target
     *            la cible
     * @param message
     *            le message
     */
    public void sendMessage(final Channel target, final String message) {
        sendMessage(target.getNom(), message);
    }

    /**
     * @param target
     *            la cible
     * @param message
     *            le message
     */
    public void sendMessage(final IrcUser target, final String message) {
        sendMessage(target.getNick(), message);
    }
}
