package net.mauhiz.contest;


/**
 * @author mauhiz
 */
public abstract class MultipleChunkSolver extends MultipleSolver {

	@Override
	protected String doProblem(String problemLine) {
		return doProblem(problemLine.split(" "));
	}

	protected abstract String doProblem(String[] chunks);
}
