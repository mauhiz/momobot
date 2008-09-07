package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.defaut.DefaultChannel;
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
        IrcChannel chan = new DefaultChannel("#tsi.fr");
        Pickup p = new Pickup(chan);
        IrcUser a = new FakeUser("a");
        IrcUser b = new FakeUser("b");
        IrcUser c = new FakeUser("c");
        IrcUser d = new FakeUser("d");
        IrcUser e = new FakeUser("e");
        IrcUser f = new FakeUser("f");
        IrcUser g = new FakeUser("g");
        IrcUser h = new FakeUser("h");
        
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
