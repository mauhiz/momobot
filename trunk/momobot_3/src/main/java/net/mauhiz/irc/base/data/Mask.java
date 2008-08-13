package net.mauhiz.irc.base.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.NullArgumentException;

/**
 * @author mauhiz
 */
public class Mask {
    private static final Pattern HOSTMASK = Pattern.compile("(.*)!(.*)@(.*)");
    private final String host;
    private final String nick;
    private final String raw;
    private final String user;
    
    /**
     * @param raw1
     */
    public Mask(final String raw1) {
        super();
        if (raw1 == null) {
            throw new NullArgumentException("raw");
        }
        raw = raw1;
        Matcher m = HOSTMASK.matcher(raw);
        if (m.matches()) {
            nick = m.group(1);
            user = m.group(2);
            host = m.group(3);
        } else {
            throw new IllegalArgumentException("Invalid mask: " + raw);
        }
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Mask)) {
            return false;
        }
        return raw.equals(((Mask) obj).raw);
    }
    
    /**
     * @return {@link #nick}
     */
    public String getNick() {
        return nick;
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return raw.hashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return raw;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }
}
