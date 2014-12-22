package de.uni.konstanz.voltgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import de.uni.konstanz.utils.GUIUtils;

public class VoltGUI extends JFrame {

	private JPanel contentPane;
	private JTextField searchTextField;
	private JButton btnBrowseForStreamFile;
	private JToggleButton btnStartStream;
	private JButton btnNewsworthyAndTrusted;
	private JButton btnNewsworthyAndNotTrusted;
	private JButton btnNotNewsworthyAndTrusted;
	private JButton btnNotNewsworthyAndNotTrusted;
	private JToggleButton btnNewClassifier;
	private JToggleButton btnEditClassifier;
	private JButton btnDeleteClassifier;
	private JButton btnTrainClassifer;
	private JTextArea userDescriptionTextArea;
	private JLabel lblNWTS;
	private JLabel lblNWNTS;
	private JLabel lblNNWTS;
	private JLabel lblNNWNTS;
	private JLabel lblCNWTS;
	private JLabel lblCNWNTS;
	private JLabel lblCNNWTS;
	private JLabel lblCNNWNTS;
	private JLabel lblSetRealName;
	private JLabel lblsetUsername;
	private JLabel lblsetLocation;
	private JLabel lblsetListsCount;
	private JLabel lblsetFollowersCount;
	private JLabel lblsetFriendsCount;
	private JLabel lblsetTweetsCount;
	private JPanel explorationTabPanel;
	private JLabel lblProgress;
	private JProgressBar progressBar;
	private JButton btnResetStream;
	private JList classifiersList;
	private JList resultsList;
	private JLabel lblProcessedTweets;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VoltGUI frame = new VoltGUI();
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
	public VoltGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1232, 730);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 6, 1220, 696);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel labelingTabPanel = new JPanel();
		tabbedPane.addTab("Labeling Environment", null, labelingTabPanel, null);
		labelingTabPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel labelingPanel = new JPanel();
		labelingPanel.setPreferredSize( new Dimension(1215, 140) );
		labelingPanel.setLayout(null);
		
		labelingTabPanel.add(labelingPanel, BorderLayout.NORTH);
		
		JLabel lblLoadFile = new JLabel("Load File:");
		lblLoadFile.setBounds(6, 6, 61, 16);
		labelingPanel.add(lblLoadFile);
		
		btnBrowseForStreamFile = new JButton("Browse");
		btnBrowseForStreamFile.setBounds(79, 1, 117, 29);
		labelingPanel.add(btnBrowseForStreamFile);
		
		btnStartStream = new JToggleButton("Start Stream");
		btnStartStream.setEnabled(false);
		btnStartStream.setBounds(195, 1, 130, 29);
		labelingPanel.add(btnStartStream);
		
		JLabel lblSearch = new JLabel("Query:");
		lblSearch.setBounds(6, 40, 45, 16);
		labelingPanel.add(lblSearch);
		
		searchTextField = new JTextField();
		searchTextField.setBounds(79, 34, 511, 28);
		labelingPanel.add(searchTextField);
		searchTextField.setColumns(10);
		
		JLabel lblLabeling = new JLabel("Labeling:");
		lblLabeling.setBounds(6, 75, 61, 16);
		labelingPanel.add(lblLabeling);
		
		String format = "<html>" + "Newsworthy" + " " + GUIUtils.getColoredTextHTML("◻︎", Color.BLUE) + " | Trusted " + GUIUtils.getColoredTextHTML("△", Color.BLUE) + "</html>";
		btnNewsworthyAndTrusted = new JButton(format);
		btnNewsworthyAndTrusted.setEnabled(false);
		btnNewsworthyAndTrusted.setBounds(79, 70, 245, 29);
		labelingPanel.add(btnNewsworthyAndTrusted);
		
		format = "<html>" + "Newsworthy" + " " + GUIUtils.getColoredTextHTML("◻︎", Color.BLUE) + " | Not Trusted " + GUIUtils.getColoredTextHTML("△", Color.RED) + "</html>";
		btnNewsworthyAndNotTrusted = new JButton(format);
		btnNewsworthyAndNotTrusted.setEnabled(false);
		btnNewsworthyAndNotTrusted.setBounds(322, 70, 268, 29);
		labelingPanel.add(btnNewsworthyAndNotTrusted);
		
		format = "<html>" + "Not Newsworthy" + " " + GUIUtils.getColoredTextHTML("◻", Color.RED) + " | Trusted " + GUIUtils.getColoredTextHTML("△", Color.BLUE) + "</html>";
		btnNotNewsworthyAndTrusted = new JButton(format);
		btnNotNewsworthyAndTrusted.setEnabled(false);
		btnNotNewsworthyAndTrusted.setBounds(79, 105, 245, 29);
		labelingPanel.add(btnNotNewsworthyAndTrusted);
		
		format = "<html>" + "Not Newsworthy" + " " + GUIUtils.getColoredTextHTML("◻", Color.RED) + " | Not Trusted " + GUIUtils.getColoredTextHTML("△", Color.RED) + "</html>";
		btnNotNewsworthyAndNotTrusted = new JButton(format);
		btnNotNewsworthyAndNotTrusted.setEnabled(false);
		btnNotNewsworthyAndNotTrusted.setBounds(322, 105, 268, 29);
		labelingPanel.add(btnNotNewsworthyAndNotTrusted);
		
		JLabel lblClassifiersList = new JLabel("Classifiers list:");
		lblClassifiersList.setBounds(606, 40, 100, 16);
		labelingPanel.add(lblClassifiersList);
		
		classifiersList = new JList();
		JScrollPane classifiersScrollPane = new JScrollPane(classifiersList);
		classifiersScrollPane.setBounds(709, 6, 198, 104);
		labelingPanel.add(classifiersScrollPane);
		
		btnNewClassifier = new JToggleButton("New Classifier");
		btnNewClassifier.setBounds(919, 3, 130, 29);
		labelingPanel.add(btnNewClassifier);
		
		btnEditClassifier = new JToggleButton("Edit");
		btnEditClassifier.setEnabled(false);
		btnEditClassifier.setBounds(919, 29, 130, 29);
		labelingPanel.add(btnEditClassifier);
		
		btnDeleteClassifier = new JButton("Delete");
		btnDeleteClassifier.setEnabled(false);
		btnDeleteClassifier.setBounds(919, 55, 130, 29);
		labelingPanel.add(btnDeleteClassifier);
		
		btnTrainClassifer = new JButton("Train");
		btnTrainClassifer.setEnabled(false);
		btnTrainClassifer.setBounds(919, 81, 130, 29);
		labelingPanel.add(btnTrainClassifer);
		
		lblProcessedTweets = new JLabel("Tweets:");
		lblProcessedTweets.setBounds(449, 6, 185, 16);
		labelingPanel.add(lblProcessedTweets);
		
		btnResetStream = new JButton("Reset Stream");
		btnResetStream.setEnabled(false);
		btnResetStream.setBounds(322, 1, 117, 29);
		labelingPanel.add(btnResetStream);
		
		lblProgress = new JLabel("Progress:");
		lblProgress.setBounds(645, 112, 61, 16);
		labelingPanel.add(lblProgress);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(709, 114, 198, 20);
		labelingPanel.add(progressBar);
		
		JPanel resultsPanel = new JPanel();
		labelingTabPanel.add(resultsPanel, BorderLayout.CENTER);
		resultsPanel.setLayout(new BorderLayout(0, 0));
		
		resultsList = new JList();
		JScrollPane resultsScrollPane = new JScrollPane(resultsList);
		resultsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(1215, 100));
		labelingTabPanel.add(infoPanel, BorderLayout.SOUTH);
		infoPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel userInfoPanel = new JPanel();
		userInfoPanel.setBorder(new TitledBorder("User information"));
		userInfoPanel.setPreferredSize(new Dimension(810, 90));
		infoPanel.add(userInfoPanel, BorderLayout.WEST);
		userInfoPanel.setLayout(null);
		
		JLabel lblRealName = new JLabel("Real name:");
		lblRealName.setBounds(6, 19, 68, 16);
		userInfoPanel.add(lblRealName);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(6, 39, 73, 16);
		userInfoPanel.add(lblUsername);
		
		JLabel lblFollowers = new JLabel("Followers:");
		lblFollowers.setBounds(259, 19, 68, 16);
		userInfoPanel.add(lblFollowers);
		
		JLabel lblFriends = new JLabel("Friends:");
		lblFriends.setBounds(259, 39, 50, 16);
		userInfoPanel.add(lblFriends);
		
		JLabel lbltweets = new JLabel("Tweets:");
		lbltweets.setBounds(259, 58, 50, 16);
		userInfoPanel.add(lbltweets);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setBounds(6, 58, 68, 16);
		userInfoPanel.add(lblLocation);
		
		JLabel lblLists = new JLabel("Lists:");
		lblLists.setBounds(6, 78, 34, 16);
		userInfoPanel.add(lblLists);
		
		userDescriptionTextArea = new JTextArea();
		userDescriptionTextArea.setWrapStyleWord(true);
		userDescriptionTextArea.setLineWrap(true);
		userDescriptionTextArea.setEditable(false);
		JScrollPane userDesciptionScrollPane = new JScrollPane(userDescriptionTextArea);
		userDesciptionScrollPane.setBounds(440, 25, 365, 70);
		userInfoPanel.add(userDesciptionScrollPane);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(440, 10, 91, 16);
		userInfoPanel.add(lblDescription);
		
		lblSetRealName = new JLabel("");
		lblSetRealName.setBounds(76, 19, 171, 16);
		userInfoPanel.add(lblSetRealName);
		
		lblsetUsername = new JLabel("");
		lblsetUsername.setBounds(76, 39, 171, 16);
		userInfoPanel.add(lblsetUsername);
		
		lblsetLocation = new JLabel("");
		lblsetLocation.setBounds(76, 58, 171, 16);
		userInfoPanel.add(lblsetLocation);
		
		lblsetListsCount = new JLabel("");
		lblsetListsCount.setBounds(44, 78, 203, 16);
		userInfoPanel.add(lblsetListsCount);
		
		lblsetFollowersCount = new JLabel("");
		lblsetFollowersCount.setBounds(326, 19, 102, 16);
		userInfoPanel.add(lblsetFollowersCount);
		
		lblsetFriendsCount = new JLabel("");
		lblsetFriendsCount.setBounds(310, 39, 118, 16);
		userInfoPanel.add(lblsetFriendsCount);
		
		lblsetTweetsCount = new JLabel("");
		lblsetTweetsCount.setBounds(309, 58, 119, 16);
		userInfoPanel.add(lblsetTweetsCount);
		
		JPanel labelingSummaryPanel = new JPanel();
		labelingSummaryPanel.setBorder( new TitledBorder("Labeling & Classification Summary") );
		infoPanel.add(labelingSummaryPanel, BorderLayout.CENTER);
		labelingSummaryPanel.setLayout(null);
		
		format = "<html>Labeled: " + GUIUtils.getColoredTextHTML("◻︎", Color.BLUE) + "/" + GUIUtils.getColoredTextHTML("△︎", Color.BLUE) + ":</html>";
		JLabel lblNewsworthy = new JLabel(format);
		lblNewsworthy.setBounds(6, 19, 91, 16);
		labelingSummaryPanel.add(lblNewsworthy);
		
		format = "<html>Labeled: " + GUIUtils.getColoredTextHTML("◻", Color.BLUE) + "/" + GUIUtils.getColoredTextHTML("△︎", Color.RED) + ":</html>";
		JLabel lblNotNewsworthy = new JLabel(format);
		lblNotNewsworthy.setBounds(6, 38, 91, 16);
		labelingSummaryPanel.add(lblNotNewsworthy);
		
		format = "<html>Labeled: " + GUIUtils.getColoredTextHTML("◻︎", Color.RED) + "/" + GUIUtils.getColoredTextHTML("△", Color.BLUE) + ":</html>";
		JLabel lblTrustedSources = new JLabel(format);
		lblTrustedSources.setBounds(6, 58, 91, 16);
		labelingSummaryPanel.add(lblTrustedSources);
		
		format = "<html>Labeled: " + GUIUtils.getColoredTextHTML("◻︎", Color.RED) + "/" + GUIUtils.getColoredTextHTML("△︎", Color.RED) + ":</html>";
		JLabel lblNotTrustedSource = new JLabel(format);
		lblNotTrustedSource.setBounds(6, 78, 91, 16);
		labelingSummaryPanel.add(lblNotTrustedSource);
		
		format = "<html>" + "Classified" + " "+ GUIUtils.getColoredTextHTML("◼︎︎", Color.BLUE) + "/" + GUIUtils.getColoredTextHTML("▲︎", Color.BLUE) + ":</html>";
		JLabel lblClassifiedNwts = new JLabel(format);
		lblClassifiedNwts.setBounds(172, 19, 107, 16);
		labelingSummaryPanel.add(lblClassifiedNwts);
		
		format = "<html>" + "Classified" + " "+ GUIUtils.getColoredTextHTML("◼︎︎", Color.BLUE) + "/" + GUIUtils.getColoredTextHTML("▲︎", Color.RED) + ":</html>";
		JLabel lblClassifiedNwnts = new JLabel(format);
		lblClassifiedNwnts.setBounds(172, 38, 107, 16);
		labelingSummaryPanel.add(lblClassifiedNwnts);
		
		format = "<html>" + "Classified" + " "+ GUIUtils.getColoredTextHTML("◼︎︎", Color.RED) + "/" + GUIUtils.getColoredTextHTML("▲︎", Color.BLUE) + ":</html>";
		JLabel lblClassifiedNnwts = new JLabel(format);
		lblClassifiedNnwts.setBounds(172, 58, 107, 16);
		labelingSummaryPanel.add(lblClassifiedNnwts);
		
		format = "<html>" + "Classified" + " "+ GUIUtils.getColoredTextHTML("◼︎", Color.RED) + "/" + GUIUtils.getColoredTextHTML("▲︎", Color.RED) + ":</html>";
		JLabel lblClassifiedNnwnts = new JLabel(format);
		lblClassifiedNnwnts.setBounds(172, 78, 107, 16);
		labelingSummaryPanel.add(lblClassifiedNnwnts);
		
		lblNWTS = new JLabel("0");
		lblNWTS.setBounds(99, 19, 61, 16);
		labelingSummaryPanel.add(lblNWTS);
		
		lblNWNTS = new JLabel("0");
		lblNWNTS.setBounds(99, 38, 61, 16);
		labelingSummaryPanel.add(lblNWNTS);
		
		lblNNWTS = new JLabel("0");
		lblNNWTS.setBounds(99, 58, 61, 16);
		labelingSummaryPanel.add(lblNNWTS);
		
		lblNNWNTS = new JLabel("0");
		lblNNWNTS.setBounds(99, 78, 61, 16);
		labelingSummaryPanel.add(lblNNWNTS);
		
		lblCNWTS = new JLabel("0");
		lblCNWTS.setBounds(278, 19, 61, 16);
		labelingSummaryPanel.add(lblCNWTS);
		
		lblCNWNTS = new JLabel("0");
		lblCNWNTS.setBounds(278, 38, 61, 16);
		labelingSummaryPanel.add(lblCNWNTS);
		
		lblCNNWTS = new JLabel("0");
		lblCNNWTS.setBounds(278, 58, 61, 16);
		labelingSummaryPanel.add(lblCNNWTS);
		
		lblCNNWNTS = new JLabel("0");
		lblCNNWNTS.setBounds(278, 78, 61, 16);
		labelingSummaryPanel.add(lblCNNWNTS);
		
		explorationTabPanel = new JPanel();
		tabbedPane.addTab("Exploration Environment", null, explorationTabPanel, null);
		
	}

	public JButton getBtnBrowseForStreamFile() {
		return btnBrowseForStreamFile;
	}

	public JToggleButton getBtnStartStream() {
		return btnStartStream;
	}

	public JButton getBtnNewsworthyAndTrusted() {
		return btnNewsworthyAndTrusted;
	}

	public JButton getBtnNewsworthyAndNotTrusted() {
		return btnNewsworthyAndNotTrusted;
	}

	public JButton getBtnNotNewsworthyAndTrusted() {
		return btnNotNewsworthyAndTrusted;
	}

	public JButton getBtnNotNewsworthyAndNotTrusted() {
		return btnNotNewsworthyAndNotTrusted;
	}

	public JToggleButton getBtnNewClassifier() {
		return btnNewClassifier;
	}

	public JToggleButton getBtnEditClassifier() {
		return btnEditClassifier;
	}

	public JButton getBtnDeleteClassifier() {
		return btnDeleteClassifier;
	}

	public JButton getBtnTrainClassifer() {
		return btnTrainClassifer;
	}

	public JButton getBtnResetStream() {
		return btnResetStream;
	}

	public JTextField getSearchTextField() {
		return searchTextField;
	}

	public JTextArea getUserDescriptionTextArea() {
		return userDescriptionTextArea;
	}

	public JLabel getLblNWTS() {
		return lblNWTS;
	}

	public JLabel getLblNWNTS() {
		return lblNWNTS;
	}

	public JLabel getLblNNWTS() {
		return lblNNWTS;
	}

	public JLabel getLblNNWNTS() {
		return lblNNWNTS;
	}

	public JLabel getLblCNWTS() {
		return lblCNWTS;
	}

	public JLabel getLblCNWNTS() {
		return lblCNWNTS;
	}

	public JLabel getLblCNNWTS() {
		return lblCNNWTS;
	}

	public JLabel getLblCNNWNTS() {
		return lblCNNWNTS;
	}

	public JLabel getLblSetRealName() {
		return lblSetRealName;
	}

	public JLabel getLblsetUsername() {
		return lblsetUsername;
	}

	public JLabel getLblsetLocation() {
		return lblsetLocation;
	}

	public JLabel getLblsetListsCount() {
		return lblsetListsCount;
	}

	public JLabel getLblsetFollowersCount() {
		return lblsetFollowersCount;
	}

	public JLabel getLblsetFriendsCount() {
		return lblsetFriendsCount;
	}

	public JLabel getLblsetTweetsCount() {
		return lblsetTweetsCount;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JList getClassifiersList() {
		return classifiersList;
	}

	public JList getResultsList() {
		return resultsList;
	}

	public JLabel getLblProcessedTweets() {
		return lblProcessedTweets;
	}
	
	
}
