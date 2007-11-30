/*
 * Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/ This file is part of JBouncer. This software is
 * dual-licensed, allowing you to choose between the GNU General Public License (GPL) and the www.jibble.org Commercial
 * License. Since the GPL may be too restrictive for use in a proprietary application, a commercial license is also
 * provided. Full license information can be found at http://www.jibble.org/licenses/ $Author: pjm2 $ $Id:
 * JBouncer.java,v 1.2 2004/03/01 19:13:37 pjm2 Exp $
 */
package bouncer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class JBouncer {
    /**
     * 
     */
    private static final Logger              LOG     = Logger.getLogger(JBouncer.class);
    /**
     * 
     */
    private Map < String, ServerConnection > servers = new HashMap < String, ServerConnection >();
    /**
     * 
     */
    private User                             user;

    /**
     * @param user1
     */
    public JBouncer(final User user1) {
        this.user = user1;
    }

    /**
     * @param key
     * @param server
     */
    public void add(final String key, final ServerConnection server) {
        synchronized (this.servers) {
            this.servers.put(key, server);
        }
        LOG.info(this.user.getLogin() + " is now connected to " + this.servers.size() + " servers.");
        server.connect();
    }

    /**
     * @return
     */
    public int getClientCount() {
        int total = 0;
        synchronized (this.servers) {
            for (ServerConnection next : this.servers.values()) {
                total += next.getClientCount();
            }
        }
        return total;
    }

    /**
     * @return
     */
    public Map < String, ServerConnection > getServers() {
        return this.servers;
    }

    /**
     * @param key
     */
    public void remove(final String key) {
        synchronized (this.servers) {
            ServerConnection server = this.servers.get(key);
            if (server != null) {
                this.servers.remove(key);
                server.quitServer();
                // server.dispose();
                LOG.info(this.user.getLogin() + " removed server " + server.getCurrentServer());
            }
        }
    }
}
