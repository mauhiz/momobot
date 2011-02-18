package net.mauhiz.contest.codechef.easy;

import junit.framework.Assert;

import org.junit.Test;

public class FactorialTest {
	@Test
	public void doTest() {
		Factorial f = new Factorial();
		Assert.assertEquals(14, f.zed(60));
		Assert.assertEquals(2183837, f.zed(8735373));
	}
}
