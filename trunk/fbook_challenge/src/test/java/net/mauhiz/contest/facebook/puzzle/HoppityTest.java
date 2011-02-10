package net.mauhiz.contest.facebook.puzzle;

import java.io.IOException;

import net.mauhiz.contest.AbstractTester;

import org.junit.Test;

public class HoppityTest extends AbstractTester {
	@Override
	@Test
	public void doTest() throws IOException {
		runTest(new Hoppity());
	}
}
