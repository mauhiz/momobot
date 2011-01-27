package net.mauhiz.contest.facebook.challenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.mauhiz.contest.LineChunkSolver;

/**
Studious Student
You've been given a list of words to study and memorize.
Being a diligent student of language and the arts,
you've decided to not study them at all and instead make up pointless games based on them.

One game you've come up with is to see how you can concatenate the words
to generate the lexicographically lowest possible string.

Input
As input for playing this game you will receive a text file containing an integer N,
the number of word sets you need to play your game against.
This will be followed by N word sets, each starting with an integer M, the number of words in the set,
followed by M words.
All tokens in the input will be separated by some whitespace and, aside from N and M,
will consist entirely of lowercase letters.

Output
Your submission should contain the lexicographically shortest strings for each corresponding word set, one per line and in order.

Constraints
1 <= N <= 100
1 <= M <= 9
1 <= all word lengths <= 10

 * @author mauhiz
 *
 */
public class Lexic extends LineChunkSolver {

	@Override
	protected String doProblem(String[] toks) {
		List<String> words = new ArrayList<String>(toks.length - 1);
		for (int i = 1; i < toks.length; i++) {
			words.add(toks[i]);
		}
		Collections.sort(words);
		result = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
		perm1(words);
		return result;
	}

	private String result;

	// print N! permutation of the characters of the string s (in order)
	private void perm1(List<String> s) {
		perm1("", s);
	}

	private void perm1(String upto, List<String> s) {
		int N = s.size();
		if (N == 0) { // finished emptying it
			if (upto.compareTo(result) < 0) {
				result = upto;
			}
		} else {
			for (int i = 0; i < N; i++) {
				perm1(upto + s.get(i), subList(s, 0, i, i + 1, N));
			}
		}
	}

	private List<String> subList(List<String> source, int start1, int end1, int start2, int end2) {
		List<String> res = new ArrayList<String>(end1 - start1 + end2 - start2 + 1);
		for (int i = start1; i < end1; i++) {
			res.add(source.get(i));
		}
		for (int i = start2; i < end2; i++) {
			res.add(source.get(i));
		}
		return res;
	}

	@Override
	public String getName() {
		return "lexic";
	}

	public static void main(String... args) throws IOException {
		new Lexic().run(args);
	}
}
