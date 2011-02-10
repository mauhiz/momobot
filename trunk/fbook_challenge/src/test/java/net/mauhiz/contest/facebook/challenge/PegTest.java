package net.mauhiz.contest.facebook.challenge;

import java.io.IOException;

import net.mauhiz.contest.AbstractTester;

import org.junit.Test;

public class PegTest extends AbstractTester {
	@Override
	@Test
	public void doTest() throws IOException {
		runTest(new Peg());
	}
}
