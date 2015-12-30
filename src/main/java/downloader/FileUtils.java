/* 
SOURCE USED SHOULD CARRY AUTHOR CREDITS
AUTHOR:MAHESH KAREKAR
EMAIL:mahesh_gk@lycos.com
FREEWARE DISTRIBUTE FREELY
*/
	package downloader;
	import java.io.*;
	import java.net.*;
	import java.util.*;


	
	/* Concatenate Files */
	public class FileUtils{
	
	int concatcomplete=0;
	
	public void concat(String args[],String au)
	{
	ListOfFiles mylist = new ListOfFiles(args);

       try {
       	
       	String outfile=getFilename(au);
       	//Check if Download directory exist 
       	boolean exists = (new File("filename")).exists();
		    if (!exists) {
		    	boolean success = (new File("Downloaded_Files")).mkdir();       	
    		}

       	
       	File f = new File("Downloaded_Files/" + outfile);
		RandomAccessFile raf = new RandomAccessFile(f, "rw");
			
         SequenceInputStream in = new SequenceInputStream( mylist );
         int c;
         byte buffer[] = new byte[1024*4];
         
         while ( ( c = in.read(buffer) ) != -1 ) {
           raf.write(buffer,0,c);;
         }
         in.close();
         raf.close();
       } catch ( IOException e ) {
         System.err.println( "Concatenate: " + e );
      }

	concatcomplete=1;
	}
	
	
	/* GetFileName */
		public static String getFilename(String pathfilename) {
    		int p = Math.max(pathfilename.lastIndexOf('/'), pathfilename.lastIndexOf('\\'));
   			if (p >= 0) {
     		return pathfilename.substring(p + 1);
   				 } else {
     			 return pathfilename;
    			}
  			}
  			
  			
  	/* File Delete */
  	
  	public static void delete(String filename) {
    // Create a File object to represent the filename
    File f = new File(filename);
    // Make sure the file or directory exists and isn't write protected
    
    // If we passed all the tests, then attempt to delete it
    boolean success = f.delete();
    // And throw an exception if it didn't work for some (unknown) reason.
    // For example, because of a bug with Java 1.1.1 on Linux, 
    // directory deletion always fails 
    if (!success) System.out.println("Delete: deletion failed");
  }
  	
 	
}

class ListOfFiles implements Enumeration {

    private String[] listOfFiles;
    private int current = 0;

    public ListOfFiles(String[] listOfFiles) {
        this.listOfFiles = listOfFiles;
    }

    public boolean hasMoreElements() {
        if (current < listOfFiles.length)
            return true;
        else
            return false;
    }

    public Object nextElement() {
        InputStream in = null;

        if (!hasMoreElements())
            throw new NoSuchElementException("No more files.");
        else {
            String nextElement = listOfFiles[current];
            current++;
            try {
                in = new FileInputStream(nextElement);
            } catch (FileNotFoundException e) {
                System.err.println("ListOfFiles: Can't open " + nextElement);
            }
        }
        return in;
    }
}




 