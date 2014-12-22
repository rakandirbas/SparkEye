package de.uni.konstanz.gui.server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import de.uni.konstanz.models.Cluster;
import de.uni.konstanz.models.Tweet;
import de.uni.konstanz.utils.ClustersUtils;

public class TestGUI implements ActionListener, ItemListener {

	private ExecutorService executor = Executors.newCachedThreadPool();
	private BlockingQueue<Tweet> buffer = new LinkedBlockingQueue<Tweet>(100000);
	private volatile AppState appState = AppState.CLEAN_START;

	File csvFileLocation = new 
			File("/Users/rockyrock/Desktop/testCSV/2013_04_19_22_filtered_sorted.csv");

	private GTweetsProducer producer;
	private GTweetsConsumer consumer;

	//GUI stuff
	static JToggleButton startBtn;
	static JButton stopBtn;
	static JButton showBtn;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		TestGUI gController = new TestGUI();
		JFrame frame = new JFrame("GUI Concurrency");
		frame.setPreferredSize( new Dimension(400, 200));
		frame.setLayout( new FlowLayout() );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		startBtn = new JToggleButton("Start");
		startBtn.addItemListener(gController);

		stopBtn = new JButton("Cancel");
		stopBtn.setEnabled(false);
		stopBtn.setActionCommand("Cancel");
		stopBtn.addActionListener(gController);

		showBtn = new JButton("Show");
		showBtn.setActionCommand("Show");
		showBtn.addActionListener(gController);

		frame.getContentPane().add(startBtn);
		frame.getContentPane().add(stopBtn);
		frame.getContentPane().add(showBtn);

		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		System.out.println(command + " is clicked");

		if ( command.equals("Cancel") ) {
			startBtn.setText("Start");
			appState = AppState.CLEAN_START;

			producer.die();
			consumer.die();

			synchronized (producer) {
				producer.notify();
			}
			synchronized (consumer) {
				consumer.notify();
			}

			//Block here until they are both dead;
			while( !producer.isSynched() ) {
				//System.out.println("Producer Not Synched yet in cancel.");
			}
			
			while( !consumer.isSynched()  ) {
				//System.out.println("Consumer Not Synched yet in cancel.");
			}
			System.out.println("App is clean");
			//TODO CLEAN THE BUFFER ALSO!!
			buffer.clear();
			executor.shutdown();
		}
		else if ( command.equals("Show") ) {
			System.out.println(producer.getInnerState() +", " +producer.getState());
			int size = buffer.size();
			System.out.println(size);
			if ( size > 0 ) {
				for ( int i = 0; i < 10; i++ ) {
					//System.out.println( buffer.poll().text );
				}
			}
			
			System.out.println("Clusters size:" + consumer.getLeaderFollowerClusters().size());
			System.out.println("ChunksCounter:"+ consumer.getChuncksCounter());
			
			for ( Long clusterID : consumer.getLeaderFollowerClusters().keySet() ) {
				System.out.println("################NEXT##############");
				Cluster cluster  = consumer.getLeaderFollowerClusters().get(clusterID);
				Map<Long, Tweet> tweets = consumer.getTweetsInClustersDao().getTweetsInClusters();
				for ( Tweet tweet : ClustersUtils.getClusterTweets(cluster, tweets) ) {
					System.out.println(tweet.getText());
				}
			}
			
		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if ( e.getStateChange() == ItemEvent.SELECTED ) {

			if ( appState == AppState.CLEAN_START) {
				System.out.println("Start");
				startBtn.setText("Pause");
				appState = AppState.RUNNING;
				executor = Executors.newCachedThreadPool();
				//buffer = new LinkedBlockingQueue<Tweet>(100000);
				producer = new GTweetsProducer(buffer, csvFileLocation);
				consumer = new GTweetsConsumer(buffer, 1000, 0.4, 10, 10);
				executor.execute( producer );
				executor.execute( consumer );
				executor.shutdown();
				stopBtn.setEnabled(false);
			}
			//Now continue execution
			else if ( appState == AppState.PAUSED ) {
				System.out.println("Continue");
				appState = AppState.RUNNING;
				producer.wakeup();
				synchronized (producer) {
					producer.notify();
				}
				
				consumer.wakeup();
				synchronized (consumer) {
					consumer.notify();
				}

				startBtn.setText("Pause");
				stopBtn.setEnabled(false);
			}
		}
		else {
			System.out.println("Pause");
			startBtn.setText("Continue");
			appState = AppState.PAUSED;

			producer.pause();
			consumer.pause();

			//Block the app here until they are really waiting
			while( !producer.isSynched() ) {
				//System.out.println("Producer Not synched in paused yet!");
			}
			while( !consumer.isSynched() ) {
				//System.out.println("Consumer Not synched in paused yet!");
			}
			stopBtn.setEnabled(true);
		}
	}

}
