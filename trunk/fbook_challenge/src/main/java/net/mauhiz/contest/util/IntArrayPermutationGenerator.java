package net.mauhiz.contest.util;

public class IntArrayPermutationGenerator extends AbstractPermutationGenerator<int[]> {

	public IntArrayPermutationGenerator(int[] perm) {
		super(perm);
	}

	@Override
	protected int getDataLength() {
		return data.length;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected int[] translateCurrToPerm() {
		int[] p = new int[dataLength];

		for (int i = 0; i < dataLength; i++) {
			p[i] = data[curr[i]];
		}

		return p;
	}

}
