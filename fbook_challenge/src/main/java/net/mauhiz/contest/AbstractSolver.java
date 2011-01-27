package net.mauhiz.contest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mauhiz
 */
public abstract class AbstractSolver {
	public static final File PROJECT_FOLDER = new File("C:\\Documents and Settings\\user1\\workspace\\fbook_challenge");

	public abstract String getName();

	protected abstract void process(BufferedReader input, PrintWriter output) throws IOException;

	private void run(File input, PrintWriter output) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(input));

		try {
			process(reader, output);

		} finally {
			reader.close();
		}
	}

	protected void run(String... args) throws IOException {
		if (args.length == 0) {
			runDefault();

		} else {
			File in = new File(args[0]);

			if (args.length == 1) {
				PrintWriter pw = new PrintWriter(System.out);
				try {
					run(in, pw);
				} finally {
					pw.close();
				}
				

			} else {
				File out = new File(args[1]);
				runOnFiles(in, out);
			}
		}
	}

	protected void runDefault() throws IOException {
		File rsFolder = new File(PROJECT_FOLDER, "src/main/resources/");
		File in = new File(rsFolder, getName() + ".txt");
		File out = new File(rsFolder, getName() + ".out.txt");

		runOnFiles(in, out);
	}

	void runOnFiles(File in, File out) throws IOException {
		if (out.exists() && !out.delete()) {
			throw new IOException("Could not delete file: " + out);
		}

		PrintWriter writer = new PrintWriter(out);

		try {
			run(in, writer);

		} finally {
			writer.close();
		}
	}
}
