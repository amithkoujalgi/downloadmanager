/* 
SOURCE USED SHOULD CARRY AUTHOR CREDITS
AUTHOR:MAHESH KAREKAR
EMAIL:mahesh_gk@lycos.com
FREEWARE DISTRIBUTE FREELY
*/
package downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

public class SubDownload extends Thread {

	public String SubDownloadId;
	public String FileLoc;
	public long FileStartPos;
	public long FileEndPos;
	public long BytesDownloaded;
	public byte Buffer[];
	public int Complete = 0;
	boolean Aborted = false;

	public SubDownload(String aSubDownloadId, String aFileLoc, long aFileStartPos, long aFileEndPos, int aBufferSize) {

		FileLoc = aFileLoc;
		FileStartPos = aFileStartPos;
		FileEndPos = aFileEndPos;
		Buffer = new byte[1024 * aBufferSize];
		BytesDownloaded = 0;
		SubDownloadId = aSubDownloadId;
		Complete = 0;

	}

	public int SubDownloadStart() {
		return 1;
	}

	public void run() {

		try {

			URL url = new URL(FileLoc);
			URLConnection uc = url.openConnection();
			BufferedInputStream instream;
			uc.setRequestProperty("Range", "bytes=" + FileStartPos + "-" + FileEndPos);
			instream = new BufferedInputStream(uc.getInputStream());

			int li_bytesRead;
			File f = new File(SubDownloadId);

			RandomAccessFile raf = new RandomAccessFile(f, "rw");

			while (BytesDownloaded < (FileEndPos - FileStartPos)) {
				li_bytesRead = instream.read(Buffer);
				raf.write(Buffer, 0, li_bytesRead);
				BytesDownloaded = BytesDownloaded + li_bytesRead;
			}

			// System.out.println("BytesDownloaded: " + BytesDownloaded);
			instream.close();
			raf.close();

			// Finished Downloading
			Complete = 1;

		} catch (Exception e) {
		}

	}

}