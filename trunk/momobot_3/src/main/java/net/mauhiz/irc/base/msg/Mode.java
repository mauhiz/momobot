package net.mauhiz.irc.base.msg;

import java.util.Set;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.data.UserChannelMode;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class Mode extends AbstractIrcMessage {
    private static final Logger LOG = Logger.getLogger(Mode.class);
    private final String message;
    
    /**
     * @param from1
     * @param to1
     * @param server1
     */
    public Mode(String from1, String to1, IrcServer server1) {
        this(from1, to1, server1, null);
    }
    
    /**
     * @param group
     * @param object
     * @param ircServer
     * @param group2
     */
    public Mode(String group, String object, IrcServer ircServer, String group2) {
        super(group, object, ircServer);
        message = group2;
    }
    
    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("MODE ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        sb.append(message);
        return sb.toString();
    }
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    @Override
    public void process(IIrcControl control) {
        Mask fromMask = new Mask(from);
        IrcUser by = fromMask.getNick() == null ? null : server.findUser(fromMask, true);
        
        String[] toks = StringUtils.split(message, ' ');
        String modeInfo = toks[0];
        
        char modifier = modeInfo.charAt(0);
        String modes = modeInfo.substring(1);
        
        if (isToChannel()) {
            IrcChannel chan = server.findChannel(to);
            if (toks.length == 1) {
                // pure channel mode
                chan.getProperties().process(modifier, modes);
            } else {
                // setting mode on people
                String target = toks[1];
                
                if (toks.length > 2) {
                    LOG.warn("TODO handle multiple users : " + ArrayUtils.toString(toks));
                }
                
                IrcUser targetUser = server.findUser(target, false);
                
                if (target == null) {
                    return; // let's skip this unknown user
                }
                Set<UserChannelMode> userModes = chan.getModes(targetUser);
                
                if (modes.length() > 1) {
                    LOG.warn("TODO handle multiple modes : " + modes);
                }
                
                UserChannelMode newMode = UserChannelMode.fromCmd(modes.charAt(0));
                
                if (modifier == '+') {
                    userModes.add(newMode);
                    
                } else {
                    userModes.remove(newMode);
                }
                
                LOG.debug("userModes : " + userModes);
            }
        } else {
            // TODO
        }
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "* " + from + " sets mode: " + message + " " + to;
    }
}
