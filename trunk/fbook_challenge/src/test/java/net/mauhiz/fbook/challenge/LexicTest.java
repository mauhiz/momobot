package net.mauhiz.fbook.challenge;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class LexicTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new Lexic().runTest());
	}
}
