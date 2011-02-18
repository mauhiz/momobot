package net.mauhiz.contest.codechef;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.Deflater;

import net.mauhiz.contest.AbstractSolver;
import net.mauhiz.contest.Exporter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Manifest.Attribute;
import org.apache.tools.ant.taskdefs.ManifestException;

public class CodechefJarExporter extends Exporter {
	static void antJar(File jarFile, Class<?> mainClass, File tmpFolder) throws ManifestException {

		org.apache.tools.ant.taskdefs.Manifest manTask = new org.apache.tools.ant.taskdefs.Manifest();
		Attribute att = new Attribute();
		att.setName(Attributes.Name.MAIN_CLASS.toString());
		att.setValue(mainClass.getName());
		manTask.addConfiguredAttribute(att);

		Project project = new Project();

		Jar jar = new Jar();
		jar.setDestFile(jarFile);
		jar.setBasedir(tmpFolder);
		jar.addConfiguredManifest(manTask);
		jar.setProject(project);
		jar.execute();
	}

	static Manifest createManifest(File tmpFolder, Class<?> mainClass) {
		Manifest maniFile = new Manifest();
		Attributes main = maniFile.getMainAttributes();
		main.put(Attributes.Name.MANIFEST_VERSION, "1.0");
		main.put(Attributes.Name.MAIN_CLASS, mainClass.getName());
		return maniFile;
	}

	/**
	 * @param startingClasses the first one contains the main
	 */
	static File export(String name, Class<?>[] startingClasses) throws IOException, ManifestException {
		FileUtils.forceMkdir(BUILD_FOLDER);

		File tmpFolder = new File(BUILD_FOLDER, name);

		if (tmpFolder.exists()) {
			FileUtils.cleanDirectory(tmpFolder);
		}

		// add all useful classes
		File classesDir = new File(BUILD_FOLDER, "classes");
		copyUsefulClassFiles(startingClasses, classesDir, tmpFolder);
		File jar = new File(BUILD_FOLDER, name + ".jar");
		FileUtils.forceDelete(jar);

		//		javaJar(jar, startingClasses[0], tmpFolder);
		antJar(jar, startingClasses[0], tmpFolder);

		return jar;
	}

	/**
	 * Doesnt work, I'll be damned if I know why
	 */
	static void javaJar(File jarFile, Class<?> mainClass, File tmpFolder) throws IOException {

		Manifest man = createManifest(tmpFolder, mainClass);

		// pack it tighlty
		JarOutputStream jarOutput = new JarOutputStream(new FileOutputStream(jarFile), man);

		try {
			jarOutput.setLevel(Deflater.BEST_COMPRESSION);
			Collection<File> files = FileUtils.listFiles(tmpFolder, new String[] { "class" }, true);

			for (File file : files) {
				String entName = StringUtils.substringAfter(file.getAbsolutePath(), tmpFolder.getAbsolutePath());
				entName = StringUtils.stripStart(entName, File.separator);
				JarEntry entry = new JarEntry(entName);
				byte[] data = FileUtils.readFileToByteArray(file);
				jarOutput.putNextEntry(entry);
				jarOutput.write(data);
				jarOutput.closeEntry();
			}
			jarOutput.flush();
		} finally {
			jarOutput.close();
		}
	}

	public static void main(String... args) throws Exception {
		List<Class<?>> startingClasses = new ArrayList<Class<?>>(args.length);

		for (String className : args) {
			startingClasses.add(Class.forName(className));
		}
		Class<? extends AbstractSolver> mainClass = (Class<? extends AbstractSolver>) startingClasses.get(0);
		AbstractSolver instance = mainClass.newInstance();
		String name = instance.getName();
		export(name, startingClasses.toArray(new Class<?>[args.length]));
	}
}
