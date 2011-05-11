package net.mauhiz.contest.codejam.qual2011;

import java.io.IOException;

import org.junit.Test;

import net.mauhiz.contest.AbstractTester;

public class BotTrustTest extends AbstractTester {

	@Test
	@Override
	public void doTest() throws IOException {
		runTest(new BotTrust());
	}

}
