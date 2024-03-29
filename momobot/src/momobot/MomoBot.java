package momobot;

import ircbot.AIrcBot;
import ircbot.ATrigger;
import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.QnetUser;
import ircbot.Topic;
import ircbot.dcc.DccChat;
import ircbot.dcc.DccFileTransfer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import momobot.event.channel.Pendu;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.log4j.Logger;

/**
 * Cette classe suit le design pattern Factory.
 * @author Administrator
 */
public class MomoBot extends AIrcBot {
    /**
     * logger.
     */
    private static final Logger LOG      = Logger.getLogger(MomoBot.class);
    /**
     * r�f�rence au bot.
     */
    private static MomoBot      instance = null;

    /**
     * @return une instance de momobot
     */
    public static MomoBot getInstance() {
        if (instance == null) {
            instance = new MomoBot();
        }
        return instance;
    }
    /**
     * la liste des channels � autojoin.
     */
    private final Collection < String > autojoin = new TreeSet < String >();
    /**
     * la liste des whois en cours.
     */
    private final Map < String, Whois > whois    = new ConcurrentHashMap < String, Whois >();

    /**
     * @param channel
     *            le channel � autojoin
     */
    public final void addToAutoJoin(final String channel) {
        this.autojoin.add(channel.toLowerCase());
    }

    /**
     * @param nick
     *            le nom � whoiser
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
     * @see ircbot.AIrcBot#onAction(ircbot.IrcUser, java.lang.String,
     *      java.lang.String)
     * @param action
     *            l'action
     */
    @Override
    protected final void onAction(final IrcUser user, final String target,
            final String action) {
        if (LOG.isInfoEnabled()) {
            LOG.info(user.getNick() + " " + target + "%" + action);
        }
    }

    /**
     * @see ircbot.AIrcBot#onChannelInfo(java.lang.String, int,
     *      java.lang.String)
     * @param userCount
     *            le nbre de users
     * @param channel
     *            le channel
     */
    @Override
    protected void onChannelInfo(final String channel, final int userCount,
            final String topic) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onConnect()
     */
    @Override
    protected final void onConnect() {
        for (final String string : this.autojoin) {
            joinChannel(string);
        }
    }

    /**
     * @see ircbot.AIrcBot#onFileTransferFinished(ircbot.dcc.DccFileTransfer,
     *      java.lang.Exception)
     * @param e
     *            exceptionnel!
     * @param transfer
     *            le transfert
     */
    @Override
    public void onFileTransferFinished(final DccFileTransfer transfer,
            final Exception e) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onIncomingChatRequest(ircbot.dcc.DccChat)
     * @param chat
     *            chat
     */
    @Override
    public void onIncomingChatRequest(final DccChat chat) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onIncomingFileTransfer(ircbot.dcc.DccFileTransfer)
     */
    @Override
    public void onIncomingFileTransfer(final DccFileTransfer transfer) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onInvite(java.lang.String, ircbot.IrcUser,
     *      java.lang.String)
     * @param channel
     *            le channel
     */
    @Override
    protected final void onInvite(final String targetNick, final IrcUser user,
            final String channel) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onJoin(Channel, IrcUser)
     * @param channel
     *            le channel
     * @param user
     *            le user
     */
    @Override
    protected final void onJoin(final Channel channel, final IrcUser user) {
        /* j'�vite de flooder en joinant clanwar */
        if (this.autojoin.contains(channel)) {
            new Whois((QnetUser) user).execute();
        }
    }

    /**
     * @see ircbot.AIrcBot#onMessage(Channel, IrcUser, String)
     * @param channel
     *            le channel
     */
    @Override
    protected final void onMessage(final Channel channel, final IrcUser user,
            final String message) {
        if (LOG.isInfoEnabled()) {
            LOG.info(user.getNick() + channel + ":" + message);
        }
        for (ATrigger t : ATrigger.getTriggers()) {
            if (t.testPublic(message)) {
                t.executePublicTrigger(user, channel.getNom(), message);
                return;
            }
        }
        if (!channel.hasRunningEvent()) {
            return;
        }
        /* le pendu */
        if (channel.getEvent() instanceof Pendu) {
            final Pendu p = (Pendu) channel.getEvent();
            if (message.length() == 1) {
                sendMessage(channel.getNom(), p.submitLettre(message
                        .toLowerCase().charAt(0)));
                return;
            }
            final String msg = p.submitMot(message);
            if (msg.length() > 0) {
                sendMessage(channel.getNom(), msg);
            }
        }
    }

    /**
     * @see ircbot.AIrcBot#onMode(Channel, ircbot.IrcUser, java.lang.String)
     * @param channel
     *            le channel
     * @param user
     *            le user
     * @param mode
     *            le mode
     */
    @Override
    protected final void onMode(final Channel channel, final IrcUser user,
            final String mode) {
        //
    }

    /**
     * @see ircbot.AIrcBot#onNickChange(ircbot.IrcUser, java.lang.String)
     * @param user
     *            le user
     * @param newNick
     *            son nouveau nick
     */
    @Override
    protected final void onNickChange(final IrcUser user, final String newNick) {
        // rien
    }

    /**
     * @see ircbot.AIrcBot#onNotice(ircbot.IrcUser, java.lang.String,
     *      java.lang.String)
     * @param user
     *            le user
     * @param target
     *            les destinataires
     * @param notice
     *            le message
     */
    @Override
    protected final void onNotice(final IrcUser user, final String target,
            final String notice) {
        if (LOG.isInfoEnabled()) {
            LOG.info(getClass(), user.getNick() + "&" + notice);
        }
        // for (ATrigger t : ATrigger.getTriggers()) {
        // if (t.testNotice(notice)) {
        // if (t.isOnlyAdmin()) {
        // if (!user.isAdmin()) {
        // return;
        // }
        // }
        // /* t.executeNoticeTrigger(user, notice); */
        // return;
        // }
        // }
    }

    /**
     * @see ircbot.AIrcBot#onOp(Channel, ircbot.IrcUser, java.lang.String)
     * @param channel
     *            le channel
     * @param user
     *            le user
     * @param recipient
     *            l'heureux �lu
     */
    @Override
    protected final void onOp(final Channel channel, final IrcUser user,
            final String recipient) {
        channel.giveOp(user.getNick());
    }

    /**
     * @see ircbot.AIrcBot#onPart(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected final void onPart(final Channel channel, final IrcUser user) {
        channel.removeUser(user.getNick());
        if (user.getNick().equalsIgnoreCase(getNick())) {
            Channel.removeChannel(channel.getNom());
        }
    }

    /**
     * @see ircbot.AIrcBot#onPrivateMessage(ircbot.IrcUser, java.lang.String)
     */
    @Override
    protected void onPrivateMessage(final IrcUser user, final String message) {
        if (LOG.isInfoEnabled()) {
            LOG.info(user.getNick() + ":" + message);
        }
        for (ATrigger t : ATrigger.getTriggers()) {
            if (t.testPrive(message)) {
                if (t.isOnlyAdmin()) {
                    if (!user.isAdmin()) {
                        return;
                    }
                }
                t.executePrivateTrigger(user, message);
                return;
            }
        }
    }

    /**
     * @see ircbot.AIrcBot#onQuit(ircbot.IrcUser, java.lang.String)
     */
    @Override
    protected void onQuit(final IrcUser user, final String reason) {
        IrcUser.removeUser(user.getNick());
        for (Channel c : Channel.getAll()) {
            c.removeUser(user.getNick());
        }
    }

    /**
     * @see ircbot.AIrcBot#onRemoveChannelBan(Channel, ircbot.IrcUser,
     *      java.lang.String)
     * @param channel
     *            le channel
     */
    @Override
    protected void onRemoveChannelBan(final Channel channel,
            final IrcUser user, final String hostmask) {
        /* rien pour l'instant. */
    }

    /**
     * @see ircbot.AIrcBot#onRemoveChannelKey(Channel, ircbot.IrcUser,
     *      java.lang.String)
     * @param channel
     *            le channel
     */
    @Override
    protected void onRemoveChannelKey(final Channel channel,
            final IrcUser user, final String key) {
        channel.unSetKey();
    }

    /**
     * @see ircbot.AIrcBot#onRemoveChannelLimit(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onRemoveChannelLimit(final Channel channel,
            final IrcUser user) {
        //
    }

    /**
     * @see ircbot.AIrcBot#onRemoveInviteOnly(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onRemoveInviteOnly(final Channel channel, final IrcUser user) {
        channel.setInviteOnly(false);
    }

    /**
     * @see ircbot.AIrcBot#onRemoveModerated(Channel, IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onRemoveModerated(final Channel channel, final IrcUser user) {
        // rien pour l'instant.
    }

    /**
     * @see ircbot.AIrcBot#onRemoveNoExternalMessages(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onRemoveNoExternalMessages(final Channel channel,
            final IrcUser user) {
        /* rien pour l'instant. */
    }

    /**
     * @see ircbot.AIrcBot#onRemovePrivate(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onRemovePrivate(final Channel channel, final IrcUser user) {
        /* rien pour l'instant. */
    }

    /**
     * @see ircbot.AIrcBot#onRemoveSecret(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onRemoveSecret(final Channel channel, final IrcUser user) {
        /* rien pour l'instant. */
    }

    /**
     * @see ircbot.AIrcBot#onRemoveTopicProtection(ircbot.Channel,
     *      ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onRemoveTopicProtection(final Channel channel,
            final IrcUser user) {
        channel.getTopic().setTopicProtected(false);
    }

    /**
     * @see ircbot.AIrcBot#onServerResponse(int, java.lang.String)
     * @param code
     *            le code rfc
     * @param response
     *            le message du serveur
     */
    @Override
    protected void onServerResponse(final int code, final String response) {
        StrTokenizer st;
        String nick;
        Whois w;
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
                w = this.whois.get(nick);
                w.setQnetAuth(st.nextToken());
                break;
            case RPL_WHOISIDLE:
                /* osef */
                break;
            case RPL_ENDOFWHOIS:
                st = new StrTokenizer(response, SPC);
                st.nextToken();
                nick = st.nextToken();
                w = this.whois.get(nick);
                w.setDone(true);
                /* le thread va s'arreter tout seul tfacon */
                this.whois.remove(nick);
                break;
            default:
                break;
        }
    }

    /**
     * @see ircbot.AIrcBot#onSetChannelBan(Channel, ircbot.IrcUser,
     *      java.lang.String)
     * @param channel
     *            le channel
     */
    @Override
    protected void onSetChannelBan(final Channel channel, final IrcUser user,
            final String hostmask) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onSetChannelKey(Channel, ircbot.IrcUser,
     *      java.lang.String)
     * @param channel
     *            le channel
     */
    @Override
    protected void onSetChannelKey(final Channel channel, final IrcUser user,
            final String key) {
        channel.setKey(key);
    }

    /**
     * @see ircbot.AIrcBot#onSetChannelLimit(Channel, ircbot.IrcUser, int)
     * @param channel
     *            le channel
     */
    @Override
    protected void onSetChannelLimit(final Channel channel, final IrcUser user,
            final int limit) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onSetInviteOnly(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onSetInviteOnly(final Channel channel, final IrcUser user) {
        channel.setInviteOnly(true);
    }

    /**
     * @see ircbot.AIrcBot#onSetModerated(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onSetModerated(final Channel channel, final IrcUser user) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onSetNoExternalMessages(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onSetNoExternalMessages(final Channel channel,
            final IrcUser user) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onSetPrivate(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onSetPrivate(final Channel channel, final IrcUser user) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onSetSecret(Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     */
    @Override
    protected void onSetSecret(final Channel channel, final IrcUser user) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onSetTopicProtection(ircbot.Channel, ircbot.IrcUser)
     * @param channel
     *            le channel
     * @param user
     *            le user
     */
    @Override
    protected final void onSetTopicProtection(final Channel channel,
            final IrcUser user) {
        channel.getTopic().setTopicProtected(true);
    }

    /**
     * @see ircbot.AIrcBot#onTopic(java.lang.String, java.lang.String,
     *      java.lang.String, long, boolean)
     * @param changed
     *            la derni�re fois qu'il a chang�.
     * @param channel
     *            le channel
     */
    @Override
    protected final void onTopic(final String channel, final String topic,
            final String setBy, final long date, final boolean changed) {
        final Topic t = new Topic(topic, date, setBy);
        Channel.getChannel(channel).setTopic(t);
    }

    /**
     * @see ircbot.AIrcBot#onUnknown(java.lang.String)
     * @param line
     *            la ligne inconnue
     */
    @Override
    protected void onUnknown(final String line) {
        //
    }

    /**
     * @see ircbot.AIrcBot#onUserList(java.lang.String, java.util.Iterator)
     * @param channel
     *            le channel
     * @param users
     *            les users
     */
    @Override
    protected final void onUserList(final String channel,
            final Iterator < Map.Entry < String, String >> users) {
        final Channel c = Channel.getChannel(channel);
        while (users.hasNext()) {
            final Map.Entry < String, String > user = users.next();
            final IrcUser iu = IrcUser.getUser(user.getKey());
            if (this.autojoin.contains(channel)) {
                new Whois((QnetUser) iu).execute();
            }
            c.addUser(iu, user.getValue());
        }
    }

    /**
     * @see ircbot.AIrcBot#onUserMode(java.lang.String, ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    protected final void onUserMode(final String targetNick,
            final IrcUser user, final String mode) {
        /* rien */
    }

    /**
     * @see ircbot.AIrcBot#onVoice(Channel, ircbot.IrcUser, java.lang.String)
     * @param channel
     *            le channel
     */
    @Override
    protected final void onVoice(final Channel channel, final IrcUser user,
            final String recipient) {
        channel.giveVoice(recipient);
    }

    /**
     * @param user
     *            le user
     * @param target
     *            la cible
     */
    public final void sendNotice(final IrcUser user, final String target) {
        sendNotice(user.getNick(), target);
    }
}
