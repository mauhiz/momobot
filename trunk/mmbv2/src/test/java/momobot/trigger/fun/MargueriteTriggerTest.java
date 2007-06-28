package momobot.trigger.fun;

import org.junit.Test;

/**
 * @author mauhiz
 */
public class MargueriteTriggerTest {
    /**
     * Test method for
     * {@link momobot.trigger.fun.MargueriteTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)}.
     */
    @Test
    public final void testExecutePublicTrigger() {
        final MargueriteTrigger mt = new MargueriteTrigger(null);
        System.out.println(mt.generateResponse("Germaine"));
    }
}
