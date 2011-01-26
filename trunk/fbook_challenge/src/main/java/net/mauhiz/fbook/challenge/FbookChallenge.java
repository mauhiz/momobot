package net.mauhiz.fbook.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.mauhiz.fbook.CommandLineClient;

/**
 * @author mauhiz
 */
public abstract class FbookChallenge extends CommandLineClient {
	@Override
	protected final void process(BufferedReader input, PrintWriter output) throws IOException {
		String firstLine = input.readLine();
		if (firstLine == null) {
			throw new IllegalStateException("Input too short!");
		}
		int numTestCases = Integer.parseInt(firstLine);

		for (int lineIndex = 1; lineIndex <= numTestCases; lineIndex++) {
			String line = input.readLine();
			if (line == null) {
				throw new IllegalStateException("Input too short!");
			}
			String result = doProblem(line.split(" "));

			output.print(result);
			if (lineIndex != numTestCases) {
				output.println();
			}
		}
	}
	
	protected abstract String doProblem(String[] chunks) ;
}
