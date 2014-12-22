package de.uni.konstanz.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

public class EventsToolsPanel extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EventsToolsPanel frame = new EventsToolsPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EventsToolsPanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 119);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel eventsToolsInnerPanel = new JPanel();
		contentPane.add(eventsToolsInnerPanel, BorderLayout.CENTER);
		
		JButton showEventsButton = new JButton("Show Events");
		
		JCheckBox automaticEventsDisplayChkbx = new JCheckBox("Automatic Events Display");
		
		JLabel lblRefreshTime = new JLabel("Refresh period:");
		
		JSpinner refreshPeriodSpinner = new JSpinner();
		GroupLayout gl_eventsToolsInnerPanel = new GroupLayout(eventsToolsInnerPanel);
		gl_eventsToolsInnerPanel.setHorizontalGroup(
			gl_eventsToolsInnerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_eventsToolsInnerPanel.createSequentialGroup()
					.addComponent(showEventsButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(automaticEventsDisplayChkbx)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblRefreshTime)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(refreshPeriodSpinner, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(135, Short.MAX_VALUE))
		);
		gl_eventsToolsInnerPanel.setVerticalGroup(
			gl_eventsToolsInnerPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_eventsToolsInnerPanel.createSequentialGroup()
					.addGroup(gl_eventsToolsInnerPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(showEventsButton)
						.addComponent(automaticEventsDisplayChkbx)
						.addComponent(lblRefreshTime)
						.addComponent(refreshPeriodSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(58, Short.MAX_VALUE))
		);
		eventsToolsInnerPanel.setLayout(gl_eventsToolsInnerPanel);
	}
}
