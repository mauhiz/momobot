package net.mauhiz.contest.codejam;

import net.mauhiz.contest.MultipleSolver;

/**
 * @author mauhiz
 */
public abstract class CodejamMultipleSolver extends MultipleSolver {

	/**
	 * test case counter
	 */
	private int i = 0;

	@Override
	protected final String doProblem(String problemLine) {
		return "Case #" + ++i + ": " + doJamProblem(problemLine);
	}

	protected abstract String doJamProblem(String problemLine);
}
