package de.uni.konstanz.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JListTest {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("GUI Concurrency");
		frame.setPreferredSize( new Dimension(400, 400));
		frame.setLayout( new FlowLayout() );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		String[] entries = {"Rakan", "Alaa"};
		
		final DefaultListModel<String> listModel = new DefaultListModel<String>();
		
		for ( int i = 0; i < entries.length; i++ ) {
			listModel.add(i, entries[i]);
		}
		
		final JList list = new JList(listModel);
		list.setCellRenderer( new MyRenderer() );
		list.addListSelectionListener( new MyListListener() );
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(200,200));
		
		
		
		JButton removeItemBtn = new JButton("Remove");
		removeItemBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				System.out.println(index);
				System.out.println( listModel.getSize() );
				listModel.remove(index);
				list.repaint();
			}
		});
		
		JButton addItemBtn = new JButton("Add");
		addItemBtn.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = listModel.getSize();
				listModel.addElement("Test " + (index+1));
			}
		} );
		
		
		
		frame.add(scrollPane);
		frame.add(removeItemBtn);
		frame.add(addItemBtn);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private static class MyListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if ( !e.getValueIsAdjusting() ) {
				JList list = (JList) e.getSource();
			}
		}
		
	}
	
	private static class MyRenderer extends DefaultListCellRenderer {
		
		public Component getListCellRendererComponent(JList list,
				Object value, int index, boolean isSelected, 
				boolean hasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(
					list, value, index, isSelected, hasFocus);
			
			String txt = label.getText();
			
//			String html = "<html>Text color: <font color='red'>red</font></html>";
//			String format = String.format( "<html>%s<font color='red'>◉</font><font color='green'>◉</font></html>", txt );
//			
			
			List<Color> colorsList = new LinkedList<Color>();
			colorsList.add(Color.RED);
			colorsList.add(Color.BLUE);
			colorsList.add(Color.CYAN);
			
			String html = "";
			
			for ( Color color : colorsList ) {
				String hexaColor = String.format("%06x", color.getRGB() & 0x00FFFFFF);
				html += String.format("<font color='%s'>◉</font>", hexaColor);
			}
			
			String format = "<html>" + txt + html + "</html>";
			label.setText(format);
			return label;
		}
	}
	
	
}



