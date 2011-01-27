package net.mauhiz.contest.fbook.challenge;

import java.io.IOException;

import net.mauhiz.contest.AbstractTester;
import net.mauhiz.contest.facebook.challenge.Peg;

import org.junit.Test;

public class PegTest extends AbstractTester {
	@Test
	public void doTest() throws IOException {
		runTest(new Peg());
	}
}
