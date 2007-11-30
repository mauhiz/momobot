package ircbot.event;

import ircbot.Channel;
import ircbot.IrcUser;

import java.util.Map;

/**
 * @author mauhiz
 */
public interface IIrcEventHandler {
    /**
     * .
     */
    short ERR_ALREADYREGISTRED = 462;
    /**
     * .
     */
    short ERR_BADCHANMASK      = 476;
    /**
     * .
     */
    short ERR_BADCHANNELKEY    = 475;
    /**
     * .
     */
    short ERR_BANNEDFROMCHAN   = 474;
    /**
     * .
     */
    short ERR_CANNOTSENDTOCHAN = 404;
    /**
     * .
     */
    short ERR_CANTKILLSERVER   = 483;
    /**
     * .
     */
    short ERR_CHANNELISFULL    = 471;
    /**
     * .
     */
    short ERR_CHANOPRIVSNEEDED = 482;
    /**
     * .
     */
    short ERR_ERRONEUSNICKNAME = 432;
    /**
     * .
     */
    short ERR_FILEERROR        = 424;
    /**
     * .
     */
    short ERR_INVITEONLYCHAN   = 473;
    /**
     * .
     */
    short ERR_KEYSET           = 467;
    /**
     * .
     */
    short ERR_NEEDMOREPARAMS   = 461;
    /**
     * .
     */
    short ERR_NICKCOLLISION    = 436;
    /**
     * .
     */
    short ERR_NICKNAMEINUSE    = 433;
    /**
     * .
     */
    short ERR_NOADMININFO      = 423;
    /**
     * .
     */
    short ERR_NOLOGIN          = 444;
    /**
     * .
     */
    short ERR_NOMOTD           = 422;
    /**
     * .
     */
    short ERR_NONICKNAMEGIVEN  = 431;
    /**
     * .
     */
    short ERR_NOOPERHOST       = 491;
    /**
     * .
     */
    short ERR_NOORIGIN         = 409;
    /**
     * .
     */
    short ERR_NOPERMFORHOST    = 463;
    /**
     * .
     */
    short ERR_NOPRIVILEGES     = 481;
    /**
     * .
     */
    short ERR_NORECIPIENT      = 411;
    /**
     * .
     */
    short ERR_NOSERVICEHOST    = 492;
    /**
     * .
     */
    short ERR_NOSUCHCHANNEL    = 403;
    /**
     * .
     */
    short ERR_NOSUCHNICK       = 401;
    /**
     * .
     */
    short ERR_NOSUCHSERVER     = 402;
    /**
     * .
     */
    short ERR_NOTEXTTOSEND     = 412;
    /**
     * .
     */
    short ERR_NOTONCHANNEL     = 442;
    /**
     * .
     */
    short ERR_NOTOPLEVEL       = 413;
    /**
     * .
     */
    short ERR_NOTREGISTERED    = 451;
    /**
     * .
     */
    short ERR_PASSWDMISMATCH   = 464;
    /**
     * .
     */
    short ERR_SUMMONDISABLED   = 445;
    /**
     * .
     */
    short ERR_TOOMANYCHANNELS  = 405;
    /**
     * .
     */
    short ERR_TOOMANYTARGETS   = 407;
    /**
     * .
     */
    short ERR_UMODEUNKNOWNFLAG = 501;
    /**
     * .
     */
    short ERR_UNKNOWNCOMMAND   = 421;
    /**
     * .
     */
    short ERR_UNKNOWNMODE      = 472;
    /**
     * .
     */
    short ERR_USERNOTINCHANNEL = 441;
    /**
     * .
     */
    short ERR_USERONCHANNEL    = 443;
    /**
     * .
     */
    short ERR_USERSDISABLED    = 446;
    /**
     * .
     */
    short ERR_USERSDONTMATCH   = 502;
    /**
     * .
     */
    short ERR_WASNOSUCHNICK    = 406;
    /**
     * .
     */
    short ERR_WILDTOPLEVEL     = 414;
    /**
     * .
     */
    short ERR_YOUREBANNEDCREEP = 465;
    /**
     * .
     */
    short ERR_YOUWILLBEBANNED  = 466;
    /**
     * .
     */
    short RPL_ADMINEMAIL       = 259;
    /**
     * .
     */
    short RPL_ADMINLOC1        = 257;
    /**
     * .
     */
    short RPL_ADMINLOC2        = 258;
    /**
     * .
     */
    short RPL_ADMINME          = 256;
    /**
     * .
     */
    short RPL_AWAY             = 301;
    /**
     * .
     */
    short RPL_BANLIST          = 367;
    /**
     * .
     */
    short RPL_CHANNELMODEIS    = 324;
    /**
     * .
     */
    short RPL_CLOSEEND         = 363;
    /**
     * .
     */
    short RPL_CLOSING          = 362;
    /**
     * .
     */
    short RPL_ENDOFBANLIST     = 368;
    /**
     * .
     */
    short RPL_ENDOFINFO        = 374;
    /**
     * .
     */
    short RPL_ENDOFLINKS       = 365;
    /**
     * .
     */
    short RPL_ENDOFMOTD        = 376;
    /**
     * .
     */
    short RPL_ENDOFNAMES       = 366;
    /**
     * .
     */
    short RPL_ENDOFSERVICES    = 232;
    /**
     * .
     */
    short RPL_ENDOFSTATS       = 219;
    /**
     * .
     */
    short RPL_ENDOFUSERS       = 394;
    /**
     * .
     */
    short RPL_ENDOFWHO         = 315;
    /**
     * .
     */
    short RPL_ENDOFWHOIS       = 318;
    /**
     * .
     */
    short RPL_ENDOFWHOWAS      = 369;
    /**
     * .
     */
    short RPL_INFO             = 371;
    /**
     * .
     */
    short RPL_INFOSTART        = 373;
    /**
     * .
     */
    short RPL_INVITING         = 341;
    /**
     * .
     */
    short RPL_ISON             = 303;
    /**
     * .
     */
    short RPL_KILLDONE         = 361;
    /**
     * .
     */
    short RPL_LINKS            = 364;
    /**
     * .
     */
    short RPL_LIST             = 322;
    /**
     * .
     */
    short RPL_LISTEND          = 323;
    /**
     * .
     */
    short RPL_LISTSTART        = 321;
    /**
     * .
     */
    short RPL_LUSERCHANNELS    = 254;
    /**
     * .
     */
    short RPL_LUSERCLIENT      = 251;
    /**
     * .
     */
    short RPL_LUSERME          = 255;
    /**
     * .
     */
    short RPL_LUSEROP          = 252;
    /**
     * ..
     */
    short RPL_LUSERUNKNOWN     = 253;
    /**
     * ..
     */
    short RPL_MOTD             = 372;
    /**
     * ..
     */
    short RPL_MOTDSTART        = 375;
    /**
     * .
     */
    short RPL_MYPORTIS         = 384;
    /**
     * .
     */
    short RPL_NAMREPLY         = 353;
    /**
     * .
     */
    short RPL_NONE             = 300;
    /**
     * .
     */
    short RPL_NOTOPIC          = 331;
    /**
     * .
     */
    short RPL_NOUSERS          = 395;
    /**
     * .
     */
    short RPL_NOWAWAY          = 306;
    /**
     * .
     */
    short RPL_REHASHING        = 382;
    /**
     * .
     */
    short RPL_SERVICE          = 233;
    /**
     * .
     */
    short RPL_SERVICEINFO      = 231;
    /**
     * .
     */
    short RPL_SERVLIST         = 234;
    /**
     * .
     */
    short RPL_SERVLISTEND      = 235;
    /**
     * .
     */
    short RPL_STATSCLINE       = 213;
    /**
     * .
     */
    short RPL_STATSCOMMANDS    = 212;
    /**
     * .
     */
    short RPL_STATSHLINE       = 244;
    /**
     * .
     */
    short RPL_STATSILINE       = 215;
    /**
     * .
     */
    short RPL_STATSKLINE       = 216;
    /**
     * .
     */
    short RPL_STATSLINKINFO    = 211;
    /**
     * .
     */
    short RPL_STATSLLINE       = 241;
    /**
     * .
     */
    short RPL_STATSNLINE       = 214;
    /**
     * .
     */
    short RPL_STATSOLINE       = 243;
    /**
     * .
     */
    short RPL_STATSQLINE       = 217;
    /**
     * .
     */
    short RPL_STATSUPTIME      = 242;
    /**
     * .
     */
    short RPL_STATSYLINE       = 218;
    /**
     * .
     */
    short RPL_SUMMONING        = 342;
    /**
     * .
     */
    short RPL_TIME             = 391;
    /**
     * .
     */
    short RPL_TOPIC            = 332;
    /**
     * .
     */
    short RPL_TOPICINFO        = 333;
    /**
     * .
     */
    short RPL_TRACECLASS       = 209;
    /**
     * .
     */
    short RPL_TRACECONNECTING  = 201;
    /**
     * .
     */
    short RPL_TRACEHANDSHAKE   = 202;
    /**
     * .
     */
    short RPL_TRACELINK        = 200;
    /**
     * .
     */
    short RPL_TRACELOG         = 261;
    /**
     * .
     */
    short RPL_TRACENEWTYPE     = 208;
    /**
     * .
     */
    short RPL_TRACEOPERATOR    = 204;
    /**
     * .
     */
    short RPL_TRACESERVER      = 206;
    /**
     * .
     */
    short RPL_TRACEUNKNOWN     = 203;
    /**
     * .
     */
    short RPL_TRACEUSER        = 205;
    /**
     * .
     */
    short RPL_UMODEIS          = 221;
    /**
     * .
     */
    short RPL_UNAWAY           = 305;
    /**
     * .
     */
    short RPL_USERHOST         = 302;
    /**
     * .
     */
    short RPL_USERS            = 393;
    /**
     * .
     */
    short RPL_USERSSTART       = 392;
    /**
     * .
     */
    short RPL_VERSION          = 351;
    /**
     * .auth Qnet.
     */
    short RPL_WHOISAUTH        = 330;
    /**
     * .
     */
    short RPL_WHOISCHANNELS    = 319;
    /**
     * .
     */
    short RPL_WHOISCHANOP      = 316;
    /**
     * .
     */
    short RPL_WHOISIDLE        = 317;
    /**
     * .
     */
    short RPL_WHOISOPERATOR    = 313;
    /**
     * .
     */
    short RPL_WHOISSERVER      = 312;
    /**
     * .
     */
    short RPL_WHOISUSER        = 311;
    /**
     * .
     */
    short RPL_WHOREPLY         = 352;
    /**
     * .
     */
    short RPL_WHOWASUSER       = 314;
    /**
     * .
     */
    short RPL_YOUREOPER        = 381;

    /**
     * This method is called whenever an ACTION is sent from a user. E.g. such events generated by typing "/me goes
     * shopping" in most IRC clients.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param sender
     *            The nick of the user that sent the action.
     * @param target
     *            The target of the action, be it a channel or our nick.
     * @param action
     *            The action carried out by the user.
     */
    void onAction(IrcUser sender, String target, String action);

    /**
     * After calling the listChannels() method in PircBot, the server will start to send us information about each
     * channel on the server. You may override this method in order to receive the information about each channel as
     * soon as it is received.
     * <p>
     * Note that certain channels, such as those marked as hidden, may not appear in channel listings.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param channel
     *            The name of the channel.
     * @param userCount
     *            The number of users visible in this channel.
     * @param topic
     *            The topic for this channel.
     */
    void onChannelInfo(String channel, int userCount, String topic);

    /**
     * This method is called once the PircBot has successfully connected to the IRC server.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.6
     */
    void onConnect();

    /**
     * Called when a user (possibly us) gets operator status taken away.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param recipient
     *            The nick of the user that got 'deopped'.
     */
    void onDeop(Channel channel, IrcUser user, String recipient);

    /**
     * Called when a user (possibly us) gets voice status removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param recipient
     *            The nick of the user that got 'devoiced'.
     */
    void onDeVoice(Channel channel, IrcUser user, String recipient);

    /**
     * This method carries out the actions to be performed when the PircBot gets disconnected. This may happen if the
     * PircBot quits from the server, or if the connection is unexpectedly lost.
     * <p>
     * Disconnection from the IRC server is detected immediately if either we or the server close the connection
     * normally. If the connection to the server is lost, but neither we nor the server have explicitly closed the
     * connection, then it may take a few minutes to detect (this is commonly referred to as a "ping timeout").
     * <p>
     * If you wish to get your IRC bot to automatically rejoin a server after the connection has been lost, then this is
     * probably the ideal method to override to implement such functionality.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     */
    void onDisconnect();

    /**
     * This method is called whenever we receive a FINGER request.
     * <p>
     * This abstract implementation responds correctly, so if you override this method, be sure to either mimic its
     * functionality or to call super.onFinger(...);
     * 
     * @param user
     *            le user
     * @param target
     *            The target of the FINGER request, be it our nick or a channel name.
     */
    void onFinger(final IrcUser user, final String target);

    /**
     * Called when we are invited to a channel by a user.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param targetNick
     *            The nick of the user being invited - should be us!
     * @param user
     *            le user
     * @param channel
     *            The channel that we're being invited to.
     */
    void onInvite(String targetNick, IrcUser user, String channel);

    /**
     * This method is called whenever someone (possibly us) joins a channel which we are on.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param channel
     *            The channel which somebody joined.
     * @param user
     *            le user
     */
    void onJoin(Channel channel, IrcUser user);

    /**
     * This method is called whenever someone (possibly us) is kicked from any of the channels that we are in.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param channel
     *            The channel from which the recipient was kicked.
     * @param user
     *            le user
     * @param recipientNick
     *            The unfortunate recipient of the kick.
     * @param reason
     *            The reason given by the user who performed the kick.
     */
    void onKick(Channel channel, IrcUser user, String recipientNick, String reason);

    /**
     * This method is called whenever a message is sent to a channel.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required. /**
     * 
     * @param cme
     *            the event received
     */
    void onMessage(ChannelMessageEvent cme);

    /**
     * Called when the mode of a channel is set.
     * <p>
     * You may find it more convenient to decode the meaning of the mode string by overriding the onOp, onDeOp, onVoice,
     * onDeVoice, onChannelKey, onDeChannelKey, onChannelLimit, onDeChannelLimit, onChannelBan or onDeChannelBan methods
     * as appropriate.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param channel
     *            The channel that the mode operation applies to.
     * @param user
     *            le user
     * @param mode
     *            The mode that has been set.
     */
    void onMode(Channel channel, IrcUser user, String mode);

    /**
     * @param user
     * @param sender
     * @param mode
     */
    void onMode(IrcUser user, IrcUser sender, String mode);

    /**
     * This method is called whenever someone (possibly us) changes nick on any of the channels that we are on.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param user
     *            le user
     * @param newNick
     *            The new nick.
     */
    void onNickChange(IrcUser user, String newNick);

    /**
     * This method is called whenever we receive a notice.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param user
     *            le user
     * @param target
     *            The target of the notice, be it our nick or a channel name.
     * @param notice
     *            The notice message.
     */
    void onNotice(IrcUser user, String target, String notice);

    /**
     * Called when a user (possibly us) gets granted operator status for a channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param recipient
     *            The nick of the user that got 'opped'.
     */
    void onOp(Channel channel, IrcUser user, String recipient);

    /**
     * This method is called whenever someone (possibly us) parts a channel which we are on.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param channel
     *            The channel which somebody parted from.
     * @param user
     *            le user
     */
    void onPart(Channel channel, IrcUser user);

    /**
     * This method is called whenever we receive a PING request from another user.
     * <p>
     * This abstract implementation responds correctly, so if you override this method, be sure to either mimic its
     * functionality or to call super.onPing(...);
     * 
     * @param user
     *            le user
     * @param target
     *            The target of the PING request, be it our nick or a channel name.
     * @param pingValue
     *            The value that was supplied as an argument to the PING command.
     */
    void onPing(final IrcUser user, final String target, final String pingValue);

    /**
     * This method is called whenever a private message is sent to the PircBot.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required. /**
     * 
     * @param pme
     */
    void onPrivateMessage(final PrivateMessageEvent pme);

    /**
     * This method is called whenever someone (possibly us) quits from the server. We will only observe this if the user
     * was in one of the channels to which we are connected.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param user
     *            le user
     * @param reason
     *            The reason given for quitting the server.
     */
    void onQuit(IrcUser user, String reason);

    /**
     * Called when a hostmask ban is removed from a channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param hostmask
     *            le masque de ban
     */
    void onRemoveChannelBan(Channel channel, IrcUser user, String hostmask);

    /**
     * Called when a channel key is removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param key
     *            The key that was in use before the channel key was removed.
     */
    void onRemoveChannelKey(Channel channel, IrcUser user, String key);

    /**
     * Called when the user limit is removed for a channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onRemoveChannelLimit(Channel channel, IrcUser user);

    /**
     * Called when a channel has 'invite only' removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onRemoveInviteOnly(Channel channel, IrcUser user);

    /**
     * Called when a channel has moderated mode removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onRemoveModerated(Channel channel, IrcUser user);

    /**
     * Called when a channel is set to allow messages from any user, even if they are not actually in the channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onRemoveNoExternalMessages(Channel channel, IrcUser user);

    /**
     * Called when a channel is marked as not being in private mode.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onRemovePrivate(Channel channel, IrcUser user);

    /**
     * Called when a channel has 'secret' mode removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onRemoveSecret(Channel channel, IrcUser user);

    /**
     * Called when topic protection is removed for a channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onRemoveTopicProtection(Channel channel, IrcUser user);

    /**
     * The actions to perform when a PING request comes from the server.
     * <p>
     * This sends back a correct response, so if you override this method, be sure to either mimic its functionality or
     * to call super.onServerPing(response);
     * 
     * @param response
     *            The response that should be given back in your PONG.
     */
    void onServerPing(final String response);

    /**
     * This method is called when we receive a numeric response from the IRC server.
     * <p>
     * Numerics in the range from 001 to 099 are used for client-server connections only and should never travel between
     * servers. Replies generated in response to commands are found in the range from 200 to 399. Error replies are
     * found in the range from 400 to 599.
     * <p>
     * For example, we can use this method to discover the topic of a channel when we join it. If we join the channel
     * #test which has a topic of 'I am King of Test' then the response will be '<code>PircBot #test :I Am King of Test</code>'
     * with a code of 332 to signify that this is a topic. (This is just an example - note that overriding the
     * <code>onTopic</code> method is an easier way of finding the topic for a channel). Check the IRC RFC for the
     * full list of other command response codes.
     * <p>
     * PircBot implements the interface ReplyConstants, which contains contstants that you may find useful here.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param code
     *            The three-digit numerical code for the response.
     * @param response
     *            The full response from the IRC server.
     */
    void onServerResponse(final int code, final String response);

    /**
     * Called when a user (possibly us) gets banned from a channel. Being banned from a channel prevents any user with a
     * matching hostmask from joining the channel. For this reason, most bans are usually directly followed by the user
     * being kicked :-)
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param hostmask
     *            The hostmask of the user that has been banned.
     */
    void onSetChannelBan(Channel channel, IrcUser user, String hostmask);

    /**
     * Called when a channel key is set. When the channel key has been set, other users may only join that channel if
     * they know the key. Channel keys are sometimes referred to as passwords.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param key
     *            The new key for the channel.
     */
    void onSetChannelKey(Channel channel, IrcUser user, String key);

    /**
     * Called when a user limit is set for a channel. The number of users in the channel cannot exceed this limit.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param limit
     *            The maximum number of users that may be in this channel at the same time.
     */
    void onSetChannelLimit(Channel channel, IrcUser user, int limit);

    /**
     * Called when a channel is set to 'invite only' mode. A user may only join the channel if they are invited by
     * someone who is already in the channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onSetInviteOnly(Channel channel, IrcUser user);

    /**
     * Called when a channel is set to 'moderated' mode. If a channel is moderated, then only users who have been
     * 'voiced' or 'opped' may speak or change their nicks.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onSetModerated(Channel channel, IrcUser user);

    /**
     * Called when a channel is set to only allow messages from users that are in the channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onSetNoExternalMessages(Channel channel, IrcUser user);

    /**
     * Called when a channel is marked as being in private mode.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onSetPrivate(Channel channel, IrcUser user);

    /**
     * Called when a channel is set to be in 'secret' mode. Such channels typically do not appear on a server's channel
     * listing.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onSetSecret(Channel channel, IrcUser user);

    /**
     * Called when topic protection is enabled for a channel. Topic protection means that only operators in a channel
     * may change the topic.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    void onSetTopicProtection(Channel channel, IrcUser user);

    /**
     * This method is called whenever we receive a TIME request.
     * <p>
     * This abstract implementation responds correctly, so if you override this method, be sure to either mimic its
     * functionality or to call super.onTime(...);
     * 
     * @param target
     *            The target of the TIME request, be it our nick or a channel name.
     * @param user
     *            le user
     */
    void onTime(final IrcUser user, final String target);

    /**
     * This method is called whenever a user sets the topic, or when PircBot joins a new channel and discovers its
     * topic.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param channel
     *            The channel that the topic belongs to.
     * @param topic
     *            The topic for the channel.
     * @param setBy
     *            The nick of the user that set the topic.
     * @param date
     *            When the topic was set (milliseconds since the epoch).
     * @param changed
     *            True if the topic has just been changed, false if the topic was already there.
     */
    void onTopic(String channel, String topic, String setBy, long date, boolean changed);

    /**
     * This method is called whenever we receive a line from the server that the PircBot has not been programmed to
     * recognise.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param line
     *            The raw line that was received from the server.
     */
    void onUnknown(String line);

    /**
     * This method is called when we receive a user list from the server after joining a channel.
     * <p>
     * Shortly after joining a channel, the IRC server sends a list of all users in that channel. The PircBot collects
     * this information and calls this method as soon as it has the full list.
     * <p>
     * To obtain the nick of each user in the channel, call the getNick() method on each User object in the array.
     * <p>
     * At a later time, you may call the getUsers method to obtain an up to date list of the users in the channel.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param channel
     *            The name of the channel.
     * @param users
     *            An array of User objects belonging to this channel.
     */
    void onUserList(Channel channel, Map < String, String > users);

    /**
     * Called when the mode of a user is set.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @param targetUser
     *            The nick that the mode operation applies to.
     * @param user
     *            le user
     * @param mode
     *            The mode that has been set.
     */
    void onUserMode(final IrcUser targetUser, final IrcUser user, final String mode);

    /**
     * This method is called whenever we receive a VERSION request. This abstract implementation responds with the
     * PircBot's _version string, so if you override this method, be sure to either mimic its functionality or to call
     * super.onVersion(...);
     * 
     * @param user
     *            celui qui m'a versionné
     * @param target
     *            The target of the VERSION request, be it our nick or a channel name.
     */
    void onVersion(final IrcUser user, final String target);

    /**
     * Called when a user (possibly us) gets voice status granted in a channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs no actions and may be overridden as
     * required.
     * 
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param recipient
     *            The nick of the user that got 'voiced'.
     */
    void onVoice(Channel channel, IrcUser user, String recipient);
}
