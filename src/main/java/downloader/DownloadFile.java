/* 
SOURCE USED SHOULD CARRY AUTHOR CREDITS
AUTHOR:MAHESH KAREKAR
EMAIL:mahesh_gk@lycos.com
FREEWARE DISTRIBUTE FREELY
*/
package downloader;
import java.net.*;
import java.text.*;


public class DownloadFile extends Thread{
	/* Attributes */
	public String FileLoc;     		//File URL 
	public String FilePath;
	public long FileSize;	 	    //File Size
	public long BytesDownloaded;   //Bytes Downloaded
	public int TotConnections;       //Total Connections
	public int BufferSize;			//Buffer Size
	public SubDownload sd[];
	public int DownloadID;
	public int Complete;
	public int ActiveSubConn;
	public long StartTime;
	
	public DownloadFile(int aDownloadID,String aFileLoc,int aTotConnections,int aBufferSize){
		
		FileLoc = aFileLoc;
		TotConnections = aTotConnections;
		BufferSize = aBufferSize;
		DownloadID= aDownloadID;
		ActiveSubConn=0;
		
		try{
				URL url = new URL(aFileLoc);
				URLConnection uc = url.openConnection();
				FileSize = uc.getContentLength();
				FilePath = url.getPath();
				//System.out.println("Filesize: " + FileSize);
			}catch(Exception e){
			}
			
				
		};
		
		
		public String getBytesDownloaded()
		{
		return String.valueOf(BytesDownloaded);
		}	
	
		public String getFileSize()
		{
		return String.valueOf(FileSize);
		}	
		
	public String getTimeForDownload(){ 
		long time_elapsed,required_time;
		time_elapsed = System.currentTimeMillis() - StartTime;
		if (BytesDownloaded > 0)
		required_time = (long)((FileSize * time_elapsed)/BytesDownloaded) - time_elapsed;
		else
		required_time =0;
		
		return " " + millisecondsToString(required_time) + " ";
	
	}
	
	public String millisecondsToString(long time){
		int milliseconds = (int)(time % 1000);
		int seconds = (int)((time/1000) % 60);
		int minutes = (int)((time/60000) % 60);
		int hours = (int)((time/3600000) % 24);

		String millisecondsStr = (milliseconds<10 ? "00" : (milliseconds<100 ? "0" : ""))+milliseconds;
		String secondsStr = (seconds<10 ? "0" : "")+seconds;
		String minutesStr = (minutes<10 ? "0" : "")+minutes;
		String hoursStr = (hours<10 ? "0" : "")+hours;
		
		return new String(hoursStr+":"+minutesStr+":"+secondsStr);
	}
	
	public String getDownloadSpeed(){ 
	
		float current_speed;
	
		if (BytesDownloaded > 0 )
		current_speed = (float)( BytesDownloaded / (System.currentTimeMillis() - StartTime));
		else
		current_speed = 0;
			
		NumberFormat formatter = NumberFormat.getNumberInstance() ;
		formatter.setMaximumFractionDigits(2);
		
		return " " + formatter.format(current_speed) + " KB/s ";
	
	}
	
	public int StartDownload(){ 
					
			int li_conn=0;
			long ld_FStartPos,ld_FEndPos,ld_partsize;
			String partname;
			
			sd = new SubDownload[TotConnections];
			
			//Size of each part to be downloaded
			ld_partsize= (long)(FileSize/TotConnections);
			
			
			for (li_conn=0;li_conn < TotConnections ;li_conn++){
				
					if ( li_conn == (TotConnections - 1))
					{
					ld_FStartPos=li_conn*ld_partsize;
					ld_FEndPos= FileSize;
					}
					else
					{
					ld_FStartPos=li_conn*ld_partsize;
					ld_FEndPos= ld_FStartPos + ld_partsize - 1;
					}
					
					partname = "DFL" +  String.valueOf(DownloadID) + String.valueOf(li_conn) + ".txt";
					//System.out.println(partname);
					//System.out.println("FS: " + ld_FStartPos + "FE:" + ld_FEndPos);
					sd[li_conn] = new SubDownload(partname,FileLoc,ld_FStartPos,ld_FEndPos,BufferSize);
					StartTime=System.currentTimeMillis();
					//Set Thread Priority
					//sd[li_conn].setPriority(Thread.NORM_PRIORITY);
					//Start Thread
					sd[li_conn].start();
					ActiveSubConn = ActiveSubConn + 1;
			}
											
			return li_conn;			 
			}
	
	public int DownloadProgress(){ 
			int pcount=0;
			calcBytesDownloaded();
			if ( BytesDownloaded > 0 && FileSize > 0 )
			pcount = (int)(( BytesDownloaded * 100 ) / FileSize) ;
			return pcount;
		};
		
	public void calcBytesDownloaded(){
		BytesDownloaded=0;	
		for (int li_conn=0;li_conn < TotConnections  ;li_conn++)
		{
			BytesDownloaded=BytesDownloaded + sd[li_conn].BytesDownloaded;
		}
		
		}	
	
	public int isSubDownComplete(int id)
	{
		
		return sd[id].Complete;
	}
	
	public String getSubDownId(int id)
	{
		return sd[id].SubDownloadId;
	}
	
	public void run(){
			if ( FileSize > 0 )
			StartDownload();
		}
	
	
}

