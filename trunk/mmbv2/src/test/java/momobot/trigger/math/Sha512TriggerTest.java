package momobot.trigger.math;

import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class Sha512TriggerTest {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(Sha512TriggerTest.class);

    /**
     * @param args
     * @throws NoSuchAlgorithmException
     */
    static void testSha512(final String args) throws NoSuchAlgorithmException {
        byte[] input = args.getBytes();
        LOG.info(Sha512Trigger.computeSha512(input));
    }

    /**
     * Test method for
     * {@link momobot.trigger.math.Md5Trigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, String)}
     * 
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testComputeFractionContinue() throws NoSuchAlgorithmException {
        testSha512("password");
        testSha512("admin");
    }
}
