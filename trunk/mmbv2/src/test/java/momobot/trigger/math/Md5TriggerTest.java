package momobot.trigger.math;

import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class Md5TriggerTest {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(Md5TriggerTest.class);

    /**
     * @param args
     * @throws NoSuchAlgorithmException
     */
    static void testMd5(final String args) throws NoSuchAlgorithmException {
        byte[] input = args.getBytes();
        LOG.info(Md5Trigger.computeMd5(input));
    }

    /**
     * Test method for
     * {@link momobot.trigger.math.Md5Trigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, String)}
     * 
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testComputeFractionContinue() throws NoSuchAlgorithmException {
        testMd5("password");
        testMd5("admin");
    }
}
