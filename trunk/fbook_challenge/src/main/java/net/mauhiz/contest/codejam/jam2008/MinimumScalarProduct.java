package net.mauhiz.contest.codejam.jam2008;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

import net.mauhiz.contest.codejam.CodejamMultipleSolver;
import net.mauhiz.contest.util.IntArrayPermutationGenerator;

public class MinimumScalarProduct extends CodejamMultipleSolver {

	public static void main(String... args) throws IOException {
		new MinimumScalarProduct().run(args);
	}

	private static int scalarProduct(int[] v1, int[] v2) {
		int s = 0;
		for (int i = 0; i < v1.length; i++) {
			s += v1[i] * v2[i];
		}
		return s;
	}

	@Override
	protected String doJamProblem(BufferedReader input) throws IOException {
		String wLine = input.readLine();
		int w = Integer.parseInt(wLine);
		int[][] ls = new int[2][w];

		for (int c = 0; c < 2; c++) {
			String line = input.readLine();
			String[] chunks = line.split(" ");
			for (int i = 0; i < w; i++) {
				ls[c][i] = Integer.parseInt(chunks[i]);
			}
		}

		int msp = Integer.MAX_VALUE;

		// no need to run permutations on both sides. Would be a w!*(w-1)! waste :)
		Iterator<int[]> it0 = new IntArrayPermutationGenerator(ls[0]);

		while (it0.hasNext()) {
			int[] v0 = it0.next();
			int s = scalarProduct(v0, ls[1]);
			if (s < msp) {
				msp = s;
			}

		}

		return Integer.toString(msp);
	}

	@Override
	public String getName() {
		return "msp";
	}
}
