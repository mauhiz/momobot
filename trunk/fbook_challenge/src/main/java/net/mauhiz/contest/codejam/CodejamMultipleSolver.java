package net.mauhiz.contest.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.mauhiz.contest.MultipleSolver;

/**
 * @author mauhiz
 */
public abstract class CodejamMultipleSolver extends MultipleSolver {

	@Override
	protected final void process(BufferedReader input, PrintWriter output, int numTestCases) throws IOException {
		for (int problemIndex = 1; problemIndex <= numTestCases; problemIndex++) {
			output.print("Case #" + problemIndex + ": " + doJamProblem(input));
			if (problemIndex != numTestCases) {
				output.println();
			}
		}
	}

	protected abstract String doJamProblem(BufferedReader input) throws IOException;
}
