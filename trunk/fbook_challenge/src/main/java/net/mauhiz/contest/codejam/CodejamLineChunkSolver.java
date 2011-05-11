package net.mauhiz.contest.codejam;

/**
 * @author mauhiz
 */
public abstract class CodejamLineChunkSolver extends CodejamLineSolver {

	@Override
	protected final String doJamProblem(String problemLine) {
		return doJamProblem(problemLine.split(" "));
	}

	protected abstract String doJamProblem(String[] chunks);
}
