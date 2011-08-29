package net.mauhiz.contest.euler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.mauhiz.contest.AbstractSolver;
import net.mauhiz.contest.LineSolver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class Euler18 extends AbstractSolver {

	static class Line {
		int[] values;
	}

	static class Leaf {
		public Leaf(int i) {
			value = i;
		}

		Leaf sub1;
		Leaf sub2;
		int value;
	}

	static class Branch {
		List<Leaf> leaves = new ArrayList<Leaf>();

		int sum() {
			int s = 0;
			for (Leaf leaf : leaves) {
				s += leaf.value;
			}
			return s;
		}
	}

	@Override
	public String getName() {
		return "euler-18";
	}

	@Override
	protected void process(BufferedReader input, PrintWriter output) throws IOException {
		List<Line> triangle = new ArrayList<Line>();
		for (int i = 0;; i++) {
			String line = input.readLine();
			if (line == null) {
				break;
			}
			String[] contents = StringUtils.split(line);
			Line row = new Line();
			row.values = new int[i + 1];
			for (int j = 0; j < i + 1; j++) {
				row.values[j] = Integer.parseInt(contents[j]);
			}
			triangle.add(row);
		}

		int maxSum = 0;

		// create branches
		Leaf top = new Leaf(triangle.get(0).values[0]);
		top.sub1 = new Leaf(triangle.get(1).values[0]);
		top.sub2 = new Leaf(triangle.get(1).values[1]);
		// TODO largely unfinished
	}

}
