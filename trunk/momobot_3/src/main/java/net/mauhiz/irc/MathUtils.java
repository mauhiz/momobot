package net.mauhiz.irc;

/**
 * @author mauhiz
 */
public class MathUtils {
    
    /**
     * @param a
     * @param b
     * @return a^b
     */
    public static int power(final int a, final int b) {
        if (b < 0) {
            throw new IllegalArgumentException("b must be positive");
        }
        if (a == 2) {
            return powerOfTwo(b);
        }
        return recursivePower(a, b);
    }
    
    /**
     * @param b
     * @return 2^b
     */
    private static int powerOfTwo(final int b) {
        return 1 << b;
    }
    
    /**
     * @param a
     * @param b
     * @return
     */
    private static int recursivePower(final int a, final int b) {
        if (b == 0) {
            return 1;
        }
        return a * recursivePower(a, b - 1);
    }
}
