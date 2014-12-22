package de.uni.konstanz.csvtasks;

import java.io.File;

public interface CSVTask {
	
	/**
	 * Apply a task to a csv file from Andreas.
	 * @param path of the file
	 * @param outputDir if null, file will be put in the same dir
	 * @return the path of the output file.
	 */
	public File runTask( File path, File outputDir );

}
