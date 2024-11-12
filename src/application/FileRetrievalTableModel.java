package application;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

@SuppressWarnings({ "deprecation", "serial" })
class FileRetrievalTableModel extends AbstractTableModel implements Observer {
    private static final String[] columnNames = {"URL", "Size (MB)", "Progress", "Speed (KB/s)", 
    "Avg Speed (KB/s)", "Elapsed Time", "Remaining Time" ,"Status"};
    private static final Class<?>[] columnClasses = {String.class, String.class,
    JProgressBar.class, String.class, String.class, String.class, String.class, String.class};
    
    private ArrayList<FileRetriever> downloadList = new ArrayList<>();
    
    public void addDownload(FileRetriever download) {
        download.addObserver(this);
        downloadList.add(download);
        fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
    }
    
    public FileRetriever getDownload(int row) {
        return downloadList.get(row);
    }
    
    public void clearDownload(int row) {
        downloadList.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public Class<?> getColumnClass(int col) {
        return columnClasses[col];
    }
    
    public int getRowCount() {
        return downloadList.size();
    }
    
    @SuppressWarnings("removal")
	public Object getValueAt(int row, int col) {
        FileRetriever download = downloadList.get(row);
        switch (col) {
            case 0: return download.getUrl();
            case 1: 
                long size = download.getSize();
                return (size == -1) ? "" : String.format("%.2f", (float)size / 1048576);
            case 2: return new Float(download.getProgress());
            case 3: return String.format("%.2f", download.getSpeed());
            case 4: return String.format("%.2f", download.getAvgSpeed());
            case 5: return download.getElapsedTime();
            case 6: return download.getRemainingTime();
            case 7: return FileRetriever.STATUS_LABELS[download.getStatus()];
        }
        return "";
    }
    
    public void update(Observable o, Object arg) {
        int index = downloadList.indexOf(o);
        fireTableRowsUpdated(index, index);
    }
}