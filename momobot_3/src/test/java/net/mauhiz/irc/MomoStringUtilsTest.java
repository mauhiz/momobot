package net.mauhiz.irc;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class MomoStringUtilsTest {
    
    /**
     * Test for {@link MomoStringUtils#effaceAccents(String)}
     */
    @Test
    public void testEffaceAccents() {
        String input = "אגהחיךטמןספqû ";
        String output = MomoStringUtils.effaceAccents(input);
        Assert.assertEquals("aaaceeeiinoqu ", output);
    }
    
}
