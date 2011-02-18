package net.mauhiz.contest.codechef;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * @author mauhiz
 */
public abstract class CodechefMultipleSolver extends CodechefSolver {
	protected int numTestCases;

	@Override
	protected void process(Scanner input, PrintStream output) throws IOException {
		String firstLine = input.nextLine();
		numTestCases = Integer.parseInt(firstLine);
		//		log.info("Going to process " + numTestCases + " cases");
		for (int problemIndex = 1; problemIndex <= numTestCases; problemIndex++) {
			//			long start = System.currentTimeMillis();
			processSingle(input, output, problemIndex);
			//			log.info("Solved case #" + problemIndex + " in " + (System.currentTimeMillis() - start) + "ms.");
		}
	}

	protected abstract void processSingle(Scanner input, PrintStream output, int probleIndex) throws IOException;
}
