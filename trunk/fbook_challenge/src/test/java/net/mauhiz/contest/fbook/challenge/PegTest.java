package net.mauhiz.contest.fbook.challenge;

import java.io.IOException;

import junit.framework.Assert;

import net.mauhiz.contest.facebook.challenge.Peg;

import org.junit.Test;

public class PegTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new Peg().runTest());
	}
}
