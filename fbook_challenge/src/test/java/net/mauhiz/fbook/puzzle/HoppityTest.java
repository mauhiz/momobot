package net.mauhiz.fbook.puzzle;

import java.io.IOException;

import junit.framework.Assert;

import net.mauhiz.fbook.puzzle.Hoppity;

import org.junit.Test;

public class HoppityTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new Hoppity().runTest());
	}
}
