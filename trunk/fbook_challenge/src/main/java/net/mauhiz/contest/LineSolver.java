package net.mauhiz.contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mauhiz
 */
public abstract class LineSolver extends MultipleSolver {
	protected abstract String doProblem(String problemLine);

	@Override
	protected final void processSingle(BufferedReader input, PrintWriter output, int problemIndex) throws IOException {
		output.print(doProblem(input.readLine()));

		if (problemIndex == numTestCases) {
			output.println();
		}

	}
}
