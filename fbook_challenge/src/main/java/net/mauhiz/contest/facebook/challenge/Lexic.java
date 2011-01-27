package net.mauhiz.contest.facebook.challenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mauhiz.contest.LineChunkSolver;
import net.mauhiz.contest.util.ListPermutationGenerator;

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

		String result = null;
		Iterator<List<String>> perms = new ListPermutationGenerator<String>(words);

		while (perms.hasNext()) {
			List<String> perm = perms.next();
			String j = join(perm);

			if (result == null || result.compareTo(j) > 0) {
				result = j;
			}
		}

		return result;
	}

	public static String join(List<String> items) {
		StringBuilder sb = new StringBuilder(items.size() * 10);
		for (String item : items) {
			sb.append(item);
		}
		return sb.toString();
	}

	@Override
	public String getName() {
		return "lexic";
	}

	public static void main(String... args) throws IOException {
		new Lexic().run(args);
	}
}
