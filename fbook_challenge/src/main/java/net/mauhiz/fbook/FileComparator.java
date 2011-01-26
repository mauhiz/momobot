package net.mauhiz.fbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileComparator {

	public static boolean isContentEquals(File o1, File o2) throws IOException {
		BufferedReader r1 = new BufferedReader(new FileReader(o1));
		try {
			BufferedReader r2 = new BufferedReader(new FileReader(o2));
			try {
				while (true) {
					String l1 = r1.readLine();
					String l2 = r2.readLine();
					if (l1 == null && l2 == null) {
						return true;
					} else if (l1 == null || !l1.equals(l2)) {
						return false;
					}
				}
			} finally {
				r2.close();
			}
		} finally {
			r1.close();
		}
	}

}
