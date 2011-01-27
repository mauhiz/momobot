package net.mauhiz.contest.util;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author mauhiz
 *
 * @param <C> an object which can be translated into a list
 */
public abstract class AbstractPermutationGenerator<C> implements Iterator<C> {

	private static BigInteger factorial(int n) {
		BigInteger f = BigInteger.ONE;
		for (int i = 2; i <= n; i++) {
			f = f.multiply(BigInteger.valueOf(i));
		}
		return f;
	}

	private BigInteger counter = BigInteger.ZERO;
	protected final int[] curr; // current permutation (mutable)
	protected final C data;
	protected final int dataLength; // numbers of items to permute

	private final BigInteger totalPerms;

	public AbstractPermutationGenerator(C data) {
		this.data = data;
		dataLength = getDataLength();
		totalPerms = factorial(dataLength);
		curr = new int[dataLength];
		for (int i = 0; i < dataLength; i++) {
			curr[i] = i;
		}
	}

	protected abstract int getDataLength();

	public boolean hasNext() {
		return counter.compareTo(totalPerms) == -1;
	}

	public C next() {
		counter = counter.add(BigInteger.ONE);
		if (BigInteger.ONE.equals(counter)) {
			return data;

		} else if (counter.compareTo(totalPerms) == 1) {
			throw new NoSuchElementException();
		}

		int i = dataLength - 1;

		while (curr[i - 1] >= curr[i]) {
			i--;
		}

		int j = dataLength;

		while (curr[j - 1] <= curr[i - 1]) {
			j--;
		}

		swap(i - 1, j - 1);

		i++;
		j = dataLength;

		while (i < j) {
			swap(i - 1, j - 1);
			i++;
			j--;
		}

		return translateCurrToPerm();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	private void swap(int idx1, int idx2) {
		int tmp = curr[idx1];
		curr[idx1] = curr[idx2];
		curr[idx2] = tmp;
	}

	public BigInteger totalPermutations() {
		return totalPerms;
	}

	protected abstract C translateCurrToPerm();
}
