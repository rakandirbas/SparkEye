package de.uni.konstanz.utils;

import java.awt.Color;

public class GUIUtils {
	/**
	 * Returns the HTML for a colored text. Note that the returned String
	 * should be placed between <html>returned String</html> tags.
	 * @param text
	 * @param color
	 * @return
	 */
	public static String getColoredTextHTML(String text, Color color) {
		String hexaColor = String.format("%06x", color.getRGB() & 0x00FFFFFF);
		String html = String.format("<font color='%s'>%s</font>", hexaColor, text);
		
		return html;
	}
}
