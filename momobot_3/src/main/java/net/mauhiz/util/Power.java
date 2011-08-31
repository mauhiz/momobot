package net.mauhiz.util;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author mauhiz
 */
public class Power extends RecursiveTask<Long> {

    private static final long serialVersionUID = -7167942143806652482L;

    /**
     * @param a
     * @param b
     *            >= 0
     * @return a^b
     */
    public static long longPower(final long a, final long b) {

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

        ForkJoinPool pool = new ForkJoinPool(2);
        ForkJoinTask<Long> f = pool.submit(new Power(a, b));
        return f.join().longValue();
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

    private final long a;
    private final long b;

    private Power(long a, long b) {
        this.a = a;
        this.b = b;
    }

    @Override
    protected Long compute() {
        if (b == 1L) {
            /* point d arret */
            return Long.valueOf(a);
            /* si b est pair */
        } else if ((b & 1L) == 0L) {
            /* x^10 = (x^5)^2 */
            Power sub = new Power(a, b >> 1);
            sub.fork();
            long squareRoot = sub.join().longValue();
            if (squareRoot > Integer.MAX_VALUE) { // Integer.MAX_VALUE = sqrt(Long.MAX_VALUE)
                throw new ArithmeticException("a^b > Long.MAX_VALUE. use Math.pow with floating point precision");
            }
            /* b >> 1 = b / 2 */
            return Long.valueOf(squareRoot * squareRoot);
        }
        Power sub = new Power(a, b - 1);
        sub.fork();
        return Long.valueOf(a * sub.join().longValue());
    }
}
