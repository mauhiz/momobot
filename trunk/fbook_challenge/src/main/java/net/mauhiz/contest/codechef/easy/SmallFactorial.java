package net.mauhiz.contest.codechef.easy;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;

import net.mauhiz.contest.codechef.CodechefMultipleSolver;

public class SmallFactorial extends CodechefMultipleSolver {
	public static void main(String... args) throws IOException {
		new SmallFactorial().runDefault();
	}

	@Override
	public String getName() {
		return "FCTRL2";
	}

	@Override
	protected void processSingle(Scanner input, PrintStream output, int probleIndex) {
		String line = input.nextLine();
		int n = Integer.parseInt(line);
		output.println(getCachedFact(n));
	}
	
	private String getCachedFact(int n) {
		if (cache == null) {
			int max = 100;
			cache = new String[max];
			initFact(max);
		}
		return cache[n];
	}

	protected String[] cache;

	public void initFact(int max) {
		BigInteger previous = BigInteger.valueOf(1);
		cache[0] = previous.toString();

		for (int i = 1; i < max; i++) {
			BigInteger fact = previous.multiply(BigInteger.valueOf(i));
			cache[i] = fact.toString();
			previous = fact;
		}
	}
}
