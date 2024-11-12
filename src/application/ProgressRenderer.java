package application;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

class ProgressRenderer extends JProgressBar implements TableCellRenderer {
    
    public ProgressRenderer(int min, int max) {
        super(min, max);
    }
    
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        
        if (value instanceof Float) {
            float percentage = ((Float) value).floatValue();
            setValue((int) percentage);
            setString(String.format("%.1f%%", percentage));
        } else {
            setValue(0);
            setString("0%");
        }
        return this;
    }
}