package net.mauhiz.contest.fbook.challenge;

import java.io.IOException;

import net.mauhiz.contest.AbstractTester;
import net.mauhiz.contest.facebook.challenge.Lexic;

import org.junit.Test;

public class LexicTest extends AbstractTester {
	@Test
	public void doTest() throws IOException {
		runTest(new Lexic());
	}
}
