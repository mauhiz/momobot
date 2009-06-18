package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;

/**
 * @author mauhiz
 */
public class Quit extends AbstractIrcMessage {
    String message;
    
    /**
     * @param ircServer
     * @param msg
     */
    public Quit(IrcServer ircServer, String msg) {
        this(null, null, ircServer, msg);
    }
    
    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param msg1
     */
    public Quit(String from1, String to1, IrcServer ircServer, String msg1) {
        super(from1, to1, ircServer);
        message = msg1;
    }
    
    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("QUIT ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        sb.append(':');
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
        IrcUser quitter = server.findUser(new Mask(from), false);
        if (quitter != null) {
            for (IrcChannel every : server.getChannels()) {
                every.remove(quitter);
            }
            server.remove(quitter);
        }
    }
}
