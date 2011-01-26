package net.mauhiz.fbook.puzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.mauhiz.fbook.CommandLineClient;

/**
 * @author mauhiz
 */
public abstract class FbookPuzzle extends CommandLineClient {
	@Override
	protected final void process(BufferedReader input, PrintWriter output) throws IOException {
		doProblem(input.readLine(), output);
	}

	protected abstract void doProblem(String readLine, PrintWriter output);
}
