package net.mauhiz.fbook.challenge;

import java.io.IOException;

import junit.framework.Assert;

import net.mauhiz.fbook.challenge.DoubleSquares;

import org.junit.Test;

public class DoubleSquaresTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new DoubleSquares().runTest());
	}
}
