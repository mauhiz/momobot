package net.mauhiz.contest.codejam.africa2010;

import java.io.BufferedReader;
import java.io.IOException;

import net.mauhiz.contest.codejam.CodejamMultipleSolver;

/**
 * @author mauhiz
 */
public class StoreCredit extends CodejamMultipleSolver {

	public static void main(String... args) throws IOException {
		new StoreCredit().run(args);
	}

	@Override
	public String getName() {
		return "store_credit";
	}

	@Override
	protected String doJamProblem(BufferedReader input) throws IOException {
		String creditLine = input.readLine();
		final int credit = Integer.parseInt(creditLine);
		
		String itemsLine = input.readLine();
		int items = Integer.parseInt(itemsLine);
		String productsLine = input.readLine();
		String[] productChunks = productsLine.split(" ");
		int[] products = new int[items];
		
		for (int i = 0; i < items; i++) {
			products[i] = Integer.parseInt(productChunks[i]);
		}
		
		for (int i = 0; i < items - 1; i++) {
			for (int j = i + 1; j < items; j++) {
				if (credit == products[i] + products[j]) {
					return i + 1 + " " + (j + 1);
				}
			}
		}
		
		throw new IllegalArgumentException("No solution");
		
	}
}
