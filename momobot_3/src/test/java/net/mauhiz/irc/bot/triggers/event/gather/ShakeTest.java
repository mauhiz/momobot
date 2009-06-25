package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.AbstractServerTest;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.bot.event.Pickup;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author Topper
 * 
 */
public class ShakeTest extends AbstractServerTest {
    private static final Logger logger = Logger.getLogger(ShakeTest.class);
    
    /**
     * exemple de test
     */
    @Test
    public void testShake() {
        IrcChannel chan = QNET.newChannel("#tsi.fr");
        Pickup p = new Pickup(chan);
        IrcUser a = QNET.newUser("a");
        IrcUser b = QNET.newUser("b");
        IrcUser c = QNET.newUser("c");
        IrcUser d = QNET.newUser("d");
        IrcUser e = QNET.newUser("e");
        IrcUser f = QNET.newUser("f");
        IrcUser g = QNET.newUser("g");
        IrcUser h = QNET.newUser("h");
        
        p.add(a, "a");
        p.add(b, "a");
        p.add(c, "a");
        p.add(d, "a");
        p.add(e, "b");
        p.add(f, "b");
        p.add(g, "b");
        p.add(h, "b");
        p.shake();
        logger.info(p);
    }
}
