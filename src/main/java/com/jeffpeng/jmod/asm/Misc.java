package com.jeffpeng.jmod.asm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Misc {
	public static File[] getJarsHook() {

		List<File> files = new ArrayList<File>();
		getJarsWalker(new File("mods"), files);
		return files.toArray(new File[files.size()]);
	}
	
	private static void getJarsWalker(File base, List<File> files) {
		File[] rawFiles = base.listFiles();
		for (File entry : rawFiles) {
			if (entry.isDirectory() && entry.toPath().endsWith("1.7.10"))
				getJarsWalker(entry, files);
			else if (entry.toPath().endsWith(".jar") || entry.toPath().endsWith(".zip"))
				files.add(entry);
		}
	}
}
