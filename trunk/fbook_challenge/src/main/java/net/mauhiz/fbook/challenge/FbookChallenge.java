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
		int numTestCases = Integer.parseInt(input.readLine());

		for (int lineIndex = 1; lineIndex <= numTestCases; lineIndex++) {
			String result = doProblem(input.readLine().split(" "));
			
			output.print(result);
			if (lineIndex != numTestCases) {
				output.println();
			}
		}
	}
	
	protected abstract String doProblem(String[] chunks) ;
}
