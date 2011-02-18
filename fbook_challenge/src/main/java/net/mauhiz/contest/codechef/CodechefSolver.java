package net.mauhiz.contest.codechef;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public abstract class CodechefSolver {

	protected abstract String getName();

	protected abstract void process(Scanner input, PrintStream output) throws IOException;

	protected void runDefault() throws IOException {
		Scanner reader = new Scanner(System.in);

		try {
			process(reader, System.out);

		} finally {
			reader.close();
		}
	}
}
