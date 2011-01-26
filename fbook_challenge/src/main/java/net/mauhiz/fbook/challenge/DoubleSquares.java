package net.mauhiz.fbook.challenge;

import java.io.IOException;

/**
 * A double-square number is an integer X which can be expressed as the sum of two perfect squares.
 * For example, 10 is a double-square because 10 = 32 + 12.
 * Your task in this problem is, given X, determine the number of ways
 * in which it can be written as the sum of two squares.
 * For example, 10 can only be written as 32 + 12 (we don't count 12 + 32 as being different).
 * On the other hand, 25 can be written as 52 + 02 or as 42 + 32.

Input
You should first read an integer N, the number of test cases. The next N lines will contain N values of X.
Constraints
0 ≤ X ≤ 2147483647
Output
For each value of X, you should output the number of ways to write X as the sum of two squares.

 * @author mauhiz
 *
 */
public class DoubleSquares extends FbookChallenge {

	public static void main(String... args) throws IOException {
		new DoubleSquares().run(args);
	}

	private long isqrt(long i) {
		return (long) Math.floor(Math.sqrt(i));
	}

	@Override
	public String getName() {
		return "double_squares";
	}

	@Override
	protected String doProblem(String[] chunks) {
		int n = Integer.parseInt(chunks[0]);
		int zs = 0; // count of ways
		long x = isqrt(n);
		long y = 0;
		while (true) {
			if (x < y) {
				break;
			} else if (x * x + y * y < n) {
				y++;
				continue;
			} else if (n < x * x + y * y) {
				x--;
				continue;
			} else { // equality
				zs++;
				x--;
				y++;
			}
		}
		return Integer.toString(zs);
	}
}
