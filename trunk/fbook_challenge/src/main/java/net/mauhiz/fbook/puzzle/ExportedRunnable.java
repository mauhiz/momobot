package net.mauhiz.fbook.puzzle;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author mauhiz
 */
public abstract class ExportedRunnable extends FbookPuzzle {

	@Override
	protected void run(String... args) throws IOException {
		if (args.length == 0) {
			generatePack();
		} else {
			super.run(args);
		}
	}
	
	public void generatePack() {
		try {
			// external dependencies - do not use direct references
			Class<?> exported = Class.forName("net.mauhiz.fbook.puzzle.PuzzleExporter");
			Method m = exported.getMethod("export", String.class, Class[].class);
			m.invoke(null, getName(), getStartingClasses());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Class<?>[] getStartingClasses() {
		return new Class<?>[] { getClass() };
	}
}
