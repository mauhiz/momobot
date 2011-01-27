package net.mauhiz.contest.codejam;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author mauhiz
 */
public abstract class CodejamLineSolver extends CodejamMultipleSolver {


	protected abstract String doJamProblem(String problemLine);

	@Override
	protected final String doJamProblem(BufferedReader input) throws IOException {
		return doJamProblem(input.readLine());
	}
}
