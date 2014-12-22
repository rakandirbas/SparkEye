package de.uni.konstanz.voltgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class VoltLabelingEnvironmentController implements ActionListener, ItemListener,
PropertyChangeListener, ListSelectionListener {

	private VoltGUI view;
	private VoltModel model;
	private TweetResultsModel resultsModel;

	public VoltLabelingEnvironmentController(VoltGUI view, VoltModel model) {
		this.view = view;
		this.model = model;
		model.getChangeFirer().addPropertyChangeListener(this);
		resultsModel = new TweetResultsModel();
		buildEventHandlers();
	}

	public void buildEventHandlers() {
		view.getBtnBrowseForStreamFile().setActionCommand("BrowseForStreamFile");
		view.getBtnBrowseForStreamFile().addActionListener(this);
		view.getBtnStartStream().addItemListener(this);
		view.getBtnResetStream().setActionCommand("ResetStream");
		view.getBtnResetStream().addActionListener(this);
		view.getResultsList().setModel(getResultsModel());
		view.getResultsList().addListSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if ( actionCommand.equals("BrowseForStreamFile") ) {
			browseForStreamFile();
		}
		else if ( actionCommand.equals("ResetStream") ) {
		}

	}

	public void browseForStreamFile() {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			model.setStreamFile(file);
			view.getBtnStartStream().setEnabled(true);
			view.getBtnResetStream().setEnabled(true);
			view.getBtnNewsworthyAndTrusted().setEnabled(true);
			view.getBtnNewsworthyAndNotTrusted().setEnabled(true);
			view.getBtnNotNewsworthyAndTrusted().setEnabled(true);
			view.getBtnNotNewsworthyAndNotTrusted().setEnabled(true);
		}
		else if (model.getStreamFile() == null) {
			view.getBtnStartStream().setEnabled(false);
			view.getBtnResetStream().setEnabled(false);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if ( propertyName.equals("newTweet") ) {
			TweetResult result = (TweetResult) evt.getNewValue();
			resultsModel.addElement(result);
			view.getLblProcessedTweets().setText(String.format("Tweets: %d", resultsModel.getSize()));
		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();
		if ( source == view.getBtnStartStream() ) {
			if ( e.getStateChange() == e.SELECTED ) {
				view.getBtnStartStream().setText("Stop");
				model.setSearchQuery(view.getSearchTextField().getText());
				model.startStream();
			}
			else {
				view.getBtnStartStream().setText("Start");
				model.stopStream();
			}
		}
	}

	public TweetResultsModel getResultsModel() {
		return resultsModel;
	}

	public void setResultsModel(TweetResultsModel resultsModel) {
		this.resultsModel = resultsModel;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if ( e.getValueIsAdjusting() == false ) {
			if ( !( view.getResultsList().getSelectedIndex() == -1 ) ) {
				TweetResult result = (TweetResult) 
						view.getResultsList().getSelectedValue();
				view.getLblSetRealName().setText(result.getTweet().getUserRealName());
				view.getLblsetUsername().setText(result.getTweet().getUserScreenName());
				view.getLblsetLocation().setText(result.getTweet().getUserLocation());
				view.getLblsetListsCount().setText(String.format("%d", result.getTweet().getUserListedCount()));
				view.getLblsetFollowersCount().setText(String.format("%d",result.getTweet().getUserFollowersCount()));
				view.getLblsetFriendsCount().setText(String.format("%d",result.getTweet().getUserFriendsCount()));
				view.getLblsetTweetsCount().setText(String.format("%d",result.getTweet().getUserNumbTweets()));
				view.getUserDescriptionTextArea().setText(result.getTweet().getUserDescription());
			}
		}
	}


}










