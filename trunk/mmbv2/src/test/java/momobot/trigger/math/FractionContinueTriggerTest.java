package momobot.trigger.math;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author mauhiz
 */
public class FractionContinueTriggerTest {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(FractionContinueTriggerTest.class);

    /**
     * @param number
     */
    static void testComputeFractionContinue(final double number) {
        LOG.info(Double.toString(number));
        LOG.info(FractionContinueTrigger.computeFractionContinue(number));
    }

    /**
     * Test method for {@link momobot.trigger.math.FractionContinueTrigger#computeFractionContinue(double)}.
     */
    @Test
    public void testComputeFractionContinue() {
        testComputeFractionContinue(0);
        testComputeFractionContinue(1);
        testComputeFractionContinue(1.55);
        testComputeFractionContinue(1.0 / 11.0);
        testComputeFractionContinue(Math.PI);
        testComputeFractionContinue(-Math.PI);
        testComputeFractionContinue(Math.E);
        testComputeFractionContinue(2 * Math.PI);
        testComputeFractionContinue(Math.PI / 3);
        testComputeFractionContinue(Math.log(2));
        testComputeFractionContinue(Math.sqrt(2));
        testComputeFractionContinue(Math.sqrt(3));
        testComputeFractionContinue(Math.sqrt(5));
        testComputeFractionContinue(Math.sqrt(7));
        testComputeFractionContinue(Math.sqrt(8));
        testComputeFractionContinue(Math.sqrt(10));
        testComputeFractionContinue(Math.sqrt(50));
    }
}
