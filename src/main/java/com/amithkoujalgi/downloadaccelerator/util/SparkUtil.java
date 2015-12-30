package com.amithkoujalgi.downloadaccelerator.util;

public class SparkUtil {
	public static <T> T getBody(String bodyJson, Class<T> c) {
		return JSONUtil.parse(bodyJson, c);
	}
}
