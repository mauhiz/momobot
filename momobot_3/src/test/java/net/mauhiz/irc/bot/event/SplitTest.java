package net.mauhiz.irc.bot.event;

import java.util.List;

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
        String[] str = {"ON", "\"127.06576.467:27015", "FAST", "ET", "BAN", "mdp:dtcdtc\""};
        SeekWar seekwar = new SeekWar();
        List<String> split = seekwar.split(str);
        String[] strout = split.toArray(new String[split.size()]);
        Assert.assertEquals(strout[0], "ON");
        Assert.assertEquals(strout[1], "\"127.06576.467:27015 FAST ET BAN mdp:dtcdtc\"");
        
        String[] str1 = {"On", "\"127.06576.467:27015", "fast", "et", "ban", "mdp:dtcdtc\"", "mid", "\"seek", "%Pv%P",
                "-", "%S", "-", "%L", "pm\""};
        split = seekwar.split(str1);
        strout = split.toArray(new String[split.size()]);
        Assert.assertEquals(strout[0], "On");
        Assert.assertEquals(strout[1], "\"127.06576.467:27015 fast et ban mdp:dtcdtc\"");
        Assert.assertEquals(strout[2], "mid");
        Assert.assertEquals(strout[3], "\"seek %Pv%P - %S - %L pm\"");
        
    }
}
