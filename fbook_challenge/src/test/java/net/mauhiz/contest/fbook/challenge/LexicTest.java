package net.mauhiz.contest.fbook.challenge;

import java.io.IOException;

import junit.framework.Assert;

import net.mauhiz.contest.facebook.challenge.Lexic;

import org.junit.Test;

public class LexicTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new Lexic().runTest());
	}
}
