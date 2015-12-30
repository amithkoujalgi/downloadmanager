package com.amithkoujalgi.downloadaccelerator.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {
	public static void print(Object o) {
		System.out.println(stringify(o));
	}

	public static <T> T parse(String s, Class<T> c) {
		return new Gson().fromJson(s, c);
	}

	public static String stringify(Object o) {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(o);
	}

}
