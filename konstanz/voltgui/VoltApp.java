package de.uni.konstanz.voltgui;

import java.awt.EventQueue;

public class VoltApp {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VoltGUI view = new VoltGUI();
					VoltModel model = new VoltModel();
					VoltLabelingEnvironmentController controller = 
							new VoltLabelingEnvironmentController(view, model);
					view.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
