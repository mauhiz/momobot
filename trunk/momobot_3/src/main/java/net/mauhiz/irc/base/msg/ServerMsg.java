package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.qnet.QnetUser;
import net.mauhiz.irc.base.model.WhoisRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ServerMsg extends AbstractIrcMessage implements NumericReplies {
    private static final Logger LOG = Logger.getLogger(ServerMsg.class);
    /**
     * code numerique
     */
    private final int code;
    /**
     * suite du message
     */
    private final String msg;
    
    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param codeStr
     * @param group2
     */
    public ServerMsg(String from1, String to1, IrcServer ircServer, String codeStr, String group2) {
        super(from1, to1, ircServer);
        code = Integer.parseInt(codeStr);
        msg = group2;
    }
    
    /**
     * @return {@link #code}
     */
    public int getCode() {
        return code;
    }
    
    @Override
    public String getIrcForm() {
        return code + " " + msg;
    }
    
    /**
     * @return {@link #msg}
     */
    public String getMsg() {
        return msg;
    }
    
    private void handleNamReply() {
        int sep = msg.indexOf(" :");
        String chanName = msg.substring(2, sep);
        IrcChannel chan = getServer().findChannel(chanName);
        if (chan == null) {
            return;
        }
        
        String[] prefixedNames = StringUtils.split(msg.substring(sep + 2));
        for (String prefixedName : prefixedNames) {
            char prefix = prefixedName.charAt(0);
            if (prefix == '+' || prefix == '@' || prefix == '%') {
                prefixedName = prefixedName.substring(1);
            }
            IrcUser next = getServer().findUser(prefixedName, true);
            chan.add(next);
        }
        LOG.info("Names Reply on " + chan + ": " + StringUtils.join(prefixedNames, ' '));
    }
    private void handleWhoisChannels() {
        String nick = StringUtils.substringBefore(msg, " ");
        IrcUser ircUser = server.findUser(nick, true);
        
        String lstChanNames = StringUtils.substringAfter(msg, " :");
        String[] chanNames = StringUtils.split(lstChanNames, " ");
        for (String chanName : chanNames) {
            if (!MomoStringUtils.isChannelName(chanName)) {
                chanName = chanName.substring(1);
            }
            IrcChannel channel = server.findChannel(chanName, false); // osef des infos qui sont pas sur mon chan
            if (channel != null) {
                channel.add(ircUser);
            }
        }
    }
    
    private void handleWhoisQnet() {
        String nick = StringUtils.substringBefore(msg, " ");
        QnetUser ircUser = (QnetUser) server.findUser(nick, true);
        
        String remaining = StringUtils.substringAfter(msg, " ");
        String auth = StringUtils.substringBefore(remaining, " :");
        ircUser.setAuth(auth);
    }
    private void handleWhoisUser() {
        String nick = StringUtils.substringBefore(msg, " ");
        IrcUser ircUser = server.findUser(nick, true);
        
        String remaining = StringUtils.substringAfter(msg, " ");
        String user = StringUtils.substringBefore(remaining, " ");
        remaining = StringUtils.substringAfter(remaining, " ");
        ircUser.setUser(user);
        
        String host = StringUtils.substringBefore(remaining, " ");
        ircUser.setHost(host);
        
        String fullName = StringUtils.substringAfter(remaining, " :");
        ircUser.setFullName(fullName);
    }
    
    /**
     * @see net.mauhiz.irc.base.msg.IIrcMessage#process(net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void process(IIrcControl control) {
        switch (code) {
            case RPL_UMODEIS :
                LOG.debug("my mode: " + msg);
                break;
            case RPL_TOPIC :
                LOG.debug("topic: " + msg);
                break;
            case RPL_TOPICINFO :
                LOG.debug("topic info: " + msg);
                break;
            case RPL_LUSERCLIENT :
                LOG.info("number of clients: " + msg);
                break;
            case RPL_LUSERCHANNELS :
                LOG.info("number of channels: " + msg);
                break;
            case RPL_LUSERME :
                LOG.info("server userme: " + msg);
                break;
            case RPL_MOTD :
                LOG.info("Motd LINE: " + msg);
                break;
            case RPL_NAMREPLY :
                handleNamReply();
                break;
            case RPL_ENDOFNAMES :
                LOG.debug("End of Names Reply");
                break;
            case RPL_LUSEROP :
                LOG.info("list of operators: " + msg);
                break;
            case RPL_MOTDSTART :
                LOG.debug("Start of MOTD: " + msg);
                break;
            case ERR_NOTEXTTOSEND :
                LOG.warn("Server told me that I tried to send an empty msg");
                break;
            case RPL_ENDOFMOTD :
                LOG.debug("End of MOTD");
                break;
            case RPL_LUSERUNKNOWN :
                LOG.info("list of unknown users: " + msg);
                break;
            case ERR_QNETSERVICEIMMUNE :
                LOG.warn("I cannot do harm to a service! " + msg);
                break;
            case RPL_WHOISUSER :
                handleWhoisUser();
                break;
            case ERR_NOSUCHNICK :
                String unkNick = StringUtils.substringBefore(msg, " :");
                WhoisRequest.end(unkNick, false);
                break;
            case RPL_WHOISCHANNELS :
                handleWhoisChannels();
                break;
            case RPL_WHOISSERVER :
                LOG.warn("TODO : whois server");
                break;
            case RPL_WHOISAUTH :
                handleWhoisQnet();
                break;
            case RPL_ENDOFWHOIS :
                String nick = StringUtils.substringBefore(msg, " ");
                WhoisRequest.end(nick, true);
                break;
            case ERR_CHANOPRIVSNEEDED :
                LOG.warn("I am not channel operator. " + StringUtils.substringBefore(msg, " "));
                break;
            case ERR_NOTREGISTERED :
                LOG.warn("I should register before sending commands!");
                break;
            case RPL_WHOISIDLE :
                LOG.warn("I have been idle : " + StringUtils.substringAfter(msg, " "));
                break;
            default :
                LOG.warn("Unhandled server reply : " + this);
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.msg.AbstractIrcMessage#toString()
     */
    @Override
    public String toString() {
        return "server msg: code=" + code + " msg=" + msg;
    }
}
