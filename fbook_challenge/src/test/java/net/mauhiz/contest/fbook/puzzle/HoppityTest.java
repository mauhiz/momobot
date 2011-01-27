package net.mauhiz.contest.fbook.puzzle;

import java.io.IOException;

import net.mauhiz.contest.AbstractTester;
import net.mauhiz.contest.facebook.puzzle.Hoppity;

import org.junit.Test;

public class HoppityTest extends AbstractTester {
	@Test
	public void doTest() throws IOException {
		runTest(new Hoppity());
	}
}
