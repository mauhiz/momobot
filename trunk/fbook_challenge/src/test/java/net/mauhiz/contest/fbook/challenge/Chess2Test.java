package net.mauhiz.contest.fbook.challenge;

import java.io.IOException;

import net.mauhiz.contest.AbstractTester;
import net.mauhiz.contest.facebook.challenge.Chess2;

import org.junit.Test;

public class Chess2Test extends AbstractTester {
	@Test
	public void doTest() throws IOException {
		runTest(new Chess2());
	}
}
