package net.mauhiz.contest.codejam;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 
 * Problem

The decimal numeral system is composed of ten digits, which we represent as "0123456789"
(the digits in a system are written from lowest to highest). Imagine you have discovered
an alien numeral system composed of some number of digits, which may or may not be the same as those used in decimal.
For example, if the alien numeral system were represented as "oF8",
then the numbers one through ten would be (F, 8, Fo, FF, F8, 8o, 8F, 88, Foo, FoF).
We would like to be able to work with numbers in arbitrary alien systems.
More generally, we want to be able to convert an arbitrary number that's written in one alien system into a second alien system.

Input

The first line of input gives the number of cases, N. N test cases follow. Each case is a line formatted as

alien_number source_language target_language
Each language will be represented by a list of its digits, ordered from lowest to highest value.
No digit will be repeated in any representation, all digits in the alien number will be present in the source language,
and the first digit of the alien number will not be the lowest valued digit of the source language
(in other words, the alien numbers have no leading zeroes). Each digit will either be a number 0-9,
an uppercase or lowercase letter, or one of the following symbols !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~

Output

For each test case, output one line containing "Case #x: " followed by the alien number translated from the source
language to the target language.

Limits

1 ≤ N ≤ 100.

Small dataset

1 ≤ num digits in alien_number ≤ 4,
2 ≤ num digits in source_language ≤ 16,
2 ≤ num digits in target_language ≤ 16.

Large dataset

1 ≤ alien_number (in decimal) ≤ 1000000000,
2 ≤ num digits in source_language ≤ 94,
2 ≤ num digits in target_language ≤ 94.

 * @author mauhiz
 *
 */
public class AlienNumbers extends CodejamMultipleChunkSolver {

	public static void main(String... args) throws IOException {
		new AlienNumbers().run(args);
	}

	@Override
	public String getName() {
		return "alien_numbers";
	}

	@Override
	protected String doJamProblem(String[] chunks) {
		char[] toConvert = chunks[0].toCharArray();
		char[] sourceLanguage = chunks[1].toCharArray();
		char[] targetLanguage = chunks[2].toCharArray();
		return convert(toConvert, sourceLanguage, targetLanguage);
	}
	
	static String convert(char[] toConvert, char[] sourceLanguage, char[] targetLanguage) {
		// convert source to BigInteger
		BigInteger source = BigInteger.valueOf(0);
		
		BigInteger sourceRadix = BigInteger.valueOf(sourceLanguage.length);
		BigInteger pow = BigInteger.valueOf(1);
		for (int i = toConvert.length - 1; i >= 0; i--) {
			int digit = indexOf(sourceLanguage, toConvert[i]);
			source = source.add(pow.multiply(BigInteger.valueOf(digit)));
			pow = pow.multiply(sourceRadix);
		}
		
		// convert BigInteger to target language
		BigInteger targetRadix = BigInteger.valueOf(targetLanguage.length);
		StringBuilder result = new StringBuilder();
		while (true) {
			BigInteger mod = source.mod(targetRadix);
			char digit = targetLanguage[mod.intValue()];
			result.insert(0, digit);
			source = source.subtract(mod);
			if (BigInteger.ZERO.equals(source)) {
				break;
			}
			source = source.divide(targetRadix);
		}
		
		return result.toString();
	}
	
	static int indexOf(char[] data, char sought) {
		for (int i =0; i < data.length; i++) {
			if (sought == data[i]) {
				return i;
			}
		}
		
		throw new IllegalArgumentException(sought + ": invalid digit");
	}

}
