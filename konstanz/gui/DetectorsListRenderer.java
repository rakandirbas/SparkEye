package de.uni.konstanz.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import de.uni.konstanz.eventdetection.EventDetector;

public class DetectorsListRenderer extends DefaultListCellRenderer {

	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected, boolean hasFocus) {
		JLabel label = 	(JLabel) super.getListCellRendererComponent(list,
				value, index, isSelected, hasFocus);
		EventDetector detector = (EventDetector)value;
		
		if ( isSelected ) {
			label.setForeground(Color.WHITE);
		}
		else {
			label.setForeground(detector.getColor());
		}
		
		return label;
	}
}

