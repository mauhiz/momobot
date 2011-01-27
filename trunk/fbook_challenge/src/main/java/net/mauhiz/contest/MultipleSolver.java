package net.mauhiz.contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author mauhiz
 */
public abstract class MultipleSolver extends AbstractSolver {
	@Override
	protected void process(BufferedReader input, PrintWriter output) throws IOException {
		String firstLine = input.readLine();
		if (firstLine == null) {
			throw new IllegalStateException("Input too short!");
		}
		int numTestCases = Integer.parseInt(firstLine);
		process(input, output, numTestCases);
	}
	
	protected abstract void process(BufferedReader input, PrintWriter output, int numTestCases) throws IOException;
}
