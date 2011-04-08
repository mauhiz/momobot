package net.mauhiz.irc.base.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mauhiz
 */
public class Mask {
    private static final Pattern HOSTMASK = Pattern.compile("(.*)!(.*)@(.*)");
    private String host;
    private String nick;
    private final String raw;
    private String user;

    /**
     * @param raw
     */
    public Mask(String raw) {
        super();
        assert raw != null;
        this.raw = raw;
        Matcher m = HOSTMASK.matcher(raw);
        if (m.matches()) {
            nick = m.group(1);
            user = m.group(2);
            host = m.group(3);
        } else {
            host = raw;
        }
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Mask)) {
            return false;
        }
        return raw.equals(((Mask) obj).raw);
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return {@link #nick}
     */
    public String getNick() {
        return nick;
    }

    public String getRaw() {
        return raw;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return raw.hashCode();
    }

    @Override
    public String toString() {
        return raw;
    }
}
