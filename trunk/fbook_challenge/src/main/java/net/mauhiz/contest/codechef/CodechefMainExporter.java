package net.mauhiz.contest.codechef;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mauhiz.contest.AbstractSolver;
import net.mauhiz.contest.Exporter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class CodechefMainExporter extends Exporter {

	/**
	 * @param startingClasses the first one contains the main
	 */
	static void export(String name, Class<?>[] startingClasses) throws IOException {
		FileUtils.forceMkdir(BUILD_FOLDER);

		File tmpFolder = new File(BUILD_FOLDER, name);

		if (tmpFolder.exists()) {
			FileUtils.cleanDirectory(tmpFolder);
		}

		// add all useful classes
		File sourceDir = new File(AbstractSolver.PROJECT_FOLDER, "src/main/java");
		Set<String> imports = new TreeSet<String>();
		List<String> sourceLines = new ArrayList<String>();
		Pattern classDec = Pattern.compile("public([a-z ]*) class ([A-Za-z0-9]+) (.+?)");

		for (Class<?> usefulClass : getUsefulClasses(startingClasses)) {
			String canName = usefulClass.getCanonicalName();
			if (canName == null || canName.indexOf('$') >= 0) {
				continue;
			}

			String pack = usefulClass.getPackage().getName();
			String packDirPath = StringUtils.replace(pack, ".", "/");
			File packDir = new File(sourceDir, packDirPath);

			if (packDir.exists()) {
				String srcInPackageName = StringUtils.substringAfter(canName, pack + ".");
				String srcFileName = srcInPackageName + ".java";
				File srcFile = new File(packDir, srcFileName);

				if (srcFile.isFile()) {
					List<String> srcLines = FileUtils.readLines(srcFile);
					for (String srcLine : srcLines) {
						if (srcLine.startsWith("package ")) {
							continue;
						} else if (srcLine.startsWith("import ")) {
							if (!srcLine.startsWith("import net.mauhiz.")) {
								imports.add(srcLine);
							}
						} else {
							Matcher m = classDec.matcher(srcLine);
							if (m.matches()) {
								sourceLines.add(m.group(1) + " class " + m.group(2) + " " + m.group(3));
							} else {
								sourceLines.add(srcLine);
							}

						}
					}
				}
			}
		}
		sourceLines.add(0, "\r\npublic class Main { public static void main(String[] args) throws Throwable {"
				+ startingClasses[0].getSimpleName() + ".main(args); } }");

		for (String impord : imports) {
			sourceLines.add(0, impord);
		}
		File result = new File(BUILD_FOLDER, name + ".java");
		FileUtils.writeLines(result, sourceLines);
	}

	public static void main(String... args) throws Exception {
		List<Class<?>> startingClasses = new ArrayList<Class<?>>(args.length);

		for (String className : args) {
			startingClasses.add(Class.forName(className));
		}
		Class<?> mainClass = startingClasses.get(0);
		Method getName = mainClass.getMethod("getName");
		String name = (String) getName.invoke(mainClass.newInstance());
		export(name, startingClasses.toArray(new Class<?>[args.length]));
	}
}
