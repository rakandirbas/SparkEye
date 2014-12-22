package de.uni.konstanz.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.io.FilenameUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jidesoft.swing.RangeSlider;
import com.rakblog.DataBundle;
import com.rakblog.ParallelCoordinates;

import de.uni.konstanz.eventdescriptors.EventDescriptor;
import de.uni.konstanz.eventdescriptors.EventDescriptorType;
import de.uni.konstanz.eventdetection.DescriptorBounds;
import de.uni.konstanz.eventdetection.EventDetector;
import de.uni.konstanz.gui.server.AppState;

public class ApplicationWindow {

	private GUIController guiController;
	private ParallelCoordinates parallelCoordinates;
	private Map<EventDescriptorType, RangeSlider> descriptorsSlidersMap;
	private Map<EventDescriptorType, JCheckBox> descriptorsChkbxesMap;
	private AppState appState = AppState.CLEAN_START;
	private Timer countersTimer;
	private DetectorsListModel detectorsListModel;
	private DetectorsListRenderer detectorsListRendrr;
	private List<EventResult> eventResultsList;
	private EventsListModel eventsListModel;
	private EventsListRendrr eventsListRendrr;
	private DisplayTweetsListModel displayTweetsListModel;
	private DisplayTweetsListRendrr displayTweetsListRendrr;

	/******* GUI STUFF *******/
	private JFrame frmSparkeye;
	//private JButton pauseClusteringButton;
	private JPanel parallelCoordinatesPanel;
	private JPanel mapPanel;//
	private JPanel wordCloudPanel;
	private JPanel scatterplotPanel;
	private JPanel tweetsPanel;
	private JPanel descriptorsPanel;
	private JPanel buttomOptionsPanelForDetectors;
	private JTabbedPane vizTabbedPane;


	//Buttons
	private JButton showEventsButton;
	private JButton applyGlobalThresholdButton;
	private JToggleButton newDetectorButton;
	private JToggleButton updateDetectorButton;
	private JButton deleteDetectorButton;
	private JButton chooseDetectorColorButton;
	private JButton selectFileBrowseBtn;
	private JToggleButton startClusteringButton;
	private JButton stopClusteringButton;

	//Descriptors Sliders
	private RangeSlider objectivitySlider;
	private RangeSlider presentOrientedSlider;
	private RangeSlider pastOrientedSlider;
	private RangeSlider locationSupportSlider;
	private RangeSlider highlyRetweetedSlider;
	private RangeSlider mobileAuthoritySlider;
	private RangeSlider differentUsersSlider;
	private RangeSlider weirdCharsSlider;
	private RangeSlider isItQuestionedSlider;
	private RangeSlider emoticonsSlider;
	private RangeSlider swearingWordsSlider;
	private RangeSlider isItSlangySlider;
	private RangeSlider isItNotIntensifiedSlider;
	private RangeSlider geoMetaDataSlider;
	private RangeSlider longTweetsSupportSlider;
	private RangeSlider popularUsersSupportSlider;
	private RangeSlider activeUsersSupportSlider;
	private RangeSlider describedUsersSlider;
	private RangeSlider nearUsersSupportSlider;
	private RangeSlider hashtagDensitySlider;
	private RangeSlider urlDensitySlider;
	private RangeSlider isItConversationalSlider;
	private RangeSlider isItAPositiveTopicSlider;
	private RangeSlider isItANegativeTopicSlider;
	private RangeSlider isItANeutralTopicSlider;
	private RangeSlider isItPopularSlider;


	//Descriptors Checkboxes
	private JCheckBox objectivityChckbx;
	private JCheckBox presentOrientedChckbx;
	private JCheckBox pastOrientedChckbx;
	private JCheckBox locationSupportChckbx;
	private JCheckBox highlyRetweetedChckbx;
	private JCheckBox mobileAuthorityChckbx;
	private JCheckBox differentUsersChckbx;
	private JCheckBox weirdCharsChckbx;
	private JCheckBox isItQuestionedChckbx;
	private JCheckBox emoticonsChckbx;
	private JCheckBox swearingWordsChckbx;
	private JCheckBox isItSlangyChckbx;
	private JCheckBox isItNotIntensifiedChckbx;
	private JCheckBox geoMetaDataSupportChckbx;
	private JCheckBox longTweetsSupportChckbx;
	private JCheckBox popularUsersSupportChckbx;
	private JCheckBox activeUsersSupportChckbx;
	private JCheckBox describedUsersChckbx;
	private JCheckBox nearUsersSupportChckbx;
	private JCheckBox hashtaghDensityChckbx;
	private JCheckBox urlDensityChckbx;
	private JCheckBox isItConversationalChckbx;
	private JCheckBox isItANeutralTopicChckbx;
	private JCheckBox IsItAPositiveTopicChckbx;
	private JCheckBox IsItANegativeTopicChckbx;
	private JCheckBox isItPopularChckbx;

	//Spinners
	private JSpinner similarityThresholdSpinner;
	private JSpinner chunkSizeSpinner;
	private JSpinner clusterLiveTimeSpinner;
	private JSpinner refreshPeriodSpinner;
	private JSpinner burstinessSpinner;

	//Lists
	private JList displayEventsList;
	private JList displayTweetsList;
	private JList eventsDetectorsList;
	private JList detectorsList;

	//Checkboxes
	private JCheckBox automaticEventsDisplayChkbx;

	//Other
	RangeSlider globalThresholdSlider;
	JLabel windowsLbl;
	JLabel topicsLbl;
	JLabel burstyTopicsLbl;
	JFileChooser fileChooser;


	/******* END OF GUI STUFF *******/

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationWindow window = new ApplicationWindow();
					window.frmSparkeye.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ApplicationWindow() {
		initialize();
		myInit();
		guiEventsInit();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSparkeye = new JFrame();
		frmSparkeye.setTitle("SparkEye");
		frmSparkeye.setBounds(100, 100, 1141, 1083);
		frmSparkeye.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel topPanel = new JPanel();

		JPanel bottomPanel = new JPanel();
		frmSparkeye.getContentPane().setLayout(new BorderLayout(0, 0));
		topPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane topTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		topPanel.add(topTabbedPane, BorderLayout.CENTER);

		JPanel clusteringTabPanel = new JPanel();
		topTabbedPane.addTab("Clustering Tools", null, clusteringTabPanel, null);

		/**
		 * Beginning of Events Tools panel
		 */
		JPanel eventsTabPanel = new JPanel( new BorderLayout() );
		topTabbedPane.addTab("Events Tools", null, eventsTabPanel, null);

		JPanel eventsToolsInnerPanel = new JPanel();
		eventsTabPanel.add(eventsToolsInnerPanel, BorderLayout.CENTER);

		showEventsButton = new JButton("Inspect Events");

		automaticEventsDisplayChkbx = new JCheckBox("Automatic Events Display");
		automaticEventsDisplayChkbx.setVisible(false);

		JLabel lblRefreshTime = new JLabel("Refresh period:");
		lblRefreshTime.setVisible(false);

		refreshPeriodSpinner = new JSpinner();
		refreshPeriodSpinner.setVisible(false);
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

		/**
		 * End of Events Tools panel
		 */

		JLabel lblSelectFile = new JLabel("Select file:");

		selectFileBrowseBtn = new JButton("Browse");

		startClusteringButton = new JToggleButton("Start Clustering");

		//pauseClusteringButton = new JButton("Pause Clustering");

		stopClusteringButton = new JButton("Stop Clustering");

		JLabel lblSimilarityThreshold = new JLabel("Similarity threshold:");

		similarityThresholdSpinner = new JSpinner();

		JLabel lblClusterLiveTime = new JLabel("Cluster lifetime:");

		clusterLiveTimeSpinner = new JSpinner();

		JLabel lblChunkSize = new JLabel("Window size:");

		chunkSizeSpinner = new JSpinner();
		
		JLabel lblBurstinessThreshold = new JLabel("Burstiness Threshold:");
		
		burstinessSpinner = new JSpinner();

		windowsLbl = new JLabel("Windows:");

		topicsLbl = new JLabel("Topics:");
		burstyTopicsLbl = new JLabel("Bursty Topics:");
		GroupLayout gl_clusteringTabPanel = new GroupLayout(clusteringTabPanel);
		gl_clusteringTabPanel.setHorizontalGroup(
				gl_clusteringTabPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_clusteringTabPanel.createSequentialGroup()
						.addGroup(gl_clusteringTabPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_clusteringTabPanel.createSequentialGroup()
										.addComponent(startClusteringButton)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(stopClusteringButton))
										.addGroup(gl_clusteringTabPanel.createSequentialGroup()
												.addContainerGap()
												.addGroup(gl_clusteringTabPanel.createParallelGroup(Alignment.TRAILING, false)
														.addComponent(windowsLbl, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addGroup(Alignment.LEADING, gl_clusteringTabPanel.createSequentialGroup()
																.addComponent(lblSelectFile)
																.addPreferredGap(ComponentPlacement.RELATED)
																.addComponent(selectFileBrowseBtn)))
																.addPreferredGap(ComponentPlacement.RELATED)
																.addGroup(gl_clusteringTabPanel.createParallelGroup(Alignment.LEADING, false)
																		.addComponent(topicsLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(lblSimilarityThreshold, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addGroup(gl_clusteringTabPanel.createParallelGroup(Alignment.LEADING, false)
																		.addComponent(burstyTopicsLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(similarityThresholdSpinner, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addComponent(lblClusterLiveTime)
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addComponent(clusterLiveTimeSpinner, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addComponent(lblChunkSize)
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addComponent(chunkSizeSpinner, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addComponent(lblBurstinessThreshold)
																		.addComponent(burstinessSpinner, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)))
																		.addContainerGap(413, Short.MAX_VALUE))
				);
		gl_clusteringTabPanel.setVerticalGroup(
				gl_clusteringTabPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_clusteringTabPanel.createSequentialGroup()
						.addGroup(gl_clusteringTabPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(startClusteringButton)
								.addComponent(stopClusteringButton))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_clusteringTabPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblSelectFile)
										.addComponent(selectFileBrowseBtn)
										.addComponent(lblSimilarityThreshold)
										.addComponent(similarityThresholdSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblClusterLiveTime)
										.addComponent(clusterLiveTimeSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblChunkSize)
										.addComponent(chunkSizeSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblBurstinessThreshold)
										.addComponent(burstinessSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_clusteringTabPanel.createParallelGroup(Alignment.BASELINE)
												.addComponent(windowsLbl)
												.addComponent(topicsLbl)
												.addComponent(burstyTopicsLbl))
												.addContainerGap(26, Short.MAX_VALUE))
				);
		clusteringTabPanel.setLayout(gl_clusteringTabPanel);
		frmSparkeye.getContentPane().add(topPanel, BorderLayout.NORTH);
		frmSparkeye.getContentPane().add(bottomPanel, BorderLayout.CENTER);
		bottomPanel.setLayout(new BorderLayout(0, 0));

		JSplitPane centerSplitPane = new JSplitPane();
		centerSplitPane.setOneTouchExpandable(true);
		bottomPanel.add(centerSplitPane, BorderLayout.CENTER);

		JTabbedPane leftTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		centerSplitPane.setLeftComponent(leftTabbedPane);
		/**
		 * Beginning of the detectors panel
		 */
		JPanel detectorsPanel = new JPanel();
		leftTabbedPane.addTab("Detectors", null, detectorsPanel, null);
		detectorsPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane detectorsScrollPane = new JScrollPane();
		detectorsPanel.add(detectorsScrollPane, BorderLayout.CENTER);

		JPanel detectorsPanelToolsPanel = new JPanel();
		detectorsScrollPane.setViewportView(detectorsPanelToolsPanel);

		JScrollPane detectorsListScrollPane = new JScrollPane();

		JLabel label = new JLabel("Detectors list:");

		newDetectorButton = new JToggleButton("New Detector");

		updateDetectorButton = new JToggleButton("Edit");

		deleteDetectorButton = new JButton("Delete");

		JLabel label_1 = new JLabel("Color:");

		chooseDetectorColorButton = new JButton("Choose a color");

		JLabel lblDescriptorsList = new JLabel("Descriptors List:");

		JScrollPane descriptorsScrollPane = new JScrollPane();

		descriptorsPanel = new JPanel();
		descriptorsScrollPane.setViewportView(descriptorsPanel);

		isItPopularChckbx = new JCheckBox("1. Popularity:");

		JLabel lblThreshold = new JLabel("Threshold:");

		isItPopularSlider = new RangeSlider();
		isItPopularSlider.setPaintTicks(true);
		isItPopularSlider.setPaintLabels(true);
		descriptorsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("90px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("200px"),
				FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
				RowSpec.decode("35px"),
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("35px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		descriptorsPanel.add(isItPopularChckbx, "1, 1, 3, 1, left, top");
		descriptorsPanel.add(lblThreshold, "1, 3, right, top");
		descriptorsPanel.add(isItPopularSlider, "3, 3, fill, top");

		IsItAPositiveTopicChckbx = new JCheckBox("2. Positive sentiment density:");
		descriptorsPanel.add(IsItAPositiveTopicChckbx, "1, 5, 3, 1, left, top");

		JLabel lblThreshold_1 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_1, "1, 7, right, center");

		isItAPositiveTopicSlider = new RangeSlider();
		isItAPositiveTopicSlider.setPaintTicks(true);
		isItAPositiveTopicSlider.setPaintLabels(true);
		descriptorsPanel.add(isItAPositiveTopicSlider, "3, 7, fill, top");

		IsItANegativeTopicChckbx = new JCheckBox("3. Negative sentiment density:");
		descriptorsPanel.add(IsItANegativeTopicChckbx, "1, 9, 3, 1");

		JLabel lblThreshold_2 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_2, "1, 11, right, center");

		isItANegativeTopicSlider = new RangeSlider();
		isItANegativeTopicSlider.setPaintTicks(true);
		isItANegativeTopicSlider.setPaintLabels(true);
		descriptorsPanel.add(isItANegativeTopicSlider, "3, 11");

		isItANeutralTopicChckbx = new JCheckBox("4. Neutral sentiment density:");
		descriptorsPanel.add(isItANeutralTopicChckbx, "1, 13, 3, 1");

		JLabel lblThreshold_3 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_3, "1, 15, right, center");

		isItANeutralTopicSlider = new RangeSlider();
		isItANeutralTopicSlider.setPaintLabels(true);
		isItANeutralTopicSlider.setPaintTicks(true);
		descriptorsPanel.add(isItANeutralTopicSlider, "3, 15");

		isItConversationalChckbx = new JCheckBox("5. Conversations density:");
		descriptorsPanel.add(isItConversationalChckbx, "1, 17, 3, 1");

		JLabel lblThreshold_4 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_4, "1, 19, right, center");

		isItConversationalSlider = new RangeSlider();
		isItConversationalSlider.setPaintTicks(true);
		isItConversationalSlider.setPaintLabels(true);
		descriptorsPanel.add(isItConversationalSlider, "3, 19");

		urlDensityChckbx = new JCheckBox("6. URL density:");
		descriptorsPanel.add(urlDensityChckbx, "1, 21, 3, 1");

		JLabel lblThreshold_5 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_5, "1, 23, right, center");

		urlDensitySlider = new RangeSlider();
		urlDensitySlider.setPaintTicks(true);
		urlDensitySlider.setPaintLabels(true);
		descriptorsPanel.add(urlDensitySlider, "3, 23");
		////////////////
		hashtaghDensityChckbx = new JCheckBox("7. Hashtag density:");
		descriptorsPanel.add(hashtaghDensityChckbx, "1, 25, 3, 1");

		JLabel lblThreshold_6 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_6, "1, 27, right, center");

		hashtagDensitySlider = new RangeSlider();
		hashtagDensitySlider.setPaintTicks(true);
		hashtagDensitySlider.setPaintLabels(true);
		descriptorsPanel.add(hashtagDensitySlider, "3, 27");

		objectivityChckbx = new JCheckBox("8. Objectivity density:");
		descriptorsPanel.add(objectivityChckbx, "1, 29, 3, 1");

		JLabel lblThreshold_25 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_25, "1, 31, right, center");

		objectivitySlider = new RangeSlider();
		objectivitySlider.setPaintTicks(true);
		objectivitySlider.setPaintLabels(true);
		descriptorsPanel.add(objectivitySlider, "3, 31");

		presentOrientedChckbx = new JCheckBox("9. Presence orientation density:");
		descriptorsPanel.add(presentOrientedChckbx, "1, 33, 3, 1");

		JLabel lblThreshold_24 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_24, "1, 35, right, center");

		presentOrientedSlider = new RangeSlider();
		presentOrientedSlider.setPaintTicks(true);
		presentOrientedSlider.setPaintLabels(true);
		descriptorsPanel.add(presentOrientedSlider, "3, 35");

		pastOrientedChckbx = new JCheckBox("10. Past orientation density:");
		descriptorsPanel.add(pastOrientedChckbx, "1, 37, 3, 1");

		JLabel lblThreshold_23 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_23, "1, 39, right, center");

		pastOrientedSlider = new RangeSlider();
		pastOrientedSlider.setPaintTicks(true);
		pastOrientedSlider.setPaintLabels(true);
		descriptorsPanel.add(pastOrientedSlider, "3, 39");

		locationSupportChckbx = new JCheckBox("11. Location in text density:");
		descriptorsPanel.add(locationSupportChckbx, "1, 41, 3, 1");

		JLabel lblThreshold_22 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_22, "1, 43, right, center");

		locationSupportSlider = new RangeSlider();
		locationSupportSlider.setPaintTicks(true);
		locationSupportSlider.setPaintLabels(true);
		descriptorsPanel.add(locationSupportSlider, "3, 43");

		highlyRetweetedChckbx = new JCheckBox("12. Re-tweet density:");
		descriptorsPanel.add(highlyRetweetedChckbx, "1, 45, 3, 1");

		JLabel lblThreshold_21 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_21, "1, 47, right, center");

		highlyRetweetedSlider = new RangeSlider();
		highlyRetweetedSlider.setPaintTicks(true);
		highlyRetweetedSlider.setPaintLabels(true);
		descriptorsPanel.add(highlyRetweetedSlider, "3, 47");

		mobileAuthorityChckbx = new JCheckBox("13. Mobile authority density:");
		descriptorsPanel.add(mobileAuthorityChckbx, "1, 49, 3, 1");

		JLabel lblThreshold_20 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_20, "1, 51, right, center");

		mobileAuthoritySlider = new RangeSlider();
		mobileAuthoritySlider.setPaintTicks(true);
		mobileAuthoritySlider.setPaintLabels(true);
		descriptorsPanel.add(mobileAuthoritySlider, "3, 51");

		differentUsersChckbx = new JCheckBox("14. Different users support:");
		descriptorsPanel.add(differentUsersChckbx, "1, 53, 4, 1");

		JLabel lblThreshold_19 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_19, "1, 55, right, center");

		differentUsersSlider = new RangeSlider();
		differentUsersSlider.setPaintTicks(true);
		differentUsersSlider.setPaintLabels(true);
		descriptorsPanel.add(differentUsersSlider, "3, 55");

		weirdCharsChckbx = new JCheckBox("15. Weird characters saturation:");
		descriptorsPanel.add(weirdCharsChckbx, "1, 57, 4, 1");

		JLabel lblThreshold_18 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_18, "1, 59, right, center");

		weirdCharsSlider = new RangeSlider();
		weirdCharsSlider.setPaintTicks(true);
		weirdCharsSlider.setPaintLabels(true);
		descriptorsPanel.add(weirdCharsSlider, "3, 59");

		isItQuestionedChckbx = new JCheckBox("16. Questioning density:");
		descriptorsPanel.add(isItQuestionedChckbx, "1, 61, 3, 1");

		JLabel lblThreshold_17 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_17, "1, 63, right, center");

		isItQuestionedSlider = new RangeSlider();
		isItQuestionedSlider.setPaintTicks(true);
		isItQuestionedSlider.setPaintLabels(true);
		descriptorsPanel.add(isItQuestionedSlider, "3, 63");

		emoticonsChckbx = new JCheckBox("17. Emoticons saturation:");
		descriptorsPanel.add(emoticonsChckbx, "1, 65, 3, 1");

		JLabel lblThreshold_16 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_16, "1, 67, right, center");

		emoticonsSlider = new RangeSlider();
		emoticonsSlider.setPaintTicks(true);
		emoticonsSlider.setPaintLabels(true);
		descriptorsPanel.add(emoticonsSlider, "3, 67");

		swearingWordsChckbx = new JCheckBox("18. Swearing words saturation:");
		descriptorsPanel.add(swearingWordsChckbx, "1, 69, 4, 1");

		JLabel lblThreshold_15 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_15, "1, 71, right, center");

		swearingWordsSlider = new RangeSlider();
		swearingWordsSlider.setPaintTicks(true);
		swearingWordsSlider.setPaintLabels(true);
		descriptorsPanel.add(swearingWordsSlider, "3, 71");

		isItSlangyChckbx = new JCheckBox("19. Slanginess saturation:");
		descriptorsPanel.add(isItSlangyChckbx, "1, 73, 3, 1");

		JLabel lblThreshold_14 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_14, "1, 75, right, center");

		isItSlangySlider = new RangeSlider();
		isItSlangySlider.setPaintTicks(true);
		isItSlangySlider.setPaintLabels(true);
		descriptorsPanel.add(isItSlangySlider, "3, 75");

		isItNotIntensifiedChckbx = new JCheckBox("20. Intensification density:");
		descriptorsPanel.add(isItNotIntensifiedChckbx, "1, 77, 3, 1");

		JLabel lblThreshold_13 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_13, "1, 79, right, center");

		isItNotIntensifiedSlider = new RangeSlider();
		isItNotIntensifiedSlider.setPaintTicks(true);
		isItNotIntensifiedSlider.setPaintLabels(true);
		descriptorsPanel.add(isItNotIntensifiedSlider, "3, 79");

		geoMetaDataSupportChckbx = new JCheckBox("21. Geo-meta data density:");
		descriptorsPanel.add(geoMetaDataSupportChckbx, "1, 81, 4, 1");

		JLabel lblThreshold_12 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_12, "1, 83, right, center");

		geoMetaDataSlider = new RangeSlider();
		geoMetaDataSlider.setPaintTicks(true);
		geoMetaDataSlider.setPaintLabels(true);
		descriptorsPanel.add(geoMetaDataSlider, "3, 83");

		longTweetsSupportChckbx = new JCheckBox("22. Long tweets density:");
		descriptorsPanel.add(longTweetsSupportChckbx, "1, 85, 3, 1");

		JLabel lblThreshold_11 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_11, "1, 87, right, center");

		longTweetsSupportSlider = new RangeSlider();
		longTweetsSupportSlider.setPaintTicks(true);
		longTweetsSupportSlider.setPaintLabels(true);
		descriptorsPanel.add(longTweetsSupportSlider, "3, 87");

		popularUsersSupportChckbx = new JCheckBox("23. Popular users density:");
		descriptorsPanel.add(popularUsersSupportChckbx, "1, 89, 3, 1");

		JLabel lblThreshold_10 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_10, "1, 91, right, center");

		popularUsersSupportSlider = new RangeSlider();
		popularUsersSupportSlider.setPaintTicks(true);
		popularUsersSupportSlider.setPaintLabels(true);
		descriptorsPanel.add(popularUsersSupportSlider, "3, 91");

		activeUsersSupportChckbx = new JCheckBox("24. Active users density:");
		descriptorsPanel.add(activeUsersSupportChckbx, "1, 93, 3, 1");

		JLabel lblThreshold_9 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_9, "1, 95, right, center");

		activeUsersSupportSlider = new RangeSlider();
		activeUsersSupportSlider.setPaintTicks(true);
		activeUsersSupportSlider.setPaintLabels(true);
		descriptorsPanel.add(activeUsersSupportSlider, "3, 95");

		describedUsersChckbx = new JCheckBox("25. Described users density:");
		descriptorsPanel.add(describedUsersChckbx, "1, 97, 3, 1");

		JLabel lblThreshold_8 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_8, "1, 99, right, center");

		describedUsersSlider = new RangeSlider();
		describedUsersSlider.setPaintTicks(true);
		describedUsersSlider.setPaintLabels(true);
		descriptorsPanel.add(describedUsersSlider, "3, 99");
		///////////////
		nearUsersSupportChckbx = new JCheckBox("26. Near users density:");
		descriptorsPanel.add(nearUsersSupportChckbx, "1, 101, 3, 1");

		JLabel lblThreshold_7 = new JLabel("Threshold:");
		descriptorsPanel.add(lblThreshold_7, "1, 103, right, center");

		nearUsersSupportSlider = new RangeSlider();
		nearUsersSupportSlider.setPaintTicks(true);
		nearUsersSupportSlider.setPaintLabels(true);
		descriptorsPanel.add(nearUsersSupportSlider, "3, 103");
		///////////////
		//Mine:

		//////////////


		detectorsList = new JList();
		detectorsListScrollPane.setViewportView(detectorsList);

		buttomOptionsPanelForDetectors = new JPanel();
		JLabel globalThresholdLabel = new JLabel("Global threshold:");

		GroupLayout gl_detectorsPanelToolsPanel = new GroupLayout(detectorsPanelToolsPanel);
		gl_detectorsPanelToolsPanel.setHorizontalGroup(
				gl_detectorsPanelToolsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_detectorsPanelToolsPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(buttomOptionsPanelForDetectors, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
						.addGroup(gl_detectorsPanelToolsPanel.createSequentialGroup()
								.addGap(6)
								.addGroup(gl_detectorsPanelToolsPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_detectorsPanelToolsPanel.createSequentialGroup()
												.addComponent(descriptorsScrollPane, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
												.addContainerGap())
												.addGroup(gl_detectorsPanelToolsPanel.createSequentialGroup()
														.addGroup(gl_detectorsPanelToolsPanel.createParallelGroup(Alignment.LEADING)
																.addComponent(label)
																.addComponent(detectorsListScrollPane, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE)
																.addGroup(gl_detectorsPanelToolsPanel.createSequentialGroup()
																		.addComponent(label_1)
																		.addGap(6)
																		.addComponent(chooseDetectorColorButton))
																		.addComponent(lblDescriptorsList)
																		.addGroup(gl_detectorsPanelToolsPanel.createSequentialGroup()
																				.addComponent(newDetectorButton)
																				.addGap(6)
																				.addComponent(updateDetectorButton)
																				.addGap(6)
																				.addComponent(deleteDetectorButton)))
																				.addGap(4))))
				);
		gl_detectorsPanelToolsPanel.setVerticalGroup(
				gl_detectorsPanelToolsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_detectorsPanelToolsPanel.createSequentialGroup()
						.addGap(5)
						.addComponent(label)
						.addGap(5)
						.addComponent(detectorsListScrollPane, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
						.addGap(5)
						.addGroup(gl_detectorsPanelToolsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(newDetectorButton)
								.addComponent(updateDetectorButton)
								.addComponent(deleteDetectorButton))
								.addGap(5)
								.addGroup(gl_detectorsPanelToolsPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_detectorsPanelToolsPanel.createSequentialGroup()
												.addGap(6)
												.addComponent(label_1))
												.addComponent(chooseDetectorColorButton))
												.addGap(5)
												.addComponent(lblDescriptorsList)
												.addGap(5)
												.addComponent(descriptorsScrollPane, GroupLayout.PREFERRED_SIZE, 490, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(buttomOptionsPanelForDetectors, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
												.addGap(63))
				);

		globalThresholdSlider = new RangeSlider();
		globalThresholdSlider.setPaintTicks(true);
		globalThresholdSlider.setPaintLabels(true);

		applyGlobalThresholdButton = new JButton("Apply");
		GroupLayout gl_buttomOptionsPanelForDetectors = new GroupLayout(buttomOptionsPanelForDetectors);
		gl_buttomOptionsPanelForDetectors.setHorizontalGroup(
				gl_buttomOptionsPanelForDetectors.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttomOptionsPanelForDetectors.createSequentialGroup()
						.addGroup(gl_buttomOptionsPanelForDetectors.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_buttomOptionsPanelForDetectors.createSequentialGroup()
										.addGap(5)
										.addComponent(globalThresholdLabel))
										.addGroup(gl_buttomOptionsPanelForDetectors.createSequentialGroup()
												.addContainerGap()
												.addComponent(applyGlobalThresholdButton)))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(globalThresholdSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				);
		gl_buttomOptionsPanelForDetectors.setVerticalGroup(
				gl_buttomOptionsPanelForDetectors.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttomOptionsPanelForDetectors.createSequentialGroup()
						.addGroup(gl_buttomOptionsPanelForDetectors.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_buttomOptionsPanelForDetectors.createSequentialGroup()
										.addGap(16)
										.addComponent(globalThresholdLabel)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(applyGlobalThresholdButton))
										.addGroup(gl_buttomOptionsPanelForDetectors.createSequentialGroup()
												.addGap(5)
												.addComponent(globalThresholdSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
												.addContainerGap(8, Short.MAX_VALUE))
				);
		buttomOptionsPanelForDetectors.setLayout(gl_buttomOptionsPanelForDetectors);
		detectorsPanelToolsPanel.setLayout(gl_detectorsPanelToolsPanel);

		/**
		 * End of the detectors panel
		 */

		/***
		 * Beginning of events panel
		 */
		JPanel eventsPanel = new JPanel( new BorderLayout() );
		leftTabbedPane.addTab("Events", null, eventsPanel, null);
		JPanel innerEventsPanel = new JPanel();
		eventsPanel.add(innerEventsPanel, BorderLayout.CENTER);

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
								.addComponent(outerDisplayEventsPanel, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
								.addComponent(eventsDetectorsScrollPane, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDetectorsInEventsTab)
								.addComponent(lblEventsInEventsTab))
								.addContainerGap())
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
						.addComponent(outerDisplayEventsPanel, GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
						.addContainerGap())
				);
		outerDisplayEventsPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane displayEventsScrollPane = new JScrollPane();
		outerDisplayEventsPanel.add(displayEventsScrollPane, BorderLayout.CENTER);

		//JPanel displayEventsPanel = new JPanel();
		displayEventsList = new JList();
		displayEventsScrollPane.setViewportView(displayEventsList);

		eventsDetectorsList = new JList();
		eventsDetectorsScrollPane.setViewportView(eventsDetectorsList);
		eventsScrolPanePanel.setLayout(gl_eventsScrolPanePanel);

		/**
		 * End of events panel
		 */
		vizTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		centerSplitPane.setRightComponent(vizTabbedPane);

		parallelCoordinatesPanel = new JPanel( new BorderLayout() );
		LinkedHashSet<String> dimensions = new LinkedHashSet<String>();
		List<DataBundle> dataBundles = new LinkedList<DataBundle>();
		LinkedHashSet<String> dimensionsToHighlight = new LinkedHashSet<String>();
		parallelCoordinates = new ParallelCoordinates(dimensions,dataBundles, dimensionsToHighlight );
		parallelCoordinatesPanel.add(parallelCoordinates, BorderLayout.CENTER);
		vizTabbedPane.addTab("Parallel Coordinates", null, parallelCoordinatesPanel, null);

		mapPanel = new JPanel();
		vizTabbedPane.addTab("Map", null, mapPanel, null);

		wordCloudPanel = new JPanel();
		vizTabbedPane.addTab("Wordcloud", null, wordCloudPanel, null);

		scatterplotPanel = new JPanel();
		vizTabbedPane.addTab("Scatterplot Matrix", null, scatterplotPanel, null);

		tweetsPanel = new JPanel(new BorderLayout());
		JScrollPane displayTweetsScrollPane = new JScrollPane();
		displayTweetsList = new JList();
		displayTweetsScrollPane.setViewportView(displayTweetsList);
		tweetsPanel.add( displayTweetsScrollPane, BorderLayout.CENTER );
		vizTabbedPane.addTab("Tweets", null, tweetsPanel, null);
	}

	public void myInit()  {
		guiController = new GUIController(0.5);
		similarityThresholdSpinner.
		setModel( new SpinnerNumberModel(50, 0, 100, 1) );
		clusterLiveTimeSpinner.
		setModel( new SpinnerNumberModel(10, 0, 5000, 1) );
		chunkSizeSpinner.
		setModel( new SpinnerNumberModel(1000, 1, 100000, 1) );
		burstinessSpinner.
		setModel( new SpinnerNumberModel(100, 1, 100000, 1) );
		
		descriptorsSlidersMap =
				new LinkedHashMap<EventDescriptorType, RangeSlider>();
		descriptorsChkbxesMap = 
				new LinkedHashMap<EventDescriptorType, JCheckBox>();

		descriptorsSlidersMap.put(EventDescriptorType.OBJECTIVITY, objectivitySlider);
		descriptorsSlidersMap.put(EventDescriptorType.PRESENT_ORRIENTED, presentOrientedSlider);
		descriptorsSlidersMap.put(EventDescriptorType.PAST_ORIENTED, pastOrientedSlider);
		descriptorsSlidersMap.put(EventDescriptorType.LOCATION_SUPPORT, locationSupportSlider);
		descriptorsSlidersMap.put(EventDescriptorType.RETWEET_DENSTIY, highlyRetweetedSlider);
		descriptorsSlidersMap.put(EventDescriptorType.MOBILE_SUPPORT, mobileAuthoritySlider);
		descriptorsSlidersMap.put(EventDescriptorType.DIFFERENT_USERS, differentUsersSlider);
		descriptorsSlidersMap.put(EventDescriptorType.WEIRD_CHARS_SATURATION, weirdCharsSlider);
		descriptorsSlidersMap.put(EventDescriptorType.QUESTIONING, isItQuestionedSlider);
		descriptorsSlidersMap.put(EventDescriptorType.EMOTICONS_SATURATION, emoticonsSlider);
		descriptorsSlidersMap.put(EventDescriptorType.SWEARING_WORDS_SATURATION, swearingWordsSlider);
		descriptorsSlidersMap.put(EventDescriptorType.SLANGS_SATURATION, isItSlangySlider);
		descriptorsSlidersMap.put(EventDescriptorType.INTENSIFICATION, isItNotIntensifiedSlider);
		descriptorsSlidersMap.put(EventDescriptorType.GEO_META_SUPPORT, geoMetaDataSlider);
		descriptorsSlidersMap.put(EventDescriptorType.TWEETS_LENGTH, longTweetsSupportSlider);
		descriptorsSlidersMap.put(EventDescriptorType.POPULAR_USERS_SUPPORT, popularUsersSupportSlider);
		descriptorsSlidersMap.put(EventDescriptorType.ACTIVE_USERS_SUPPORT, activeUsersSupportSlider);
		descriptorsSlidersMap.put(EventDescriptorType.DESCRIBED_USERS_SUPPORT, describedUsersSlider);
		descriptorsSlidersMap.put(EventDescriptorType.NEAR_USERS_SUPPORT, nearUsersSupportSlider);
		descriptorsSlidersMap.put(EventDescriptorType.HASHTAG_DENSITY, hashtagDensitySlider);
		descriptorsSlidersMap.put(EventDescriptorType.URL_DENSITY, urlDensitySlider);
		descriptorsSlidersMap.put(EventDescriptorType.CONVERSATIONAL, isItConversationalSlider);
		descriptorsSlidersMap.put(EventDescriptorType.POSITIVE_SENTIMENT, isItAPositiveTopicSlider);
		descriptorsSlidersMap.put(EventDescriptorType.NEGATIVE_SENTIMENT, isItANegativeTopicSlider);
		descriptorsSlidersMap.put(EventDescriptorType.NEUTRAL_SENTIMENT, isItANeutralTopicSlider);
		descriptorsSlidersMap.put(EventDescriptorType.POPULAR, isItPopularSlider);


		descriptorsChkbxesMap.put(EventDescriptorType.OBJECTIVITY, objectivityChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.PRESENT_ORRIENTED, presentOrientedChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.PAST_ORIENTED, pastOrientedChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.LOCATION_SUPPORT, locationSupportChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.RETWEET_DENSTIY, highlyRetweetedChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.MOBILE_SUPPORT, mobileAuthorityChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.DIFFERENT_USERS, differentUsersChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.WEIRD_CHARS_SATURATION, weirdCharsChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.QUESTIONING, isItQuestionedChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.EMOTICONS_SATURATION, emoticonsChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.SWEARING_WORDS_SATURATION, swearingWordsChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.SLANGS_SATURATION, isItSlangyChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.INTENSIFICATION, isItNotIntensifiedChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.GEO_META_SUPPORT, geoMetaDataSupportChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.TWEETS_LENGTH, longTweetsSupportChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.POPULAR_USERS_SUPPORT, popularUsersSupportChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.ACTIVE_USERS_SUPPORT, activeUsersSupportChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.DESCRIBED_USERS_SUPPORT, describedUsersChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.NEAR_USERS_SUPPORT, nearUsersSupportChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.HASHTAG_DENSITY, hashtaghDensityChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.URL_DENSITY, urlDensityChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.CONVERSATIONAL, isItConversationalChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.NEUTRAL_SENTIMENT, isItANeutralTopicChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.POSITIVE_SENTIMENT, IsItAPositiveTopicChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.NEGATIVE_SENTIMENT, IsItANegativeTopicChckbx);
		descriptorsChkbxesMap.put(EventDescriptorType.POPULAR, isItPopularChckbx);

		for ( EventDescriptorType descriptorType : descriptorsSlidersMap.keySet() ) {
			RangeSlider slider = descriptorsSlidersMap.get(descriptorType);
			slider.setMinimum(0);
			slider.setMaximum(100);
			slider.setLowValue(0);
			slider.setHighValue(100);
			slider.setMajorTickSpacing(10);
			slider.setMinorTickSpacing(1);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			float fontSize = 8f;
			slider.setFont( slider.getFont().deriveFont(fontSize) );
		}
		globalThresholdSlider.setMinimum(0);
		globalThresholdSlider.setMaximum(100);
		//globalThresholdSlider.setValue(50);
		globalThresholdSlider.setLowValue(0);
		globalThresholdSlider.setHighValue(100);
		globalThresholdSlider.setMajorTickSpacing(10);
		globalThresholdSlider.setMinorTickSpacing(1);
		globalThresholdSlider.setPaintTicks(true);
		globalThresholdSlider.setPaintLabels(true);
		float fontSize = 8f;
		globalThresholdSlider.setFont( globalThresholdSlider.getFont().deriveFont(fontSize) );

		startClusteringButton.setEnabled(false);
		stopClusteringButton.setEnabled(false);

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		detectorsListModel = new DetectorsListModel();
		detectorsListRendrr = new DetectorsListRenderer();

		detectorsList.setModel(detectorsListModel);
		eventsDetectorsList.setModel(detectorsListModel);

		detectorsList.setCellRenderer(detectorsListRendrr);
		eventsDetectorsList.setCellRenderer(detectorsListRendrr);

		updateDetectorButton.setEnabled(false);
		deleteDetectorButton.setEnabled(false);

		eventsListModel = new EventsListModel();
		eventsListRendrr = new EventsListRendrr();
		displayEventsList.setModel(eventsListModel);
		displayEventsList.setCellRenderer(eventsListRendrr);

		detectorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		eventsDetectorsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		displayTweetsListModel = new DisplayTweetsListModel();
		displayTweetsListRendrr = new DisplayTweetsListRendrr();
		displayTweetsList.setModel(displayTweetsListModel);
		displayTweetsList.setCellRenderer(displayTweetsListRendrr);

	}

	public void guiEventsInit() {
		selectFileBrowseBtn.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(null);
				File file = fileChooser.getSelectedFile();
				String ext = "";

				if ( file != null ) {
					ext = FilenameUtils.getExtension(file.getAbsolutePath());
				}

				if ( (returnVal == JFileChooser.APPROVE_OPTION) && ext.equals("csv") ) {
					startClusteringButton.setEnabled(true);
					stopClusteringButton.setEnabled(true);
					guiController.setCsvFileLocation(file);
				}
				else {
					startClusteringButton.setEnabled(false);
					stopClusteringButton.setEnabled(false);
				}
			}
		} );

		startClusteringButton.addItemListener( new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() == ItemEvent.SELECTED ) {
					if ( appState == AppState.CLEAN_START) {
						appState = AppState.RUNNING;
						stopClusteringButton.setEnabled(false);
						startClusteringButton.setText("Pause Clustering");
						guiController.setBurstinessThreshold(getBurstinessThreshold());
						guiController.startClustering(getWindowSize(),
								getSimilarityThreshold(), getClusterLivetime());
						countersTimer.start();
					}
					else if ( appState == AppState.PAUSED ) {
						appState = AppState.RUNNING;
						stopClusteringButton.setEnabled(false);
						startClusteringButton.setText("Pause Clustering");
						guiController.continueClustering();
						countersTimer.start();
					}
				}
				else {
					appState = AppState.PAUSED;
					stopClusteringButton.setEnabled(true);
					startClusteringButton.setText("Continue Clustering");
					guiController.pauseClustering();
					countersTimer.stop();
					burstyTopicsLbl.setText("Bursty Topics: " + guiController.getNumberOfBurstyClusters());
				}
			}
		} );

		stopClusteringButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				appState = AppState.CLEAN_START;
				startClusteringButton.setText("Start Clustering");
				guiController.cancelClustering();
				countersTimer.stop();
				resetCounters();
				displayEventsList.clearSelection();
				vizTabbedPane.repaint();
			}
		} );

		countersTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				windowsLbl.setText("Windows: " + guiController.getNumberOfWindows());
				topicsLbl.setText("Topics: " + guiController.getNumberOfClusters());
				//burstyTopicsLbl.setText("Bursty Topics: " + guiController.getNumberOfBurstyClusters());
				//System.out.println("Bursty clusters: " + guiController.getNumberOfBurstyClusters());
			}
		});
		countersTimer.setRepeats(true);

		newDetectorButton.addItemListener( new NewDetectorItemListener() );
		applyGlobalThresholdButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int lowVal = globalThresholdSlider.getLowValue();
				int highVal = globalThresholdSlider.getHighValue();
				setGlobalThreshold(lowVal, highVal);
			}
		} );

		chooseDetectorColorButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (  ( !detectorsList.isSelectionEmpty() ) && 
						( newDetectorButton.isSelected() || updateDetectorButton.isSelected() ) ) {
					EventDetector eventDetector = 
							(EventDetector) detectorsList.getSelectedValue();
					Color newColor = JColorChooser.showDialog(null, "Choose Detector Color", Color.BLACK);

					if ( newColor != null ) {
						eventDetector.setColor(newColor);
						detectorsListModel.editElement(eventDetector);
						chooseDetectorColorButton.setForeground(newColor);
					}

				}
			}
		} );

		updateDetectorButton.addItemListener( new EditDetectorItemListener() );
		detectorsList.addListSelectionListener( new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					if ( !detectorsList.isSelectionEmpty() ) {
						EventDetector eventDetector = 
								(EventDetector) detectorsList.getSelectedValue();
						populateDescriptorsList(eventDetector);
						chooseDetectorColorButton.setForeground(eventDetector.getColor());

						if ( !newDetectorButton.isSelected() ) {
							updateDetectorButton.setEnabled(true);
							deleteDetectorButton.setEnabled(true);
						}

					}
					else {
						resetDescriptorsList();
						chooseDetectorColorButton.setForeground(Color.BLACK);
						updateDetectorButton.setEnabled(false);
						deleteDetectorButton.setEnabled(false);
					}
				}
			}
		} );

		deleteDetectorButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ( !detectorsList.isSelectionEmpty() ) {
					EventDetector eventDetector = 
							(EventDetector) detectorsList.getSelectedValue();
					detectorsListModel.removeElement(eventDetector);
					guiController.removeDetector(eventDetector);
				}
			}
		} );

		showEventsButton.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				eventResultsList = guiController.getEventResults();
			}
		} );

		eventsDetectorsList.addListSelectionListener( new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					JList list = (JList) e.getSource();
					if ( !list.isSelectionEmpty() ) {
						List<EventResult> toPresentEventResults = new 
								LinkedList<EventResult>();
						//NOW build the event results based on the selected detectors
						//Then update the list model of displayEventsList

						//If multiple selection
						if ( list.getSelectedValuesList().size() > 1 ) {
							List<EventDetector> newEventDetectors = 
									new LinkedList<EventDetector>();

							for ( Object object : list.getSelectedValuesList() ) {
								EventDetector eventDetector = (EventDetector) object;
								newEventDetectors.add(eventDetector);
							}


							for ( EventResult eventResult : eventResultsList ) {
								boolean toAdd = true;
								EventDetector eventDetector = null;
								for ( Object object : list.getSelectedValuesList() ) {
									eventDetector = (EventDetector) object;
									if ( !eventResult.getEventDetectors().contains(eventDetector) ) {
										toAdd = false;
									}
								}

								boolean sameDetectorsNumber = false;

								//To get only event that were detected *unqiuly* by
								//the selected detectors. So if there another one that detected 
								//the event and not selected, then the event is not present.
								if ( list.getSelectedValuesList().size() == eventResult.getEventDetectors().size() ) {
									sameDetectorsNumber = true;
								}

								if (toAdd && sameDetectorsNumber && ( eventDetector != null )) {
									EventResult newEventResult = new
											EventResult(eventResult.getEvent(), 
													eventResult.getCluster(), newEventDetectors,
													eventResult.getTweetsDao(), eventResult.getColor());
									toPresentEventResults.add(newEventResult);
								}
							}

							//This is to add events that were uniquly found by the detector
							for ( Object object : list.getSelectedValuesList() ) {
								EventDetector eventDetector = (EventDetector) object;
								for ( EventResult eventResult : eventResultsList ) {
									if ( eventResult.getEventDetectors().contains(eventDetector)
											&& (eventResult.getEventDetectors().size() == 1) ) {
										toPresentEventResults.add(eventResult);
									}
								}

							}

							eventsListModel.setEventResults(toPresentEventResults);
						}
						//If only one detector is selected
						else {
							for ( Object object : list.getSelectedValuesList() ) {
								EventDetector eventDetector = (EventDetector) object;
								for ( EventResult eventResult : eventResultsList ) {
									if ( eventResult.getEventDetectors().contains(eventDetector) ) {
										List<EventDetector> newEventDetectors = 
												new LinkedList<EventDetector>();
										newEventDetectors.add(eventDetector);
										EventResult newEventResult = 
												new EventResult(eventResult.getEvent(),
														eventResult.getCluster(), newEventDetectors,
														eventResult.getTweetsDao(), eventDetector.getColor());
										toPresentEventResults.add(newEventResult);
									}
								}

							}

							eventsListModel.setEventResults(toPresentEventResults);
						}


					}
				}
			}
		} );

		displayEventsList.addListSelectionListener( new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					JList displayEventsList = (JList) e.getSource();

					if ( getSelectedVizTab().equals("Tweets") ) {
						if ( displayEventsList.getSelectedValuesList().size() == 1 ) {
							EventResult eventResult = (EventResult) displayEventsList.getSelectedValue();
							displayTweetsListModel.setTweets(eventResult);
						}
					}
					else if ( getSelectedVizTab().equals("Parallel Coordinates")  ) {
						visualizeParallelCoordinates();
					}
				}
			}

		});
		
		KeyListener listKeyListener = new ListKeyListener();
		displayEventsList.addKeyListener(listKeyListener);
		eventsDetectorsList.addKeyListener(listKeyListener);
		detectorsList.addKeyListener(listKeyListener);


	}//END of GUI events initialization
	
	class ListKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
				JList list = (JList) e.getSource();
				list.clearSelection();
			}
		}
		
	}

	public void visualizeParallelCoordinates() {
		LinkedHashSet<String> dimensions = new LinkedHashSet<String>();
		List<DataBundle> dataBundles = new LinkedList<DataBundle>();
		List<EventDetector> selectedEventDetectors = new LinkedList<EventDetector>();
		Map<EventDetector, DataBundle> detectorsDataBundles =
				new LinkedHashMap<EventDetector, DataBundle>();

		//		for ( EventDescriptorType eventDescriptorType : EventDescriptorType.values() ) {
		//			String dim = GUIController.eventDescriptorsTypesShortcuts.get(eventDescriptorType);
		//			dimensions.add(dim);
		//		}

		dimensions = guiController.getDimensions();

		if ( eventsDetectorsList.getSelectedValuesList().size() > 1 ) {
			/* Create data bundles for each detector to 
			 * hold the records/events that were only detected by this detector 
			 */
			for ( Object object : eventsDetectorsList.getSelectedValuesList() ) {
				EventDetector eventDetector = (EventDetector) object;
				selectedEventDetectors.add(eventDetector);
				List<Map<String,Double>> valuesList = new 
						LinkedList<Map<String,Double>>();
				DataBundle dataBundle = new DataBundle(eventDetector.getName(), valuesList, eventDetector.getColor());
				dataBundles.add(dataBundle);
				detectorsDataBundles.put(eventDetector, dataBundle);
			}

			/*
			 * Create a data bundle to hold the records/events that
			 * were detected by all the selected detectors
			 */
			List<Map<String,Double>> intersectionValuesList = new 
					LinkedList<Map<String,Double>>();
			DataBundle intersectionDataBundle = new
					DataBundle("Intersection", intersectionValuesList, Color.YELLOW);
			dataBundles.add(intersectionDataBundle);

			//Generate rows for the selected events/records
			for ( Object object : displayEventsList.getSelectedValuesList() ) {
				EventResult eventResult = (EventResult) object;
				/*
				 * Generate rows for the records that are unique for each detector
				 */
				if (eventResult.getEventDetectors().size() == 1) {
					EventDetector eventDetector = eventResult.getEventDetectors().get(0);
					DataBundle dataBundle = detectorsDataBundles.get(eventDetector);
					Map<String, Double> row = new LinkedHashMap<String, Double>();
					for ( EventDescriptorType eventDescType : EventDescriptorType.values() ) {
						EventDescriptor eventDesc = eventResult.getEvent().getDescriptorsMap().get(eventDescType);
						row.put(GUIController.eventDescriptorsTypesShortcuts.get(eventDescType), eventDesc.getScore());
						dataBundle.getValuesList().add(row);
					}
				}
				/*
				 * Generate rows for the records that intersection from all detectors
				 */
				else if ( eventResult.getEventDetectors().size() == selectedEventDetectors.size() ) {
					Map<String, Double> row = new LinkedHashMap<String, Double>();
					for ( EventDescriptorType eventDescType : EventDescriptorType.values() ) {
						EventDescriptor eventDesc = eventResult.getEvent().getDescriptorsMap().get(eventDescType);
						row.put(GUIController.eventDescriptorsTypesShortcuts.get(eventDescType), eventDesc.getScore());
						intersectionDataBundle.getValuesList().add(row);
					}
				}
			}
			parallelCoordinates.setDimensions(dimensions);
			parallelCoordinates.setDataBundles(dataBundles);
			vizTabbedPane.repaint();
		}
		else if (eventsDetectorsList.getSelectedValuesList().size() == 1) {
			EventDetector eventDetector = (EventDetector) eventsDetectorsList.getSelectedValue();
			List<Map<String,Double>> valuesList = new 
					LinkedList<Map<String,Double>>();
			DataBundle dataBundle = new DataBundle(eventDetector.getName(), valuesList, eventDetector.getColor());
			dataBundles.add(dataBundle);
			for ( Object object : displayEventsList.getSelectedValuesList() ) {
				EventResult eventResult = (EventResult) object;
				Map<String, Double> row = new LinkedHashMap<String, Double>();
				for ( EventDescriptorType eventDescType : EventDescriptorType.values() ) {
					EventDescriptor eventDesc = eventResult.getEvent().getDescriptorsMap().get(eventDescType);
					row.put(GUIController.eventDescriptorsTypesShortcuts.get(eventDescType), eventDesc.getScore());
					//System.out.printf("Adding %s = %f\n", GUIController.eventDescriptorsTypesShortcuts.get(eventDescType), eventDesc.getScore());
				}
				//System.out.println("\n");
				dataBundle.getValuesList().add(row);
			}

			List<String> dimsToHL = new ArrayList<String>();
			LinkedHashSet<String> dimensionsToHighlight = new LinkedHashSet<String>();
			for ( EventDescriptorType eventDescType : eventDetector.getDescriptorsTypes().keySet() ) {
				//dimensionsToHighlight.add(GUIController.eventDescriptorsTypesShortcuts.get(eventDescType));
				dimsToHL.add(GUIController.eventDescriptorsTypesShortcuts.get(eventDescType));
			}

			List<Integer> intDimsToHL = new LinkedList<Integer>();
			for ( String hlDim :  dimsToHL) {
				intDimsToHL.add(Integer.parseInt(hlDim));
			}

			Collections.sort(intDimsToHL);

			for ( int dimHL : intDimsToHL ) {
				dimensionsToHighlight.add( String.format("%d", dimHL) );
			}

			parallelCoordinates.setDimensions(dimensions);
			parallelCoordinates.setDataBundles(dataBundles);
			if ( !eventDetector.getName().equals("All Topics") ) {
				parallelCoordinates.highlightDimensions(dimensionsToHighlight);
			}
			else {
				parallelCoordinates.highlightDimensions(new LinkedHashSet<String>());
			}
			vizTabbedPane.repaint();
		}
	}//END of parallel coordinates viz

	private class EditDetectorItemListener implements ItemListener {
		EventDetector eventDetector = null;

		public void itemStateChanged(ItemEvent e) {
			if ( e.getStateChange() == ItemEvent.SELECTED &&
					!detectorsList.isSelectionEmpty() ) {

				newDetectorButton.setEnabled(false);
				deleteDetectorButton.setEnabled(false);

				updateDetectorButton.setText("Save");
				eventDetector = 
						(EventDetector) detectorsList.getSelectedValue();
			}
			else {
				//Now save when dis_selected
				if ( eventDetector != null ) {
					newDetectorButton.setEnabled(true);
					deleteDetectorButton.setEnabled(true);
					updateDetectorButton.setText("Edit");
					Map<EventDescriptorType, DescriptorBounds> descriptorsTypes = 
							new LinkedHashMap<EventDescriptorType, DescriptorBounds>();
					for ( EventDescriptorType descriptorType : EventDescriptorType.values() ) {
						if ( descriptorsChkbxesMap.get(descriptorType).isSelected() ) {
							int sliderLowVal = descriptorsSlidersMap.get(descriptorType).getLowValue();
							int sliderHighVal = descriptorsSlidersMap.get(descriptorType).getHighValue();
							double lowValue = (double) sliderLowVal / 100;
							double highValue = (double) sliderHighVal / 100;
							DescriptorBounds descriptorBounds = new DescriptorBounds(lowValue, highValue);
							descriptorsTypes.put(descriptorType, descriptorBounds);

						}
					}
					eventDetector.setDescriptorsTypes(descriptorsTypes);
					eventResultsList = guiController.updateEventResults();
				}

				eventDetector = null;
			}
		}
	}

	private class NewDetectorItemListener implements ItemListener {
		String detectorName = null;
		EventDetector eventDetector = null;
		@Override
		public void itemStateChanged(ItemEvent e) {
			if ( e.getStateChange() == ItemEvent.SELECTED ) {
				updateDetectorButton.setEnabled(false);
				deleteDetectorButton.setEnabled(false);
				detectorName = JOptionPane.showInputDialog(null,
						"Enter detector name: ", "New Detector",
						JOptionPane.PLAIN_MESSAGE);

				if ( detectorName != null ) {
					newDetectorButton.setText("Save Detector");
					Map<EventDescriptorType, DescriptorBounds> descriptorsTypes = 
							new LinkedHashMap<EventDescriptorType, DescriptorBounds>();
					eventDetector = 
							guiController.newDetector(detectorName,
									Color.BLACK, descriptorsTypes);
					detectorsListModel.addElement(eventDetector);
					int index = detectorsListModel.getElementIndex(eventDetector);
					detectorsList.setSelectedIndex(index);
				}
				else {
					newDetectorButton.setText("New Detector");
					newDetectorButton.setSelected(false);
				}

			}
			else {
				//When dis_selected, then save
				newDetectorButton.setText("New Detector");
				if ( detectorName != null && eventDetector != null ) {
					updateDetectorButton.setEnabled(true);
					deleteDetectorButton.setEnabled(true);
					Map<EventDescriptorType, DescriptorBounds> descriptorsTypes = 
							new LinkedHashMap<EventDescriptorType, DescriptorBounds>();
					for ( EventDescriptorType descriptorType : EventDescriptorType.values() ) {
						if ( descriptorsChkbxesMap.get(descriptorType).isSelected() ) {
							int sliderLowVal = descriptorsSlidersMap.get(descriptorType).getLowValue();
							int sliderHighVal = descriptorsSlidersMap.get(descriptorType).getHighValue();
							double lowValue = (double) sliderLowVal / 100;
							double highValue = (double) sliderHighVal / 100;
							DescriptorBounds descriptorBounds = new DescriptorBounds(lowValue, highValue);
							descriptorsTypes.put(descriptorType, descriptorBounds);
						}
					}
					eventDetector.setDescriptorsTypes(descriptorsTypes);
					eventResultsList = guiController.updateEventResults();
				}

				detectorName = null;
				eventDetector = null;
			}
		}

	}

	private void populateDescriptorsList( EventDetector eventDetector ) {
		for ( EventDescriptorType descriptorType :  EventDescriptorType.values() ) {
			JCheckBox checkbox = descriptorsChkbxesMap.get(descriptorType);
			RangeSlider slider = descriptorsSlidersMap.get(descriptorType);
			if ( eventDetector.getDescriptorsTypes().containsKey(descriptorType) ) {
				checkbox.setSelected(true);
				DescriptorBounds descriptorBounds = eventDetector.getDescriptorsTypes().get(descriptorType);
				int lowValue = (int) (descriptorBounds.getLowValue() * 100);
				int highValue = (int) (descriptorBounds.getHighValue() * 100);
				slider.setLowValue(lowValue);
				slider.setHighValue(highValue);
			}
			else {
				checkbox.setSelected(false);
				slider.setLowValue(0);
				slider.setHighValue(100);
			}
		}
	}

	public void resetDescriptorsList() {
		for ( EventDescriptorType descriptorType :  EventDescriptorType.values() ) {
			descriptorsChkbxesMap.get(descriptorType).setSelected(false);
			descriptorsSlidersMap.get(descriptorType).setLowValue(0);
			descriptorsSlidersMap.get(descriptorType).setHighValue(100);
		}
	}

	public void setGlobalThreshold( int lowValue, int highValue ) {
		for ( EventDescriptorType descriptorType : descriptorsSlidersMap.keySet() ) {
			RangeSlider slider = descriptorsSlidersMap.get(descriptorType);
			slider.setLowValue(lowValue);
			slider.setHighValue(highValue);
		}
	}

	public Map<EventDescriptorType, RangeSlider> getDescriptorsSliders() {
		return descriptorsSlidersMap;
	}

	public Map<EventDescriptorType, JCheckBox> getDescriptorsChkbxes() {
		return descriptorsChkbxesMap;
	}

	public double getSimilarityThreshold() {
		int spinnerValue = (int) similarityThresholdSpinner.getValue();
		double threshold = (double) spinnerValue / 100;
		return threshold;
	}

	public int getClusterLivetime() {
		return (int) clusterLiveTimeSpinner.getValue();
	}
	
	public int getBurstinessThreshold() {
		return (int) burstinessSpinner.getValue();
	}

	public int getWindowSize() {
		return (int) chunkSizeSpinner.getValue();
	}

	public String getSelectedVizTab() {
		int index = vizTabbedPane.getSelectedIndex();
		if ( index != -1 ) {
			return vizTabbedPane.getTitleAt(index);
		}
		return "non";
	}

	public void resetCounters() {
		windowsLbl.setText("Windows: ");
		topicsLbl.setText("Topics: ");
	}
}











