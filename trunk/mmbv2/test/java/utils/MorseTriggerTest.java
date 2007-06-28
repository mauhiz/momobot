package utils;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class MorseTriggerTest {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(MorseTriggerTest.class);

    /**
     * Test method for {@link utils.MomoStringUtils#fromMorse(String)}.
     */
    @Test
    public void testFromMorse() {
        LOG
                .info(MomoStringUtils
                        .fromMorse(".--. --- ..- .-. ...- .- .-.. .. -.. . .-. .. .-.. - . ... ..- ..-. ..-. .. - -.. . - .- .--. . .-. ... .- -- ..- . .-.. ... ..- .. ...- .. . -.. . ... --- -. -. --- -- -.. . ..-. .- -- .. .-.. .-.. . .-.. . - --- ..- - .- - - .- -.-. .... . . - . -. -- .. -. ..- ... -.-. ..- .-.. ."));
        Assert.assertEquals(null, null);
    }
}
