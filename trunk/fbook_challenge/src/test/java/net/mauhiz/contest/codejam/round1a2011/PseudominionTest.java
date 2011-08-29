package net.mauhiz.contest.codejam.round1a2011;

import java.io.IOException;

import net.mauhiz.contest.AbstractTester;

import org.junit.Test;

public class PseudominionTest extends AbstractTester {

	@Test
	@Override
	public void doTest() throws IOException {
		runTest(new Pseudominion());
	}

	@Test
	public void doTest2() throws IOException {
		runTest(new Pseudominion() {
			@Override
			public String getName() {
				return super.getName() + "2";
			}
		});
	}

}
