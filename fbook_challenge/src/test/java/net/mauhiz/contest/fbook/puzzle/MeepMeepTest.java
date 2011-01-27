package net.mauhiz.contest.fbook.puzzle;

import java.io.IOException;

import junit.framework.Assert;

import net.mauhiz.contest.facebook.puzzle.MeepMeep;

import org.junit.Test;

public class MeepMeepTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new MeepMeep().runTest());
	}
}
