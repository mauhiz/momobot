package momobot;

import ircbot.AbstractIrcBot;
import ircbot.Channel;
import ircbot.IIrcBot;
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
 * @author mauhiz
 */
@SuppressWarnings("unused")
public class MomoBot extends AbstractIrcBot {
    /**
     * la liste des channels à autojoin.
     */
    public static final Collection < String > AUTOJOIN = new TreeSet < String >();
    /**
     * référence au bot.
     */
    private static MomoBot                    instance;
    /**
     * logger.
     */
    private static final Logger               LOG      = Logger.getLogger(MomoBot.class);

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
    private final Map < String, Whois > whois = new ConcurrentHashMap < String, Whois >();

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
    @Override
    public final void onAction(final IrcUser user, final String target, final String action) {
        if (LOG.isInfoEnabled()) {
            LOG.info(user.getNick() + SPC + target + "%" + action);
        }
    }

    /**
     * @see IIrcBot#onChannelInfo(String, int, String)
     */
    @Override
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
     * @see IIrcBot#onFileTransferFinished(DccFileTransfer, Exception)
     */
    @Override
    public void onFileTransferFinished(final DccFileTransfer transfer, final Exception exception) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onIncomingChatRequest(DccChat)
     */
    @Override
    public void onIncomingChatRequest(final DccChat chat) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onIncomingFileTransfer(DccFileTransfer)
     */
    @Override
    public void onIncomingFileTransfer(final DccFileTransfer transfer) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onInvite(String, IrcUser, String)
     */
    @Override
    public void onInvite(final String targetNick, final IrcUser user, final String channel) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onJoin(Channel, IrcUser)
     * @param channel
     *            le channel
     * @param user
     *            le user
     */
    @Override
    public final void onJoin(final Channel channel, final IrcUser user) {
        for (final ITrigger trigger : AbstractTrigger.getTriggers()) {
            if (!(trigger instanceof IJoinTrigger)) {
                continue;
            }
            ((IJoinTrigger) trigger).executeJoinTrigger(channel, user);
        }
    }

    /**
     * @see IIrcBot#onMessage(Channel, IrcUser, String)
     * @param channel
     *            le channel
     */
    @Override
    public final void onMessage(final Channel channel, final IrcUser user, final String message) {
        if (LOG.isInfoEnabled()) {
            LOG.info(user.getNick() + channel + ":" + message);
        }
        for (final ITrigger trigger : AbstractTrigger.getTriggers()) {
            if (!(trigger instanceof IPublicTrigger)) {
                continue;
            }
            ((IPublicTrigger) trigger).executePublicTrigger(user, channel, message);
        }
    }

    /**
     * @see IIrcBot#onMode(Channel, IrcUser, String)
     */
    @Override
    public void onMode(final Channel channel, final IrcUser user, final String mode) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onNickChange(IrcUser, String)
     */
    @Override
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
    @Override
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
            ((INoticeTrigger) trigger).executeNoticeTrigger(user, notice);
        }
    }

    /**
     * @see AbstractIrcBot#onPrivateMessage(IrcUser, String)
     */
    @Override
    public void onPrivateMessage(final IrcUser user, final String message) {
        if (LOG.isInfoEnabled()) {
            LOG.info(user + ":" + message);
        }
        for (final ITrigger trigger : AbstractTrigger.getTriggers()) {
            if (!(trigger instanceof IPriveTrigger)) {
                continue;
            }
            ((IPriveTrigger) trigger).executePrivateTrigger(user, message);
        }
    }

    /**
     * @see IIrcBot#onRemoveChannelBan(Channel, IrcUser, String)
     */
    @Override
    public void onRemoveChannelBan(final Channel channel, final IrcUser user, final String hostmask) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onRemoveModerated(Channel, IrcUser)
     */
    @Override
    public void onRemoveModerated(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onRemoveNoExternalMessages(Channel, IrcUser)
     */
    @Override
    public void onRemoveNoExternalMessages(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onRemovePrivate(Channel, IrcUser)
     */
    @Override
    public void onRemovePrivate(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onRemoveSecret(Channel, IrcUser)
     */
    @Override
    public void onRemoveSecret(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onServerResponse(int, java.lang.String)
     * @param code
     *            le code rfc
     * @param response
     *            le message du serveur
     */
    @Override
    public void onServerResponse(final int code, final String response) {
        StrTokenizer st;
        String nick;
        Whois localWhois;
        switch (code) {
            case RPL_WHOISUSER:
                /* rien */
                break;
            case RPL_WHOISCHANNELS:
                /* osef */
                break;
            case RPL_WHOISSERVER:
                /* osef */
                break;
            case RPL_WHOISAUTH:
                st = new StrTokenizer(response, SPC);
                st.nextToken();
                nick = st.nextToken();
                localWhois = this.whois.get(nick);
                localWhois.setQnetAuth(st.nextToken());
                break;
            case RPL_WHOISIDLE:
                /* osef */
                break;
            case RPL_ENDOFWHOIS:
                st = new StrTokenizer(response, SPC);
                st.nextToken();
                nick = st.nextToken();
                localWhois = this.whois.get(nick);
                localWhois.setDone(true);
                /* le thread va s'arreter tout seul tfacon */
                this.whois.remove(nick);
                break;
            default:
                break;
        }
    }

    /**
     * @see IIrcBot#onSetChannelBan(Channel, IrcUser, String)
     */
    @Override
    public void onSetChannelBan(final Channel channel, final IrcUser user, final String hostmask) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onSetModerated(Channel, IrcUser)
     */
    @Override
    public void onSetModerated(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onSetNoExternalMessages(Channel, IrcUser)
     */
    @Override
    public void onSetNoExternalMessages(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onSetPrivate(Channel, IrcUser)
     */
    @Override
    public void onSetPrivate(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onSetSecret(Channel, IrcUser)
     */
    @Override
    public void onSetSecret(final Channel channel, final IrcUser user) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onUnknown(String)
     */
    @Override
    public void onUnknown(final String line) {
        // TODO Auto-generated method stub
    }

    /**
     * @see IIrcBot#onUserList(Channel, Iterable)
     * @param channel
     *            le channel
     * @param users
     *            les users
     */
    @Override
    public final void onUserList(final Channel channel, final Iterable < Entry < String, String >> users) {
        for (final Entry < String, String > user : users) {
            final IrcUser iu = IrcUser.getUser(user.getKey());
            if (null == channel) {
                return;
            }
            if (AUTOJOIN.contains(channel.getNom().toLowerCase(Locale.US))) {
                new Whois(iu).execute();
            }
            channel.addUser(iu, user.getValue());
        }
    }

    /**
     * @see IIrcBot#onUserMode(String, IrcUser, String)
     */
    @Override
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
