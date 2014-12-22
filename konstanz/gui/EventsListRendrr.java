package de.uni.konstanz.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import de.uni.konstanz.eventdetection.EventDetector;

public class EventsListRendrr extends DefaultListCellRenderer {
	
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected, boolean hasFocus) {
		JLabel label = 	(JLabel) super.getListCellRendererComponent(list,
				value, index, isSelected, hasFocus);
		EventResult eventResult = (EventResult)value;
		
		String name = eventResult.getName();
		String html = "";
		for ( EventDetector eventDetector : eventResult.getEventDetectors() ) {
			Color color = eventDetector.getColor();
			String hexaColor = String.format("%06x", color.getRGB() & 0x00FFFFFF);
			html += String.format("<font color='%s'>◉</font>", hexaColor);
		}
		String format = "<html>" + name + " " + html + "</html>";
		label.setText(format);
		return label;
	}

}









