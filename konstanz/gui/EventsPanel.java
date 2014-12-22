package de.uni.konstanz.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

public class EventsPanel extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EventsPanel frame = new EventsPanel();
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
	public EventsPanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 454, 696);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel innerEventsPanel = new JPanel();
		contentPane.add(innerEventsPanel, BorderLayout.CENTER);
		innerEventsPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		innerEventsPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel eventsScrolPanePanel = new JPanel();
		scrollPane.setViewportView(eventsScrolPanePanel);
		
		JLabel lblDetectorsInEventsTab = new JLabel("Detectors:");
		
		JScrollPane eventsDetectorsScrollPane = new JScrollPane();
		
		JLabel lblEventsInEventsTab = new JLabel("Events:");
		
		JPanel outerDisplayEventsPanel = new JPanel();
		GroupLayout gl_eventsScrolPanePanel = new GroupLayout(eventsScrolPanePanel);
		gl_eventsScrolPanePanel.setHorizontalGroup(
			gl_eventsScrolPanePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_eventsScrolPanePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_eventsScrolPanePanel.createParallelGroup(Alignment.LEADING)
						.addComponent(outerDisplayEventsPanel, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDetectorsInEventsTab)
						.addComponent(lblEventsInEventsTab)
						.addComponent(eventsDetectorsScrollPane, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl_eventsScrolPanePanel.setVerticalGroup(
			gl_eventsScrolPanePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_eventsScrolPanePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDetectorsInEventsTab)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(eventsDetectorsScrollPane, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEventsInEventsTab)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(outerDisplayEventsPanel, GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
					.addContainerGap())
		);
		outerDisplayEventsPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane displayEventsScrollPane = new JScrollPane();
		outerDisplayEventsPanel.add(displayEventsScrollPane, BorderLayout.CENTER);
		
		JPanel displayEventsPanel = new JPanel();
		displayEventsScrollPane.setViewportView(displayEventsPanel);
		
		JList eventsDetectorsList = new JList();
		eventsDetectorsScrollPane.setViewportView(eventsDetectorsList);
		eventsScrolPanePanel.setLayout(gl_eventsScrolPanePanel);
	}
}
