package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.bot.event.Pickup;

/**
 * @author Topper
 * 
 */
public class ShakeTest {
    
    /**
     * exemple de test
     */
    public void testShake() {
        Channel chan = new Channel("#tsi.fr");
        Pickup p = new Pickup(chan);
        IrcUser a = new IrcUser("a");
        IrcUser b = new IrcUser("b");
        IrcUser c = new IrcUser("c");
        IrcUser d = new IrcUser("d");
        p.add(a, "a");
        p.add(b, "a");
        p.add(c, "a");
        p.add(d, "a");
        p.shake();
        System.out.println(p);
    }
}
