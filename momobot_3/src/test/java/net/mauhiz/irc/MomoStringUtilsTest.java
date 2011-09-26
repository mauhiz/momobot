package net.mauhiz.irc;

import net.mauhiz.util.StringUtil;

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
     * Test for {@link StringUtil#normalizeToAscii(String)}
     */
    @Test
    public void testEffaceAccents() {
        String input = "âäàçéèëïîñôöqù ";
        String output = StringUtil.normalizeToAscii(input);
        Assert.assertEquals("aaaceeeiinooqu ", output);

        input = "ého";
        output = StringUtil.normalizeToAscii(input);
        Assert.assertEquals("eho", output);

    }
}
