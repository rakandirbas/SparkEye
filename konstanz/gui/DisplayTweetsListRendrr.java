package de.uni.konstanz.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import de.uni.konstanz.models.Tweet;

public class DisplayTweetsListRendrr extends DefaultListCellRenderer {
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected, boolean hasFocus) {
		JLabel label = 	(JLabel) super.getListCellRendererComponent(list,
				value, index, isSelected, hasFocus);
		Tweet tweet = (Tweet) value;
		
		return label;
	}
}
