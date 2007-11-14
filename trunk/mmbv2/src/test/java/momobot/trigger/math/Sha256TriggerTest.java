package momobot.trigger.math;

import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class Sha256TriggerTest {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(Sha256TriggerTest.class);

    /**
     * @param args
     * @throws NoSuchAlgorithmException
     */
    static void testSha256(final String args) throws NoSuchAlgorithmException {
        byte[] input = args.getBytes();
        LOG.info(Sha256Trigger.computeSha256(input));
    }

    /**
     * Test method for
     * {@link momobot.trigger.math.Md5Trigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, String)}
     * 
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testComputeFractionContinue() throws NoSuchAlgorithmException {
        testSha256("password");
        testSha256("admin");
    }
}
