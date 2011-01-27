package net.mauhiz.contest.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mauhiz
 */
public abstract class SingleLinePuzzle extends ExportedRunnable {
	@Override
	protected final void process(BufferedReader input, PrintWriter output) throws IOException {
		doProblem(input.readLine(), output);
	}

	protected abstract void doProblem(String readLine, PrintWriter output);
}
