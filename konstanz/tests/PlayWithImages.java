package de.uni.konstanz.tests;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayWithImages {
	public static void main(String[] args) throws IOException {
		File file = new File("/Users/rockyrock/Desktop/image.jpg");
//		BufferedImage in = ImageIO.read(file);
//		BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g = newImage.createGraphics();
//		g.drawImage(in, 0, 0, null);
//		g.dispose();
		//ImageIO.write(newImage, "png", new File("/Users/rockyrock/Desktop/ascii-art.png"));
		
		int width = 100;
		int height = 30;
	 
		BufferedImage image = ImageIO.read(file);
		//BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setFont(new Font("SansSerif", Font.BOLD, 24));
	 
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.drawString("JAVA", 10, 20);
	 
		//save this image
		//ImageIO.write(image, "png", new File("/users/mkyong/ascii-art.png"));
	 
		for (int y = 0; y < height; y++) {
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < width; x++) {
	 
				sb.append(image.getRGB(x, y) == -16777216 ? " " : "$");
	 
			}
	 
			if (sb.toString().trim().isEmpty()) {
				continue;
			}
	 
		}
		ImageIO.write(image, "png", new File("/Users/rockyrock/Desktop/ascii-art.png"));
	}
}
