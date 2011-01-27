package net.mauhiz.contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author mauhiz
 */
public abstract class LineSolver extends MultipleSolver {
	@Override
	protected final void process(BufferedReader input, PrintWriter output, int numTestCases) throws IOException {
		for (int lineIndex = 1; lineIndex <= numTestCases; lineIndex++) {
			String line = input.readLine();
			if (line == null) {
				throw new IllegalStateException("Input too short!");
			}
			String result = doProblem(line);

			output.print(result);
			if (lineIndex != numTestCases) {
				output.println();
			}
		}
	}
	
	protected abstract String doProblem(String problemLine) ;
}
