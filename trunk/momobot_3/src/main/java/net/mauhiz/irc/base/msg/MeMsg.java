package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class MeMsg extends IrcMessage {
    
    String message;
    
    /**
     * TODO constr private
     * 
     * @param from1
     * @param to1
     * @param server1
     * @param message1
     */
    public MeMsg(final String from1, final String to1, final IrcServer server1, final String message1) {
        super(from1, to1, server1);
        message = message1;
    }
    
    /**
     * @return le message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (StringUtils.isEmpty(message)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("PRIVMSG ");
        if (super.to != null) {
            
            sb.append(super.to);
            
            sb.append(' ');
        }
        
        sb.append(':');
        sb.append((char) 01 + "ACTION ");
        sb.append(message);
        sb.append((char) 01);
        return sb.toString();
    }
}
