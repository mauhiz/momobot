package net.mauhiz.fbook.puzzle;

import java.io.IOException;
import java.io.PrintWriter;

public class MeepMeep extends ExportedRunnable {

	public static void main(String... args) throws IOException {
		new MeepMeep().run(args);
	}

	@Override
	public String getName() {
		return "meepmeep";
	}
	
	@Override
	protected void doProblem(String problemLine, PrintWriter output) {
		output.println("Meep meep!");
	}
}
