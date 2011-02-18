package net.mauhiz.contest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class Exporter {

	public static final File BUILD_FOLDER = new File(AbstractSolver.PROJECT_FOLDER, "target");

	private static void addSuperClasses(Collection<Class<?>> cls, Class<?> class1) {
		if (class1 == null || !cls.add(class1)) { // this part of the tree was already done
			return;
		}

		for (Class<?> iface : class1.getInterfaces()) {
			addSuperClasses(cls, iface);
		}

		addSuperClasses(cls, class1.getSuperclass());
	}

	/**
	 * @return list of copied files
	 * @throws IOException 
	 */
	protected static List<File> copyUsefulClassFiles(Class<?>[] startingClasses, File classesDir, File copyTarget)
			throws IOException {
		List<File> classFiles = new ArrayList<File>();
		for (Class<?> usefulClass : getUsefulClasses(startingClasses)) {
			String canName = usefulClass.getCanonicalName();
			if (canName == null) {
				continue;
			}
			String pack = usefulClass.getPackage().getName();
			String packDirPath = StringUtils.replace(pack, ".", "/");
			File packDir = new File(classesDir, packDirPath);

			if (packDir.exists()) { // if not : part of the JDK, ignore
				String clsInPackageName = StringUtils.substringAfter(canName, pack + ".");
				String clsFileName = StringUtils.replace(clsInPackageName, ".", "$") + ".class";
				File clsFile = new File(packDir, clsFileName);

				if (clsFile.isFile()) {
					File target = new File(copyTarget, packDirPath + "/" + clsFileName);
					FileUtils.copyFile(clsFile, target);
					classFiles.add(target);
				}
			}
		}
		return classFiles;
	}

	protected static Collection<Class<?>> getUsefulClasses(Class<?>[] startingClasses) {
		Set<Class<?>> cls = new HashSet<Class<?>>();
		for (Class<?> clz : startingClasses) {
			addSuperClasses(cls, clz);
		}
		return cls;
	}
}
