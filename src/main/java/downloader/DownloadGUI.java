package downloader;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class DownloadGUI extends JFrame implements ActionListener{
	
	private static JLabel lblLoc = new JLabel("URL:");
	private static JTextField txtFileLoc = new JTextField(20);
	private static JLabel lblConn = new JLabel("    Remote Connections:");
	private static JComboBox cmbConnection = new JComboBox();
	private static JLabel lblMemory = new JLabel("    Memory:");
	private static JComboBox cmbMemory = new JComboBox();
	private static JLabel lblMb = new JLabel("MB");
	public static JButton cbDownload = new JButton("START");
	public DownloadFile df[] = new  DownloadFile[0];
	public int DownloadID=0;
	private DownloadTableModel dm;

	
	//Start DownloadGUI Constructor
	public DownloadGUI(){
		super("DACCEL Download Accelerator v1.0 - Written By Mahesh Karekar");
		//Assign Icon to Frame
		//this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("images/SpeedUp.gif")));
		Container c= getContentPane();
		c.setLayout(new BorderLayout());
		
		JPanel toppanel = new JPanel();
		toppanel.setLayout(new FlowLayout());
		c.add(toppanel,BorderLayout.NORTH);
		
		toppanel.add(lblLoc);
		toppanel.add(txtFileLoc);
		toppanel.add(cbDownload);
		
			
		JPanel bottompanel = new JPanel();
		bottompanel.setLayout(new FlowLayout());
		c.add(bottompanel,BorderLayout.SOUTH);
		cbDownload.addActionListener(this);
		
		//Added Combobox for connections 
		cmbConnection.addItem("1");
		cmbConnection.addItem("2");
		cmbConnection.addItem("3");
		cmbConnection.addItem("4");
		cmbConnection.addItem("5");
		cmbConnection.addItem("6");
		cmbConnection.addItem("7");
		cmbConnection.addItem("8");
		cmbConnection.addItem("9");
		cmbConnection.addItem("10");
		
		toppanel.add(lblConn);
		toppanel.add(cmbConnection);
		
		cmbMemory.addItem("1");
		cmbMemory.addItem("2");
		cmbMemory.addItem("3");
		cmbMemory.addItem("4");
		cmbMemory.addItem("5");
		cmbMemory.addItem("6");
		cmbMemory.addItem("7");
		cmbMemory.addItem("8");
		cmbMemory.addItem("9");
		cmbMemory.addItem("10");
		
		toppanel.add(lblMemory);
		toppanel.add(cmbMemory);
		toppanel.add(lblMb);
		
		dm = new DownloadTableModel();
		JTable table = new JTable( dm);
		tblMouseAdapter ma = new tblMouseAdapter(this);
		table.addMouseListener(ma);
		
		//Set Table Width
		table.getColumnModel().getColumn(1).setMinWidth(300);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		table.setPreferredScrollableViewportSize(new Dimension((screen.width/2)+ 300, (screen.height/2)));
		table.setDefaultRenderer(JProgressBar.class, new DownloadTableRenderer());
		JScrollPane ps = new JScrollPane(table);
		bottompanel.add(ps);
		table.getTableHeader().repaint();

		}//End Constructor DownloadGUI
			
	public void actionPerformed(ActionEvent e){
		
		JButton Button = (JButton)e.getSource();
		
			if ( Button.getActionCommand() == "START" )
				{
					//User Inputs
					String ls_FileLoc;
					int li_TotalConnections = 5;//Default Remote Connections
					int li_BufferLen = 4; // Default 4 MB Buffer 
					
					//Check for empty Url
					if (txtFileLoc.getText().equals("")){
						JOptionPane.showMessageDialog(
    				             null,"URL is Invalid or Empty.Please enter valid URL",
                 				 "ERROR",JOptionPane.ERROR_MESSAGE);
			
						return;
					}
					
					ls_FileLoc = txtFileLoc.getText();
					li_TotalConnections = Integer.parseInt((String)cmbConnection.getSelectedItem()); 
					li_BufferLen = Integer.parseInt((String)cmbMemory.getSelectedItem()); 
					
					//Add Download Info to table
					dm.adddownload(DownloadID,ls_FileLoc);
					//Initiate New Download
					DownloadFile d = new DownloadFile(DownloadID,ls_FileLoc,li_TotalConnections,li_BufferLen);
					addDownload(d);
					//Set Priority of Thread
					//df[DownloadID].setPriority(Thread.MIN_PRIORITY);
					//Start Download Thread
					df[DownloadID].start();
					
					//ADD Monitor to the Thread
					Monitor dMonitor = new Monitor(this,DownloadID);
					//dMonitor.setPriority(Thread.MIN_PRIORITY);
					dMonitor.start();
					DownloadID = DownloadID + 1;
					
					//Empty the TextField
					txtFileLoc.setText("");
					
				} // ENDIF ( Button.getActionCommand() == "START" )
				
		}//END actionPerformed(ActionEvent e)
		
		
	public void addDownload(DownloadFile d) {
       
        DownloadFile[] dab = new DownloadFile[df.length+1];
        System.arraycopy(df, 0, dab, 0, df.length);
        dab[dab.length-1] = d;
        df = dab;
        
    	}// END addDownload(DownloadFile d)
	
	public void updateStatus( int currThread, boolean dFailed){
				if (!dFailed){
				this.dm.setValueAt(this.df[currThread].getFileSize(),currThread,2);
		   		this.dm.setValueAt(this.df[currThread].getBytesDownloaded(),currThread,3);
		   		this.dm.setValueAt(this.df[currThread].getTimeForDownload(),currThread,4);
		   		this.dm.setValueAt(this.df[currThread].getDownloadSpeed(),currThread,5);
		   		this.dm.setValueAt(String.valueOf(this.df[currThread].DownloadProgress()),currThread,6);	
				}
				else {
				String str="Failed";
				this.dm.setValueAt((Object)str,currThread,2);
		   		this.dm.setValueAt((Object)str,currThread,3);
		   		this.dm.setValueAt((Object)str,currThread,4);
		   		this.dm.setValueAt((Object)str,currThread,5);
		   		
				}
				
				
		}//END updateStatus( int currThread)
		
		
	//MAIN 
	public static void main(String args[]){

			//GUI 
			DownloadGUI dgui = new DownloadGUI();
			dgui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			dgui.setResizable(false);
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); 
			//dgui.setLocation((screen.width - screen.width/2) / 2, (screen.height - screen.height/2) / 2);   
			dgui.setLocation(50,100);   
			dgui.setSize(screen.width/2, screen.height/2);
					
			dgui.validate();
			dgui.pack();
			dgui.show();
			
		}//End Main()
			
}

class tblMouseAdapter extends MouseAdapter{
	DownloadGUI dm_dgui;
	int index;
	Runtime r;
	
	public tblMouseAdapter(DownloadGUI gui){
		dm_dgui=gui;
		r = Runtime.getRuntime();
	}
	

	public void mousePressed(MouseEvent e){
				if (e.getClickCount() == 2 ){
				index = ((JTable)e.getSource()).getSelectedRow();
				FileUtils fu = new FileUtils()  ;
				//Check if download complete
				if ( dm_dgui.df[index].Complete == 1){
					String execFile = fu.getFilename(dm_dgui.df[index].FilePath);
					try{
						File frun = new File("Downloaded_Files/" + execFile);
						String runFile = frun.getAbsolutePath() ;
						String FileExt = runFile.substring(runFile.lastIndexOf('.') + 1,runFile.length());

						String osName = System.getProperty("os.name" );
            			String[] cmd = new String[3];

            			if( osName.equals( "Windows XP" ) || osName.equals( "Windows 2000" )
            					|| osName.equals("Windows NT") )
            				{
                			cmd[0] = "cmd.exe" ;
                			cmd[1] = "/C" ;
               				cmd[2] = runFile;
               				r.exec(cmd);
            				}
            			else if( osName.equals( "Windows 95" ) || osName.equals( "Windows 98" ) 
            					||	osName.equals( "Windows ME" ))
            				{
                			cmd[0] = "command.com" ;
                			cmd[1] = "/C" ;
                			cmd[2] = runFile;
                			r.exec(cmd);
            				}
            			else
            				{
            					JOptionPane.showMessageDialog(
    				          		null,"OS Not Supported",
                 			 		"ERROR",JOptionPane.ERROR_MESSAGE);	
							}
						
					 
						}catch(Exception ioe){
							String eMsg = ioe.getMessage();
						JOptionPane.showMessageDialog(
    				          null,eMsg,
                 			 "ERROR",JOptionPane.ERROR_MESSAGE);	
							
						}
				}
				else if( dm_dgui.df[index].Complete == -1 ){
					JOptionPane.showMessageDialog(
    				          null,"Download Failed.Please verify url address",
                		 "INFORMATION",JOptionPane.INFORMATION_MESSAGE);				
                }
				else{
					JOptionPane.showMessageDialog(
    				          null,"Please wait for Download to Complete",
                 			 "INFORMATION",JOptionPane.INFORMATION_MESSAGE);				
                 	}
				
				
				}			
		
	}
		
}
	
//---------Monitor Class --------------------		
class Monitor extends Thread{
			
		   DownloadGUI dm_dgui;
		   int threadIndex;
		   
		   public Monitor(DownloadGUI dm_frame,int idx){
		   	dm_dgui=dm_frame;
		   	threadIndex = idx;
		  	
		   }

		   public void run(){
	
		/* Main Loop t check if any Download Threads exist
		   Monitor and update Thread status  */
		   int dThreadCount=0;
		   int currThread=0;
		   int dconnections=0;
		   int Downloadcomplete;
		   String[] files;
		   FileUtils futils = new FileUtils();
		
		   currThread= threadIndex;
		   //dm_dgui.updateStatus(currThread);
		   if (dm_dgui.df[currThread].FileSize == -1){
		   		dm_dgui.df[currThread].Complete =-1;
						   			
		   		//Message Stating Download Failed
				JOptionPane.showMessageDialog(
   				      	 null,"Download Failed.Try Downloading Again or verify URL is Correct" ,
                 		 "INFORMATION",JOptionPane.INFORMATION_MESSAGE);
                 dm_dgui.updateStatus(currThread,true);
                 		 				
		   		}
		   			
		   
		   while(dm_dgui.df[currThread].Complete == 0) // Download Thread Monitor Loop
		   {
		
		   	//Total Download Threads at this Movement
		   			
		   				dconnections = dm_dgui.df[currThread].TotConnections;
		   				
		   				//Check
		   				// 1. Is current Download Complete
		   				// 2. Are all subconnection for Download Active
		   				if (dm_dgui.df[currThread].Complete == 0 && 
		   						dm_dgui.df[currThread].ActiveSubConn == dconnections ){
		   				
		   				dm_dgui.updateStatus(currThread,false);
		   				
		   				
		   				//Lets get the Connections
		   				Downloadcomplete = 1;//Set Initial Download Complete Flag as True
		   				files =  new String[dconnections]; // Array Holding SubDownload Names
		   				
		   				//Are Subconnections active
		   				for(int subDown = 0; subDown < dconnections ; subDown ++){
		   				    
		   				    files[subDown]= dm_dgui.df[currThread].getSubDownId(subDown);
		   				   		
		   				   		//Verify if Sub Download is Incomplete
		   				   		if(dm_dgui.df[currThread].isSubDownComplete(subDown) == 0){
		   				      	
		   				      		Downloadcomplete = 0; //Download Incomplete
		   				      		break;
		   				      		
		   				      	}//EndIf if(dgui.df[currThread].isSubDownComplete(subDown) == 0) 
		   				      
		   				     }//EndFor for(int subDown = 0; subDown < dconnections ; subDown ++)
		   				      
		   				if (Downloadcomplete == 1) //Complete
		   					{
		   						
		   					//Concatinate Files
		   					futils.concat(files,dm_dgui.df[currThread].FilePath);
		   					
		   					for(int fileid=0;fileid < dconnections;fileid++)
		   						futils.delete(files[fileid]); //Delete File
		   					
		   					dm_dgui.df[currThread].Complete = 1; //DownloadFile Thread Complete
		   					dm_dgui.updateStatus(currThread,false);//Update Table
		   					}     
		   				  
		   				  }//End if InComplete Download
		   		} // End While
			}//End run()
}
	