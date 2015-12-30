package com.amithkoujalgi.downloadaccelerator.entry;

import java.io.File;

import com.amithkoujalgi.downloadaccelerator.core.Downloader;
import com.amithkoujalgi.downloadaccelerator.util.SparkUtil;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Entry {
	public static void main(String[] args) throws Exception {
		final String url = "https://dl.google.com/dl/picasa/picasamac39.dmg";
		// String url = "http://www.basavabalaga.org/images/banner01.jpg";
		String targetDir = System.getProperty("user.home") + File.separator + "Downloads";
		new Downloader(targetDir, 1, url).download();

		Spark.post("/add", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				NewDownload dl = SparkUtil.getBody(request.body(), NewDownload.class);
				Downloader d = new Downloader(System.getProperty("user.home") + File.separator + "Downloads", 1,
						url);
				d.download();
				return null;
			}
		});
		Spark.post("/get/:id", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				request.params("id");
				return null;
			}
		});
	}
}

class NewDownload {
	private String url;
	private int numParts;

	public int getNumParts() {
		return numParts;
	}

	public void setNumParts(int numParts) {
		this.numParts = numParts;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}