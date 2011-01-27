package net.mauhiz.contest.codejam.africa2010;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.mauhiz.contest.codejam.CodejamLineChunkSolver;

/**
 * @author mauhiz
 */
public class ReverseWords extends CodejamLineChunkSolver {

	public static void main(String... args) throws IOException {
		new ReverseWords().run(args);
	}

	@Override
	public String getName() {
		return "reverse_words";
	}

	@Override
	protected String doJamProblem(String[] chunks) {
		List<String> words = new ArrayList<String>(Arrays.asList(chunks));
		Collections.reverse(words);
		StringBuilder ret = new StringBuilder(10 * words.size());
		for (String w : words) {
			ret.append(' ').append(w);
		}
		return ret.substring(1);
	}
}
