package net.mauhiz.fbook.puzzle;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * @author mauhiz
 */
public abstract class ExportedRunnable extends FbookPuzzle {
	
	protected static final Logger LOG = Logger.getLogger("fbook");

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
			
		} catch (ClassNotFoundException e) {
			LOG.severe(e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			LOG.severe(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			LOG.severe(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			LOG.severe(e.getTargetException().getLocalizedMessage());
		}
	}

	protected Class<?>[] getStartingClasses() {
		return new Class<?>[] { getClass() };
	}
}
