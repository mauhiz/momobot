package net.mauhiz.irc.base.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mauhiz
 */
public class Mask {
    static final Pattern HOSTMASK = Pattern.compile("(.*)!(.*)@(.*)");
    String host;
    String nick;
    String raw;
    String user;
    
    public Mask(final String raw1) {
        super();
        raw = raw1;
        buildMask();
    }
    
    /**
     * 
     */
    void buildMask() {
        Matcher m = HOSTMASK.matcher(raw);
        if (m.matches()) {
            nick = m.group(1);
            user = m.group(2);
            host = m.group(3);
        } else {
            throw new IllegalStateException("Invalid raw");
        }
    }
    
    /**
     * @return {@link #nick}
     */
    public String getNick() {
        return nick;
    }
}
