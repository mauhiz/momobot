package net.mauhiz.contest.codejam.qual2011;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mauhiz.contest.codejam.CodejamLineChunkSolver;

import org.apache.commons.lang.StringUtils;

public class Magicka extends CodejamLineChunkSolver {

	static enum Base {
		Q, W, E, R, A, S, D, F;
	}

	static class Combination {
		String sequence;
		char output;
	}

	static class Opposition {
		char char1;
		char char2;
	}

	public static void main(String... args) throws IOException {
		new Magicka().run(args);
	}

	@Override
	protected String doJamProblem(String[] chunks) {
		int chunkIndex = 0;
		int c = Integer.parseInt(chunks[chunkIndex++]);

		// combinations
		Map<String, Character> combinations = new HashMap<String, Character>(c);
		for (int i = 0; i < c; i++) {
			String chunk = chunks[chunkIndex++];

			combinations.put(StringUtils.reverse(chunk.substring(0, 2)), chunk.charAt(2));
			combinations.put(chunk.substring(0, 2), chunk.charAt(2));
		}

		int d = Integer.parseInt(chunks[chunkIndex++]);

		// oppositions
		Map<Character, Character> oppositions = new HashMap<Character, Character>(d);
		for (int i = 0; i < d; i++) {
			String chunk = chunks[chunkIndex++];
			oppositions.put(chunk.charAt(0), chunk.charAt(1));
			oppositions.put(chunk.charAt(1), chunk.charAt(0));
		}

		int n = Integer.parseInt(chunks[chunkIndex++]);
		String invoke = chunks[chunkIndex++];
		List<Character> output = new ArrayList<Character>(n);
		for (int i = 0; i < invoke.length(); i++) {
			Character next = invoke.charAt(i);
			output.add(next);
			while (checkCombination(output, combinations)) {

			}

			while (checkOpposition(output, oppositions, true)) {

			}

		}
		checkOpposition(output, oppositions, false);

		return output.toString();
	}

	private static boolean checkCombination(List<Character> output, Map<String, Character> combinations) {
		if (output.size() > 1) {
			Character previous = output.get(output.size() - 2);
			Character next = output.get(output.size() - 1);
			String topOfStack = new String(new char[] { previous.charValue(), next.charValue() });
			Character combination = combinations.get(topOfStack);
			if (combination != null) {
				output.set(output.size() - 2, combination);
				output.remove(output.size() - 1);
				return true;
			}
		}
		return false;
	}

	private static boolean checkOpposition(List<Character> output, Map<Character, Character> oppositions,
			boolean doNotUseLast) {
		for (int i = 0; i < (doNotUseLast ? output.size() - 1 : output.size()); i++) {
			Character next = output.get(i);
			Character opp = oppositions.get(next);
			if (opp != null && output.contains(opp)) {
				output.clear();
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "magicka";
	}

}
