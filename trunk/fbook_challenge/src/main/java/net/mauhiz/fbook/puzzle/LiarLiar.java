package net.mauhiz.fbook.puzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class LiarLiar extends ExportedRunnable {

	public static void main(String... args) throws IOException {
		new LiarLiar().run(args);
	}

	@Override
	public String getName() {
		return "liarliar";
	}

	@Override
	protected void process(BufferedReader input, PrintWriter output) throws IOException {
		int m = Integer.parseInt(input.readLine());
		output.println("");
	}

}
