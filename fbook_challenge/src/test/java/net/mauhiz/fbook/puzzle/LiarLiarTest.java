package net.mauhiz.fbook.puzzle;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class LiarLiarTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new LiarLiar().runTest());
	}
}
