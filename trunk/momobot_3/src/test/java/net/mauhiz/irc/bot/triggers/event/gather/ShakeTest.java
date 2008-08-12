package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.bot.event.Pickup;

import org.junit.Test;

/**
 * @author Topper
 * 
 */
public class ShakeTest {
    
    /**
     * exemple de test
     */
    @Test
    public void testShake() {
        Channel chan = new Channel("#tsi.fr");
        Pickup p = new Pickup(chan);
        IrcUser a = new IrcUser("a");
        IrcUser b = new IrcUser("b");
        IrcUser c = new IrcUser("c");
        IrcUser d = new IrcUser("d");
        IrcUser e = new IrcUser("e");
        IrcUser f = new IrcUser("f");
        IrcUser g = new IrcUser("g");
        IrcUser h = new IrcUser("h");
        
        p.add(a, "a");
        p.add(b, "a");
        p.add(c, "a");
        p.add(d, "a");
        p.add(e, "b");
        p.add(f, "b");
        p.add(g, "b");
        p.add(h, "b");
        p.shake();
        System.out.println(p);
    }
}
