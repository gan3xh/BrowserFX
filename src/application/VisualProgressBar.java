package application;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

class VisualProgressBar extends JProgressBar implements TableCellRenderer {
    
    public VisualProgressBar(int minValue, int maxValue) {
        super(minValue, maxValue);
    }
    
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        
        if (value instanceof Float) {
            float floatValue = (Float) value;
            setValue((int) floatValue);
        } else {
            setValue(0);
        }
        
        return this;
    }
}