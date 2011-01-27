package net.mauhiz.contest.util;

import java.util.ArrayList;
import java.util.List;

public class ListPermutationGenerator<T> extends AbstractPermutationGenerator<List<T>> {

	public ListPermutationGenerator(List<T> perm) {
		super(perm);
	}

	@Override
	protected int getDataLength() {
		return data.size();
	}

	@Override
	protected List<T> translateCurrToPerm() {
		List<T> p = new ArrayList<T>(dataLength);

		for (int cur : curr) {
			p.add(data.get(cur));
		}

		return p;
	}
}
