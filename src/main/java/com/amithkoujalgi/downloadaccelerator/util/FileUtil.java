package com.amithkoujalgi.downloadaccelerator.util;

import java.io.File;

public class FileUtil {
	public static void rename(String src, String dest) {
		File srcFl = new File(src);
		File destFl = new File(dest);
		srcFl.renameTo(destFl);
	}
}
