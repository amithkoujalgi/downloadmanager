/* 
OSOURCE USED SHOULD CARRY AUTHOR CREDITS
AUTHOR:MAHESH KAREKAR
EMAIL:mahesh_gk@lycos.com
FREEWARE DISTRIBUTE FREELY
*/
package downloader;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Color;


public class DownloadTableRenderer extends JProgressBar implements TableCellRenderer {

		public DownloadTableRenderer() {
				super(SwingConstants.HORIZONTAL);
				setSize(115,15);
				setStringPainted(true);
				}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected,boolean hasFocus,int row,int column) {
		if (value == null) {
		return this;
		}
		
		if (value instanceof JProgressBar) {
			setValue(((JProgressBar)value).getValue());
			setString(String.valueOf(((JProgressBar)value).getValue()) + "%");
			if ( ((JProgressBar)value).getValue() < 50 )
			setForeground(Color.blue);
			else
			if ( ((JProgressBar)value).getValue() > 50 && ((JProgressBar)value).getValue() < 100 )
			setForeground(Color.green);
			else
			if ( ((JProgressBar)value).getValue() == 100)
			setForeground(Color.red);
			
			}
		else {
			setValue(0);
			}
		return this;
		}
}