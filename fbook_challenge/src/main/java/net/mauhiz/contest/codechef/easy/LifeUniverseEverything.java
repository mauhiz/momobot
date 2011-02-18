package net.mauhiz.contest.codechef.easy;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import net.mauhiz.contest.codechef.CodechefSolver;

public class LifeUniverseEverything extends CodechefSolver {
	public static void main(String... args) throws IOException {
		new LifeUniverseEverything().runDefault();
	}

	@Override
	public String getName() {
		return "TEST";
	}

	@Override
	protected void process(Scanner input, PrintStream output) throws IOException {
		while (true) {
			String line = input.nextLine();
			if ("42".equals(line)) {
				break;
			}
			output.println(line);
		}
	}
}
