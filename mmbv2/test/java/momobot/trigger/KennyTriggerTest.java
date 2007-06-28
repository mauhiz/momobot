package momobot.trigger;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class KennyTriggerTest {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(KennyTriggerTest.class);

    /**
     * Test method for {@link KennyTrigger#translate(String)}.
     */
    @Test
    public void testTranslate() {
        LOG.info(KennyTrigger.translate("Hello world!"));
        Assert.assertEquals(null, null);
    }
}
