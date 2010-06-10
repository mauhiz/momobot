package net.mauhiz.irc;

import java.util.regex.Matcher;

import net.mauhiz.irc.bot.event.SeekWar2;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class MomoStringUtilsTest {
    
    /**
     * Test for {@link MomoStringUtils#genereSeekMessage(String, int, String, String)}
     * 
     */
    @Test
    public void genereSeekMessage() {
        String input = "seek %Pv%P - %S - %L pm ";
        String output = MomoStringUtils.genereSeekMessage(input, 5, "Off", "midz");
        Assert.assertEquals("seek 5v5 - Off - midz pm ", output);
    }
    
    /**
     * Test for {@link MomoStringUtils#effaceAccents(String)}
     */
    @Test
    public void testEffaceAccents() {
        String input = "âäàçéèëïîñôöqù ";
        String output = MomoStringUtils.effaceAccents(input);
        Assert.assertEquals("aaaceeeiinooqu ", output);
        
        input = "�ho";
        output = MomoStringUtils.effaceAccents(input);
        Assert.assertEquals("eho", output);
        
    }
    
    /** "192.168.0.5:27015 pass:dtcdtc" */
    @Test
    public void testMatchIp() {
        String text = "192.168.0.5:27015 pass:dtcdtc";
        Matcher m = SeekWar2.IP_PATTERN.matcher(text);
        Assert.assertTrue(m.find());
        Assert.assertEquals("192.168.0.5:27015", m.group());
    }
}
