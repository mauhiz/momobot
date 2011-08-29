package net.mauhiz.contest.codejam.round1a2011;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mauhiz.contest.codejam.CodejamMultipleSolver;
import net.mauhiz.contest.util.ListPermutationGenerator;

public class Pseudominion extends CodejamMultipleSolver {

	public static void main(String... args) throws IOException {
		new Pseudominion().run(args);
	}

	static class Card {

		Card(BufferedReader input, boolean deck, int index) throws IOException {
			String[] cst = input.readLine().split(" ");
			c = Integer.parseInt(cst[0]);
			s = Integer.parseInt(cst[1]);
			t = Integer.parseInt(cst[2]);
			this.deck = deck;
			this.index = index;
		}

		final int index;
		final int c;
		final int s;
		final int t;
		final boolean deck;

		@Override
		public String toString() {
			return Integer.toString(index);
		}
	}

	@Override
	public String getName() {
		return "pseudominion";
	}

	@Override
	protected String doJamProblem(BufferedReader input) throws IOException {
		int n = Integer.parseInt(input.readLine());
		List<Card> all = new ArrayList<Card>(n);
		{
			int cardIndex = 1;
			for (int i = 0; i < n; i++) {
				all.add(new Card(input, false, cardIndex++));
			}
			int m = Integer.parseInt(input.readLine());
			for (int i = 0; i < m; i++) {
				all.add(new Card(input, true, cardIndex++));
			}
		}

		int maxScore = -1;

		for (ListPermutationGenerator<Card> permutationGenerator = new ListPermutationGenerator<Card>(all); permutationGenerator
				.hasNext();) {
			List<Card> nextPerm = permutationGenerator.next();
			int score = nextTry(n, nextPerm);
			if (score > maxScore) {
				maxScore = score;
			}
		}

		// largest score
		return Integer.toString(maxScore);
	}

	private int nextTry(int n, List<Card> all) {
		int pioched = 0;
		int score = 0;
		int maxScore = 0;
		int turns = 1;
		for (int i = 0; i < all.size(); i++) {
			if (turns == 0) {
				break;
			}
			Card card = all.get(i);
			turns--;
			if (card.deck) {
				// invalid play (not pioched yet)
				if (pioched + n >= card.index) {
					// ok
				} else {
					return -1;
				}
			}
			pioched += card.c;
			score += card.s;
			maxScore = Math.max(score, maxScore);
			turns += card.t;
		}
		return maxScore;
	}
}
