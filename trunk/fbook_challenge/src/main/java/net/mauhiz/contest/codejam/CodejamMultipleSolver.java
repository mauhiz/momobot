package net.mauhiz.contest.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.mauhiz.contest.MultipleSolver;

/**
 * @author mauhiz
 */
public abstract class CodejamMultipleSolver extends MultipleSolver {

	protected abstract String doJamProblem(BufferedReader input) throws IOException;

	@Override
	protected final void processSingle(BufferedReader input, PrintWriter output, int problemIndex) throws IOException {
		output.println("Case #" + problemIndex + ": " + doJamProblem(input));
	}
}
