package de.uni.konstanz.csvtasks;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;


public class CSVToApp {
	public static void main(String[] args) {
		File inputdir = new File("/Users/rockyrock/Desktop/SyriaTweets/in");
		File outputdir = new File("/Users/rockyrock/Desktop/SyriaTweets/out");
		List<CSVTask> tasksToDo = new LinkedList<CSVTask>();
		tasksToDo.add( new CSVToEnglish() );
		//tasksToDo.add( new CSVToSorted() );
		//tasksToDo.add( new CSVToWordsCount() );
		//tasksToDo.add( new CSVToArabic() );
		runTasks(inputdir, outputdir, tasksToDo);
	}

	public static void runTasks(File inputDirectory, File outputDirectory,
			List<CSVTask> tasksToDo) {

		if ( !inputDirectory.isDirectory() ) {
			System.err.println("The specified path is not a directory");
			return;
		}
		else {
			for ( File fileInDir : inputDirectory.listFiles() ) {
				if ( fileInDir.isFile() && 
						FilenameUtils.getExtension(fileInDir.getAbsolutePath()).equals("csv") ) {
					File processedFile = fileInDir;
					//Apply a chain of tasks to each csv file.
					for ( CSVTask task : tasksToDo ) {
						System.out.printf("Applying task %s to file %s.\n", 
								task.toString(), processedFile.getAbsoluteFile());
						long t0 = System.currentTimeMillis();
						processedFile = task.runTask(processedFile, outputDirectory);
						long t1 = System.currentTimeMillis();
						System.out.printf("Task took %d seconds to complete.\n", (t1-t0)/1000);
					}
				}
			}

		}

	}
}
