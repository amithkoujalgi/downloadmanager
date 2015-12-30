package com.amithkoujalgi.downloadaccelerator.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class HTTPUtil {
	public static boolean hasHeader(HttpResponse response, String header) {
		for (Header hdr : response.getAllHeaders()) {
			String key = hdr.getName();
			if (key.equals(header)) {
				return true;
			}
		}
		return false;
	}

	public static String getHeader(HttpResponse response, String header) throws Exception {
		Map<String, String> map = new HashMap<>();
		for (Header hdr : response.getAllHeaders()) {
			String key = hdr.getName();
			String val = hdr.getValue();
			map.put(key, val);
			if (key.equals(header)) {
				return val;
			}
		}
		throw new Exception("Header '" + header + "' not found.");
	}

	public static void spitHeaders(HttpResponse response) throws Exception {
		Map<String, String> map = new HashMap<>();
		for (Header hdr : response.getAllHeaders()) {
			String key = hdr.getName();
			String val = hdr.getValue();
			map.put(key, val);
		}
		JSONUtil.print(map);
	}

	public static long getFileSizeBytes(HttpResponse response) throws Exception {
		if (hasHeader(response, "Content-Length")) {
			String fileSize = getHeader(response, "Content-Length");
			return Long.parseLong(fileSize);
		}
		throw new Exception("Couldn't find content size");
	}
}
