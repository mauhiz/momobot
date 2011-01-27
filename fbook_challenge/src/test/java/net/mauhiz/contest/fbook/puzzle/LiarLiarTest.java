package net.mauhiz.contest.fbook.puzzle;

import java.io.IOException;

import junit.framework.Assert;

import net.mauhiz.contest.facebook.puzzle.LiarLiar;

import org.junit.Test;

public class LiarLiarTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new LiarLiar().runTest());
	}
}
