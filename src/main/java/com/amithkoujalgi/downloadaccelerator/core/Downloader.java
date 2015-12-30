package com.amithkoujalgi.downloadaccelerator.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.amithkoujalgi.downloadaccelerator.util.FileUtil;
import com.amithkoujalgi.downloadaccelerator.util.HTTPUtil;

public class Downloader {
	private String targetDirectory = System.getProperty("user.home") + File.separator + "Downloads", url;
	private int numThreads;
	private Thread th;

	public Downloader(String targetDirectory, int numThreads, String url) {
		this.targetDirectory = targetDirectory;
		this.numThreads = numThreads;
		this.url = url;
	}

	public void download() throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);

		RemoteFileMetadata metadata = getRemoteFileMetadata(response);

		if (metadata.isMultipartDownloadSupported()) {
			System.out.println("Yay! Download in parts supported!");
			// downloadInChunks();
			downloadAsSinglePart(metadata);
		} else {
			System.out.println("Oh crap! Download in parts ain't supported.");
			downloadAsSinglePart(metadata);
		}
	}

	private void downloadInChunks() {

	}

	private static Thread downloadAsSinglePart(RemoteFileMetadata metadata) throws InterruptedException {
		SinglePartFileDownloadThread t = new SinglePartFileDownloadThread(metadata);
		t.start();
		return t;
	}

	// private void downloadNormally(HttpResponse response, RemoteFileMetadata
	// metadata) throws Exception {
	// HttpEntity entity = response.getEntity();
	// if (entity != null) {
	// // long len = entity.getContentLength();
	// InputStream inputStream = entity.getContent();
	// File file = new File(targetDirectory + File.separator +
	// metadata.getFilename());
	// FileUtils.copyInputStreamToFile(inputStream, file);
	// }
	// }

	private RemoteFileMetadata getRemoteFileMetadata(HttpResponse response) throws Exception {
		HTTPUtil.spitHeaders(response);
		RemoteFileMetadata metadata = new RemoteFileMetadata();

		if (HTTPUtil.hasHeader(response, "Accept-Ranges")) {
			metadata.setMultipartDownloadSupported(true);
		} else {
			metadata.setMultipartDownloadSupported(false);
		}

		String filename = FilenameUtils.getBaseName(url);
		if (filename != null && !filename.trim().isEmpty()) {
			filename = filename.trim();
		}
		String extn = FilenameUtils.getExtension(url);
		if (extn != null && !extn.trim().isEmpty()) {
			filename = filename + "." + extn;
		}
		metadata.setExtension(extn);
		metadata.setFilename(filename);
		metadata.setUrl(url);
		metadata.setDestinationFilePath(targetDirectory + File.separator + filename);
		metadata.setFileSizeBytes(HTTPUtil.getFileSizeBytes(response));
		return metadata;
	}
}

class RemoteFileMetadata {
	private String url, filename, extension, destinationFilePath;

	public String getDestinationFilePath() {
		return destinationFilePath;
	}

	public void setDestinationFilePath(String destinationFilePath) {
		this.destinationFilePath = destinationFilePath;
	}

	private Long fileSizeBytes;
	private boolean isMultipartDownloadSupported;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public long getFileSizeBytes() {
		return fileSizeBytes;
	}

	public void setFileSizeBytes(Long fileSizeBytes) {
		this.fileSizeBytes = fileSizeBytes;
	}

	public boolean isMultipartDownloadSupported() {
		return isMultipartDownloadSupported;
	}

	public void setMultipartDownloadSupported(boolean isMultipartDownloadSupported) {
		this.isMultipartDownloadSupported = isMultipartDownloadSupported;
	}
}

class SinglePartFileDownloadThread extends Thread {
	private String id = UUID.randomUUID().toString();
	private RemoteFileMetadata metadata;
	private long downloadedBytes = 0L, downloadBeginTime = 0;
	private boolean paused = false;

	public SinglePartFileDownloadThread(RemoteFileMetadata metadata) {
		this.metadata = metadata;
	}

	public Long getDownloadedBytes() {
		return downloadedBytes;
	}

	public void pause() {
		this.paused = true;
	}

	public boolean isPaused() {
		return paused;
	}

	public double getDownloadedPercentage() {
		long totalSize = metadata.getFileSizeBytes();
		double perc = ((double) downloadedBytes / (double) totalSize) * 100;
		return perc;
	}

	public long getDownloadBeginTime() {
		return downloadBeginTime;
	}

	public long getElapsedTime() {
		return System.currentTimeMillis() - downloadBeginTime;
	}

	public boolean isComplete() {
		return downloadedBytes == metadata.getFileSizeBytes();
	}

	@Override
	public void run() {
		BufferedInputStream in = null;
		FileOutputStream out = null;
		try {
			URL url = new URL(metadata.getUrl());
			URLConnection conn = url.openConnection();
			int size = conn.getContentLength();
			if (size < 0) {
				System.out.println("Could not get the file size");
			} else {
				System.out.println("File size: " + size);
			}
			in = new BufferedInputStream(url.openStream());
			out = new FileOutputStream(metadata.getDestinationFilePath() + ".part");
			byte data[] = new byte[1024];
			int count;
			double sumCount = 0.0;
			while ((count = in.read(data, 0, 1024)) != -1) {
				out.write(data, 0, count);
				sumCount += count;
				downloadedBytes = (long) sumCount;
				if (size > 0) {
					// System.out.println("Percentace: " + (sumCount / size *
					// 100.0) + "%");
				}
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e3) {
					e3.printStackTrace();
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e4) {
					e4.printStackTrace();
				}
			FileUtil.rename(metadata.getDestinationFilePath() + ".part", metadata.getDestinationFilePath());
		}
	}
}