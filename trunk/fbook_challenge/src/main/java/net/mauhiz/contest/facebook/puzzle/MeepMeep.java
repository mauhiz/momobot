package net.mauhiz.contest.facebook.puzzle;

import java.io.IOException;
import java.io.PrintWriter;

import net.mauhiz.contest.facebook.SingleLinePuzzle;

public class MeepMeep extends SingleLinePuzzle {

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
