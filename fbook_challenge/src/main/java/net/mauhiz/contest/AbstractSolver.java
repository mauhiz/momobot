package net.mauhiz.contest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractSolver {
	public static final File PROJECT_FOLDER = new File(".");

	protected final Logger log = Logger.getLogger("solver");

	{
		log.setLevel(Level.ALL);
	}

	public abstract String getName();

	protected abstract void process(BufferedReader input, PrintWriter output) throws IOException;

	protected void run(File input, PrintWriter output) throws IOException {
		long start = System.currentTimeMillis();
		BufferedReader reader = new BufferedReader(new FileReader(input));

		try {
			process(reader, output);

		} finally {
			reader.close();
		}
		log.info("Solving completed in " + (System.currentTimeMillis() - start) + "ms");
	}

	protected void run(String... args) throws IOException {
		if (args.length == 0) {
			runDefault(); // run without arguments

		} else {
			File in = new File(args[0]);

			if (args.length == 1) {
				log.setLevel(Level.OFF);
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

	protected void runOnFiles(File in, File out) throws IOException {
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
