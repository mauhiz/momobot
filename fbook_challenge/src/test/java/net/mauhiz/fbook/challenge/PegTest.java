package net.mauhiz.fbook.challenge;

import java.io.IOException;

import junit.framework.Assert;

import net.mauhiz.fbook.challenge.Peg;

import org.junit.Test;

public class PegTest {
	@Test
	public void doTest() throws IOException {
		Assert.assertTrue(new Peg().runTest());
	}
}
