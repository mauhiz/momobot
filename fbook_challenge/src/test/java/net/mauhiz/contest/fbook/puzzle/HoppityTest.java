package net.mauhiz.contest.fbook.puzzle;

import java.io.IOException;

import junit.framework.Assert;

import net.mauhiz.contest.facebook.puzzle.Hoppity;

import org.junit.Test;

public class HoppityTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new Hoppity().runTest());
	}
}
