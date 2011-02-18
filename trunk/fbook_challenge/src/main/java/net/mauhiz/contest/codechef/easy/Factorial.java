package net.mauhiz.contest.codechef.easy;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import net.mauhiz.contest.codechef.CodechefMultipleSolver;

public class Factorial extends CodechefMultipleSolver {
	public static void main(String... args) throws IOException {
		new Factorial().runDefault();
	}

	@Override
	public String getName() {
		return "FCTRL";
	}

	@Override
	protected void processSingle(Scanner input, PrintStream output, int probleIndex) throws IOException {
		String line = input.nextLine();
		int n = Integer.parseInt(line);
		output.println(zed(n));
	}

	/**
	 * z = n%5 + n%(5^2) + ...
	 */
	public int zed(int n) {
		int count = 0;
		int num = n;
		while (num >= 5) {
			num /= 5;
			count += num;
		}
		return count;
	}
}
