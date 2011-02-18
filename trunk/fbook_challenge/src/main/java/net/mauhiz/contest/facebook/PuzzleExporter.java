package net.mauhiz.contest.facebook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mauhiz.contest.Exporter;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class PuzzleExporter extends Exporter {

	/**
	 * @param startingClasses the first one contains the main
	 */
	public static File export(String name, Class<?>[] startingClasses) throws IOException {
		FileUtils.forceMkdir(BUILD_FOLDER);

		File tmpFolder = new File(BUILD_FOLDER, name);
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
		File classesDir = new File(BUILD_FOLDER, "classes");
		File bin = new File(tmpFolder, "bin");
		filesToKeep.addAll(copyUsefulClassFiles(startingClasses, classesDir, bin));

		// pack it tighlty
		File zip = new File(BUILD_FOLDER, name + ".tar");
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

}
