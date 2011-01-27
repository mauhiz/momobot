package net.mauhiz.contest.facebook;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import net.mauhiz.contest.AbstractSolver;

/**
 * @author mauhiz
 */
public abstract class ExportedRunnable extends AbstractSolver {
	
	protected static final Logger LOG = Logger.getLogger("fbook");

	@Override
	protected void run(String... args) throws IOException {
		if (args.length == 0) {
			generatePack();
		} else {
			super.run(args);
		}
	}
	
	public File generatePack() {
		try {
			// external dependencies - do not use direct references
			Class<?> exported = Class.forName("net.mauhiz.contest.fbook.PuzzleExporter");
			Method m = exported.getMethod("export", String.class, Class[].class);
			Object result = m.invoke(null, getName(), getStartingClasses());
			
			if (result instanceof File) {
				return (File) result;
			}
			
		} catch (ClassNotFoundException e) {
			LOG.severe(e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			LOG.severe(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			LOG.severe(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			LOG.severe(e.getTargetException().getLocalizedMessage());
		}
		
		return null;
	}

	protected Class<?>[] getStartingClasses() {
		return new Class<?>[] { getClass() };
	}
}
