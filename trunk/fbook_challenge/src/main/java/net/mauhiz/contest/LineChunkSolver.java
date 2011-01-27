package net.mauhiz.contest;


/**
 * @author mauhiz
 */
public abstract class LineChunkSolver extends LineSolver {

	@Override
	protected String doProblem(String problemLine) {
		return doProblem(problemLine.split(" "));
	}

	protected abstract String doProblem(String[] chunks);
}
