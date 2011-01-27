package net.mauhiz.contest.codejam;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class AlienNumbersTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new AlienNumbers().runTest());
	}
}
