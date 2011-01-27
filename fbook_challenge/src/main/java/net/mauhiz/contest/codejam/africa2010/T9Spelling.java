package net.mauhiz.contest.codejam.africa2010;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.contest.codejam.CodejamLineSolver;

/**
 * @author mauhiz
 *
 */
public class T9Spelling extends CodejamLineSolver {

	static class T9Sequence {
		char[] sequence;

		char target;
		public T9Sequence(char target, char... sequence) {
			this.target = target;
			this.sequence = sequence;
		}

		char getFirst() {
			return sequence[0];
		}
		
		char getLast() {
			return sequence[sequence.length - 1];
		}
	}

	static final Map<Character, T9Sequence> t9data = new HashMap<Character, T9Sequence>('z' - 'a' + 2);

	static { // init
		char key = '0';
		init(' ', key, 1);
		
		for (int i = 0; i < 3; i++) {
			init((char) ('a' + i), (char) (key + 2), i + 1);
			init((char) ('d' + i), (char) (key + 3), i + 1);
			init((char) ('g' + i), (char) (key + 4), i + 1);
			init((char) ('j' + i), (char) (key + 5), i + 1);
			init((char) ('m' + i), (char) (key + 6), i + 1);
			init((char) ('p' + i), (char) (key + 7), i + 1);
			init((char) ('t' + i), (char) (key + 8), i + 1);
			init((char) ('w' + i), (char) (key + 9), i + 1);
		}

		init('s', '7', 4);
		init('z', '9', 4);
	}

	static String convert(char[] problemLine) {
		StringBuilder result = new StringBuilder();
		Character previous = null;
		for (char c : problemLine) {
			T9Sequence t9equiv = t9data.get(Character.valueOf(c));
			
			if (previous != null && previous.charValue() == t9equiv.getFirst()){
				result.append(' ');
			}
			result.append(t9equiv.sequence);
			previous = Character.valueOf(t9equiv.getLast());
		}
		return result.toString();
	}

	private static void init(char target, char key, int n) {
		char[] sequence = new char[n];
		for (int i = 0; i < n; i++) {
			sequence[i] = key;
		}
		T9Sequence seq = new T9Sequence(target, sequence);
		t9data.put(Character.valueOf(target), seq);
	}

	public static void main(String... args) throws IOException {
		new T9Spelling().run(args);
	}

	protected String doJamProblem(String problemLine) {
		return convert(problemLine.toCharArray());
	}

	@Override
	public String getName() {
		return "t9";
	}
}
