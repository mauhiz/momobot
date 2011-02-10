package net.mauhiz.contest.facebook.puzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.StringTokenizer;

import net.mauhiz.contest.facebook.ExportedRunnable;

public class Breathalyzer extends ExportedRunnable {
	static class DictionaryLoader {

		Collection<String> dict = new ArrayList<String>(178700);

		private void run(File source) throws IOException {
			long start = System.currentTimeMillis();
			BufferedReader br = new BufferedReader(new FileReader(source), 2000000);
			try {
				while (true) {
					String line = br.readLine();
					if (line == null) {
						break;
					} else if (line.length() > 0) {
						dict.add(line);
					}
				}
			} finally {
				br.close();
			}
			LOG.info("Dictionary loaded in " + (System.currentTimeMillis() - start) + " ms");
		}

		protected void runLocal() throws IOException {
			File dictFile = new File(PROJECT_FOLDER, "src/main/resources/breathalyzer_dict.txt");
			run(dictFile);
		}

		protected void runOnTarget(String... args) throws IOException {
			File dictFile = new File("/var/tmp/twl06.txt");
			run(dictFile);
		}
	}

	/**
	 * Inspired from commons-lang/StringUtils
	 */
	public static int getLevenshteinDistance(String str1, String str2, int stopIfOver) {
		int len1 = str1.length(); // length of str1
		int len2 = str2.length(); // length of str2

		if (len1 == 0) {
			return len2;

		} else if (len2 == 0) {
			return len1;

		} else if (len1 > len2) { // swap the input strings to consume less memory
			return getLevenshteinDistance(str2, str1, stopIfOver);

		} else if (len2 >= len1 + stopIfOver) { // OPT: this distance is hopeless
			return len1;

		} else if (stopIfOver == 1) {
			if (str1.charAt(0) != str2.charAt(0)) {
				return stopIfOver; // won't do better
			}
		}

		return levDist(str1.toCharArray(), str2.toCharArray());
	}

	private static int levDist(char[] str1, char[] str2) {
		int[] previous = new int[str1.length + 1]; //'previous' cost array, horizontally

		for (int i = 0; i <= str1.length; i++) {
			previous[i] = i;
		}

		int[] costArray = new int[str1.length + 1]; // cost array, horizontally

		for (int j = 0; j < str2.length; j++) {
			costArray[0] = j + 1;

			for (int i = 0; i < str1.length; i++) {
				int cost = str1[i] == str2[j] ? previous[i] : previous[i] + 1;
				// minimum of cell to the left+1, to the top+1, diagonally left and up +cost
				costArray[i + 1] = min(costArray[i] + 1, previous[i + 1] + 1, cost);
			}

			// copy current distance counts to 'previous row' distance counts
			int[] swap = previous;
			previous = costArray;
			costArray = swap;
		}

		// our last action in the above loop was to switch costArray and previous,
		// so previous now actually has the most recent cost counts
		return previous[str1.length];
	}

	public static void main(String... args) throws IOException {
		new Breathalyzer().run(args);
	}

	/**
	 * supposes args.length > 0
	 */
	private static int min(int... args) {
		int min = Integer.MAX_VALUE;
		for (int arg : args) {
			if (arg < min) {
				min = arg;
			}
		}
		return min;
	}

	private final DictionaryLoader dl = new DictionaryLoader();

	@Override
	public String getName() {
		return "breathalyzer";
	}

	@Override
	protected Class<?>[] getStartingClasses() {
		return new Class<?>[] { getClass(), DictionaryLoader.class };
	}

	@Override
	protected void process(BufferedReader input, PrintWriter output) throws IOException {
		int totalChanges = 0;
		for (StringTokenizer toks = new StringTokenizer(input.readLine()); toks.hasMoreElements();) {
			long start = System.currentTimeMillis();
			String tok = toks.nextToken().toUpperCase(Locale.ENGLISH);
			int minChanges = tok.length();
			for (String nextWord : dl.dict) {
				int dist = getLevenshteinDistance(tok, nextWord, minChanges);
				if (dist < minChanges) {
					minChanges = dist;
				}
			}
			LOG.info("Word " + tok + " (" + minChanges + ") processed in " + (System.currentTimeMillis() - start)
					+ " ms");
			totalChanges += minChanges;
		}
		output.println(totalChanges);
	}

	@Override
	protected void run(String... args) throws IOException {
		if (args.length > 0) { // real mode
			dl.runOnTarget();
		} // else : pack mode
		super.run(args);
	}

	@Override
	protected void runOnFiles(File in, File out) throws IOException {
		dl.runLocal();
		super.runOnFiles(in, out); // test mode
	}
}
