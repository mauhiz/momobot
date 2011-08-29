package net.mauhiz.util;

/**
 * @author mauhiz
 */
public enum MathUtils {
    ;
    /**
     * @param a
     * @param b
     *            >= 0
     * @return a^b
     */
    public static long longPower(long a, long b) {
        if (b < 0L) {
            throw new ArithmeticException("Use Math.pow because negative powers are not integers");
        } else if (b == 0L) {
            /* 0^0 et 0^x = 1 */
            return 1L;
        } else if (a == 0L) {
            /* x^0 = 1 */
            return 0L;
        } else if (a == 1L) {
            /* x^1 = x */
            return 1L;
        } else if (b > Long.SIZE) {
            throw new ArithmeticException("b > Long.SIZE. use Math.pow with floating point precision");
        } else if (a == 2L) {
            /* 2^x = 0b0000001000, ou 1 est en xieme position */
            return 1L << b;
        }
        return recursiveLongPower(a, b);
    }

    /**
     * @param a
     * @param b
     *            >= 0
     * @return a^b
     */
    public static int power(int a, int b) {
        long result = longPower(a, b);
        if (result < Integer.MAX_VALUE) {
            return (int) result;
        }
        throw new ArithmeticException("a^b > Integer.MAX_VALUE. use longPower");
    }

    /**
     * @param a
     *            int != 2
     * @param b
     *            int > 0
     * @return a^b
     */
    private static long recursiveLongPower(long a, long b) {
        if (b == 1L) {
            /* point d arret */
            return a;
            /* si b est pair */
        } else if ((b & 1L) == 0L) {
            /* x^10 = (x^5)^2 */
            long squareRoot = recursiveLongPower(a, b >> 1);
            if (squareRoot > Integer.MAX_VALUE) {
                throw new ArithmeticException("a^b > Long.MAX_VALUE. use Math.pow with floating point precision");
            }
            /* b >> 1 = b / 2 */
            return squareRoot * squareRoot;
        }
        /* RECURSE ! */
        return a * recursiveLongPower(a, b - 1);
    }
}
