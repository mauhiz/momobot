/*
 * Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/ This file is part of JBouncer. This software is
 * dual-licensed, allowing you to choose between the GNU General Public License (GPL) and the www.jibble.org Commercial
 * License. Since the GPL may be too restrictive for use in a proprietary application, a commercial license is also
 * provided. Full license information can be found at http://www.jibble.org/licenses/ $Author: pjm2 $ $Id:
 * JBouncerManager.java,v 1.2 2004/03/01 19:13:37 pjm2 Exp $
 */
package bouncer;

import java.util.Map;

/**
 * @author mauhiz
 */
public class JBouncerManager {
    /**
     * Maps User -> JBouncer.
     */
    private Map < User, JBouncer > bouncers;
    /**
     * 
     */
    private long                   startTime;

    /**
     * @param bouncers1
     */
    public JBouncerManager(final Map < User, JBouncer > bouncers1) {
        this.bouncers = bouncers1;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * @param user
     * @return
     */
    public JBouncer getBouncer(final User user) {
        return this.bouncers.get(user);
    }

    /**
     * @return
     */
    public Map < User, JBouncer > getBouncers() {
        return this.bouncers;
    }

    /**
     * @return
     */
    public long getStartTime() {
        return this.startTime;
    }
}
