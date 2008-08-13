package net.mauhiz.irc.bot.event;

import org.junit.Test;

/**
 * @author Topper
 * 
 */
public class SlitTest {
    
    /**
     * exemple de test
     */
    @Test
    public void testSplit() {
        String[] str = {"ON", "\"127.06576.467:27015", "FAST", "ET", "BAN", "mdp:dtcdtc\""};
        SeekWar seekwar = new SeekWar();
        String[] strout = seekwar.Split(str);
        System.out.println("0:" + strout[0]);
        System.out.println("1:" + strout[1]);
        // System.out.println("2:" + strout[2]);
        // System.out.println(StringUtils.join(seekwar.Split(str)));
        String[] str1 = {"On", "\"127.06576.467:27015", "fast", "et", "ban", "mdp:dtcdtc\"", "mid", "\"seek", "%Pv%P",
                "-", "%S", "-", "%L", "pm\""};
        strout = seekwar.Split(str1);
        System.out.println("0:" + strout[0]);
        System.out.println("1:" + strout[1]);
        System.out.println("2:" + strout[2]);
        System.out.println("3:" + strout[3]);
        
        System.out.println(seekwar.start(str1));
        
    }
}
