package net.mauhiz.contest;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;

public abstract class AbstractTester {

	public void runTest(AbstractSolver s) throws IOException {
		File rsFolder = new File(AbstractSolver.PROJECT_FOLDER, "src/test/resources/");
		File in = new File(rsFolder, s.getName() + "_test.txt");
		File out = new File(rsFolder, s.getName() + "_test.out.txt");

		s.runOnFiles(in, out);

		File solution = new File(rsFolder, s.getName() + "_solution.txt");
		String expected = FileUtils.readFileToString(solution);
	
		String actual = FileUtils.readFileToString(out);

		Assert.assertEquals(expected, actual);
	}
	
	public abstract void doTest() throws IOException;
}
