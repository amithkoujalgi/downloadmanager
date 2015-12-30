/* 
SOURCE USED SHOULD CARRY AUTHOR CREDITS
AUTHOR:MAHESH KAREKAR
EMAIL:mahesh_gk@lycos.com
FREEWARE DISTRIBUTE FREELY
*/
package downloader;
import javax.swing.table.*;
import javax.swing.JProgressBar;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class DownloadTableModel extends DefaultTableModel{
	  	private boolean DEBUG = false;
		 String[] columnNames = {"No.", 
                                      "File Name",
                                      "Size",
                                      "Received",
                                      "Time",
                                      "Speed",
                                      "Progress"};
         Object[][] data={ };
        
         Class columnClasses[] = {String.class,String.class,String.class, 
         String.class, String.class, String.class, JProgressBar.class};    
       
       	
        public void resetData()
        {
      			data = new String[3][7] ;

			//for ( int i = 0 ; i < 200 ; i ++ )
 			//	data[i] = new String[6] ;

			for ( int i = 0 ; i < 3 ; i ++ )
 				for ( int j = 0 ; j < 7 ; j ++ )
  				{
  					if ( j < 6 )
  					data[i][j] = Integer.toString ( i ) + " " + Integer.toString ( j ) ;
  					else
  					data[i][j] = createProgressBar();
  				}
  				fireTableDataChanged();
  				//printDebugData();
        
        }
        
        
        public void adddownload(int id,String loc)
        {
        	int li_rowcount,i;
        	//copy existing values
        	Object temp[][] = data;
        	li_rowcount = data.length;
        	data = new Object[li_rowcount + 1][7];
        	
        	
        	//Recopy original contents
        	for (  i = 0 ; i < li_rowcount ; i ++ )
 				for ( int j = 0 ; j < 7 ; j ++ )
  				data[i][j] = temp[i][j] ;
        	
        	//Now copy New Contents
        	data[i][0]=(Object)Integer.toString ( id );
        	data[i][1]=(Object)loc;
        	data[i][2]=" ";
        	data[i][3]=" ";
        	data[i][4]=" ";
        	data[i][5]=" ";
        	data[i][6]=createProgressBar();
        	fireTableDataChanged();
        	//printDebugData();
        }
        	
        	
        private JProgressBar createProgressBar()
		{
			JProgressBar progressBar = new JProgressBar(0, 100);

			return progressBar;
		}

        
		
        public int getColumnCount() {
            return columnNames.length;
        }
        
        public int getRowCount() {
        	if ( data == null)
        	return 0;
        	else
            return  data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) { 
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
          
            if (col >= 6)
			{
				((JProgressBar)data[row][col]).setValue(Integer.parseInt((String)value));
	
			}
			else 
			if(col == 2)
			{
				if ( String.valueOf(value).equals("-1"))
					data[row][col]="Timed Out or Invalid Url";
				else
					data[row][col]= value;				
			}
			else
			{
				data[row][col] = value;
			}
			fireTableCellUpdated(row, col);
           
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }

}


