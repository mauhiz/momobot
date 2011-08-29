package net.mauhiz.contest.codejam.round1a2011;

import java.io.IOException;
import java.math.BigInteger;

import net.mauhiz.contest.codejam.CodejamLineChunkSolver;

public class FreeCell extends CodejamLineChunkSolver {
	private static final String POSSIBLE = "Possible";
	private static final String BROKEN = "Broken";

	public static void main(String... args) throws IOException {
		new FreeCell().run(args);
	}

	@Override
	protected String doJamProblem(String[] chunks) {
		BigInteger n = new BigInteger(chunks[0]);

		int pd = Integer.parseInt(chunks[1]);
		int pg = Integer.parseInt(chunks[2]);
		if (pg == 100 && pd != 100) {

			return BROKEN;
		} else if (pg == 0 && pd != 0) {
			return BROKEN;
		} else {
			int dmax = Math.min(n.intValue(), 100);
			for (int d = 1; d <= dmax; d++) {
				if ((d * pd) % (100) == 0) {
					return POSSIBLE;
				}
			}
		}
		return BROKEN;
	}

	@Override
	public String getName() {
		return "freecell";
	}

}
