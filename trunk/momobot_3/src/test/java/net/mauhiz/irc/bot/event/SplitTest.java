package net.mauhiz.irc.bot.event;

import net.mauhiz.irc.base.data.Channel;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Topper
 * 
 */
public class SplitTest {
    
    /**
     * exemple de test
     */
    @Test
    public void testSplit() {
        Channel chan = new Channel("#tsi.fr");
        Gather gather = new Gather(chan);
        String[] str = {"ON", "\"127.06576.467:27015", "FAST", "ET", "BAN", "mdp:dtcdtc\""};
        SeekWar seekwar = new SeekWar(gather);
        String[] strout = seekwar.split(str);
        Assert.assertEquals(strout[0], "ON");
        Assert.assertEquals(strout[1], "\"127.06576.467:27015 FAST ET BAN mdp:dtcdtc\"");
        
        String[] str1 = {"On", "\"127.06576.467:27015", "fast", "et", "ban", "mdp:dtcdtc\"", "mid", "\"seek", "%Pv%P",
                "-", "%S", "-", "%L", "pm\""};
        strout = seekwar.split(str1);
        Assert.assertEquals(strout[0], "On");
        Assert.assertEquals(strout[1], "\"127.06576.467:27015 fast et ban mdp:dtcdtc\"");
        Assert.assertEquals(strout[2], "mid");
        Assert.assertEquals(strout[3], "\"seek %Pv%P - %S - %L pm\"");
        
    }
}
