package net.mauhiz.contest.codejam.round1a2011;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mauhiz.contest.codejam.CodejamMultipleSolver;

import org.apache.commons.lang.StringUtils;

public class KillerWord extends CodejamMultipleSolver {

	public static void main(String... args) throws IOException {
		new KillerWord().run(args);
	}

	@Override
	public String getName() {
		return "killerword";
	}

	@Override
	protected String doJamProblem(BufferedReader input) throws IOException {
		String firstLine = input.readLine();
		String[] chunks = firstLine.split(" ");
		int n = Integer.parseInt(chunks[0]);
		int m = Integer.parseInt(chunks[1]);
		List<String> d = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			d.add(input.readLine());
		}
		List<String> l = new ArrayList<String>(m);
		for (int i = 0; i < m; i++) {
			l.add(input.readLine());
		}

		List<String> w = new ArrayList<String>(m);
		for (int i = 0; i < m; i++) {
			w.add(doProblem(d, l.get(i)));
		}
		return StringUtils.join(w, " ");
	}

	private String doProblem(List<String> d, String l) {
		int maxPoints = -1;
		String ret = null;
		for (String dictWord : d) {
			int newPoints = doTry(new ArrayList<String>(d), dictWord, l);
			if (newPoints > maxPoints) {
				maxPoints = newPoints;
				ret = dictWord;
			}
		}
		return ret;
	}

	private int doTry(List<String> dict, String dictWord, String l) {
		int len = dictWord.length();
		for (Iterator<String> i = dict.iterator(); i.hasNext();) {
			if (i.next().length() != len) {
				i.remove();
			}
		}
		if (dict.size() == 1) {
			return 0;
		}
		int wrongGuesses = 0;
		for (int i = 0; i < l.length(); i++) {
			char nextGuess = l.charAt(i);
			if (!containsAny(nextGuess, dict)) {
				continue;
			}
			List<Integer> indices = indices(dictWord, nextGuess);
			if (indices.isEmpty()) {
				wrongGuesses++;
			}
			updateWithIndices(nextGuess, indices, dict);
			if (dict.size() == 1) {
				break;
			}

		}
		return wrongGuesses;
	}

	private void updateWithIndices(char nextGuess, List<Integer> indices, List<String> dict) {
		for (Iterator<String> i = dict.iterator(); i.hasNext();) {
			if (!isEquals(indices, indices(i.next(), nextGuess))) {
				i.remove();
			}
		}
	}

	private boolean isEquals(List<Integer> indices, List<Integer> indices2) {
		if (indices.size() != indices2.size()) {
			return false;
		}

		for (int i = 0; i < indices.size(); i++) {
			if (indices.get(i).intValue() != indices2.get(i).intValue()) {
				return false;
			}
		}

		return true;
	}

	private List<Integer> indices(String dictWord, char nextGuess) {
		List<Integer> ret = new ArrayList<Integer>();
		for (int i = 0; i < dictWord.length(); i++) {
			if (dictWord.charAt(i) == nextGuess) {
				ret.add(Integer.valueOf(i));
			}
		}
		return ret;
	}

	private boolean containsAny(char nextGuess, List<String> dict) {
		for (String d : dict) {
			if (d.indexOf(nextGuess) >= 0) {
				return true;
			}
		}
		return false;
	}

}
