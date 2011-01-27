package net.mauhiz.contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mauhiz
 */
public abstract class MultipleSolver extends AbstractSolver {
	protected int numTestCases;

	@Override
	protected final void process(BufferedReader input, PrintWriter output) throws IOException {
		String firstLine = input.readLine();
		if (firstLine == null) {
			throw new IllegalStateException("Input too short!");
		}
		numTestCases = Integer.parseInt(firstLine);
		log.info("Going to process " + numTestCases + " cases");
		for (int problemIndex = 1; problemIndex <= numTestCases; problemIndex++) {
			long start = System.currentTimeMillis();
			processSingle(input, output, problemIndex);
			log.info("Solved case #" + problemIndex + " in " + (System.currentTimeMillis() - start) + "ms.");
		}
	}

	protected abstract void processSingle(BufferedReader input, PrintWriter output, int probleIndex) throws IOException;
}
