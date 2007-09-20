package momobot.trigger.fun;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class MargueriteTriggerTest {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(MargueriteTriggerTest.class);

    /**
     * Test method for
     * {@link momobot.trigger.fun.MargueriteTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)}.
     */
    @Test
    public final void testExecutePublicTrigger() {
        final MargueriteTrigger mt = new MargueriteTrigger(null);
        LOG.info(mt.generateResponse("Germaine"));
    }
}
