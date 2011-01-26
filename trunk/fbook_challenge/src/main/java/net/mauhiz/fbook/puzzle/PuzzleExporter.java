package net.mauhiz.fbook.puzzle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mauhiz.fbook.CommandLineClient;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class PuzzleExporter {
	/**
	 * @param startingClasses the first one contains the main
	 */
	public static File export(String name, Class<?>[] startingClasses) throws IOException {
		FileUtils.forceMkdir(SCRIPT_FOLDER);
		
		File tmpFolder = new File(SCRIPT_FOLDER, "tmp-" + name);
		if (tmpFolder.exists()) {
			FileUtils.cleanDirectory(tmpFolder);
		}
		
		// a windows runnable, may come handy
		generateWindowsRunnable(tmpFolder, name, startingClasses[0]);
		
		
		List<File> filesToKeep = new ArrayList<File>();
		
		// put the unix runnable
		File uRunn = generateUnixRunnable(tmpFolder, name, startingClasses[0]);
		filesToKeep.add(uRunn);

		// add all useful classes
		File classesDir = new File(CommandLineClient.PROJECT_FOLDER, "target/classes");
		File bin = new File(tmpFolder, "bin");

		for (Class<?> usefulClass : getUsefulClasses(startingClasses)) {
			String canName = usefulClass.getCanonicalName();
			if (canName != null && canName.indexOf('$') < 0) { // skip internal classes
				String path = StringUtils.replace(canName, ".", "/") + ".class";
				File clsFile = new File(classesDir, path);
				
				if (clsFile.isFile()) { // if not : part of the JDK, ignore
					File target =  new File(bin, path);
					FileUtils.copyFile(clsFile, target);
					filesToKeep.add(target);
				}
			}
		}
		
		// pack it tighlty
		File zip = new File(SCRIPT_FOLDER, name + ".tar");
		TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new FileOutputStream(zip));
		
		try {
			for (File file : filesToKeep) {
				String entName = StringUtils.substringAfter(file.getAbsolutePath(), tmpFolder.getAbsolutePath());
				TarArchiveEntry entry = new TarArchiveEntry(entName);
				entry.setSize(file.length());
				tarOutput.putArchiveEntry(entry);
				byte[] fileData = FileUtils.readFileToByteArray(file);
				tarOutput.write(fileData);
				tarOutput.closeArchiveEntry();
			}
		} finally {
			tarOutput.close();
		}
		
		return zip;
	}
	
	public static final File SCRIPT_FOLDER = new File(CommandLineClient.PROJECT_FOLDER, "src/main/scripts");

	public static File generateUnixRunnable(File tmpFolder, String name, Class<?> main) throws IOException {
		File runn = new File(tmpFolder, name);
		if (runn.exists()) {
			FileUtils.forceDelete(runn);
		}
		String script = "java -classpath .:./bin " + main.getCanonicalName() + " $1";
		FileUtils.writeStringToFile(runn, script);

		return runn;
	}
	
	public static File generateWindowsRunnable(File tmpFolder, String name, Class<?> main) throws IOException {
		File runn = new File(tmpFolder, name + ".bat");
		if (runn.exists()) {
			FileUtils.forceDelete(runn);
		}
		String script = "\"%JRE_HOME%/bin/java\" -classpath .;./bin " + main.getCanonicalName() + " %1";
		FileUtils.writeStringToFile(runn, script);

		return runn;
	}

	private static Collection<Class<?>> getUsefulClasses(Class<?>[] startingClasses) {
		Set<Class<?>> cls = new HashSet<Class<?>>();
		for (Class<?> clz : startingClasses) {
			addSuperClasses(cls, clz);
		}
		return cls;
	}

	private static void addSuperClasses(Collection<Class<?>> cls, Class<?> class1) {
		if (class1 == null || !cls.add(class1)) { // this part of the tree was already done
			return;
		}

		for (Class<?> iface : class1.getInterfaces()) {
			addSuperClasses(cls, iface);
		}

		addSuperClasses(cls, class1.getSuperclass());
	}
}
