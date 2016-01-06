package com.amithkoujalgi.downloadmanager.entry;

import com.google.gson.Gson;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Route;
import spark.Spark;

public class Entry {
	public static void main(String[] args) {
		Spark.port(9999);
		Spark.staticFileLocation("/www");
		Spark.get("/hello", "application/json", new Route() {
			public Object handle(Request request, Response response) throws Exception {
				return "hello";
			}
		}, new JsonTransformer());
		Spark.after(new Filter() {
			public void handle(Request request, Response response) throws Exception {
			//	response.header("Content-Encoding", "gzip");
			}
		});
	}
}

class JsonTransformer implements ResponseTransformer {
	private Gson gson = new Gson();

	public String render(Object model) throws Exception {
		return gson.toJson(model);
	}
}