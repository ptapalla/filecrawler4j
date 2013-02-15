/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.ics.uci.frontend;

import java.io.File;

import org.apache.log4j.Logger;

import edu.ics.uci.backend.DirProperties;
import edu.ics.uci.backend.FileProperties;
import edu.ics.uci.backend.Frontier;

/**
 * This project is based on the open-source project "Crawler4j" 
 * by Yasser Ganjisaffar, and contains both duplicate and modified
 * portions of code from the "Crawler4j" project.
 * 
 * @author Paul Tapalla <ptapalla at uci dot edu>
 */
public abstract class FileCrawler {
	
	private static Logger logger = Logger.getLogger(FileCrawler.class.getName());
	
	/**
	 * This crawler's controller
	 */
	private CrawlController crawlController;
	
	/**
	 * The Frontier where data is stored for this crawl.
	 */
	private Frontier frontier;
	
	/**
	 * The top directory (name only, no other use)
	 */
	private String topDirectory;
	
	/**
	 * The directory where the crawling beings.
	 */
	private String pathToDirectory;
	
	/**
	 * CrawlConfig settings
	 */
	private boolean crawlHiddenFiles;
	
	/**
	 * The delay between processing files.
	 */
	private long delay;

	/**
	 * Initialize the crawler.
	 */
	protected void init(CrawlController crawlController) {
		this.crawlController = crawlController;
		frontier = crawlController.getFrontier();
		topDirectory = frontier.getConfig().getTopDirectory();
		pathToDirectory = frontier.getConfig().getPathToDirectory();
		crawlHiddenFiles = frontier.getConfig().isCrawlHiddenFiles();
		delay = frontier.getConfig().getDelay();
		logger.info("Crawler is ready");
		if(frontier.isResumed()) {
			System.out.println("Files processed so far: " + frontier.getProcessedFileCount());
		}
		onStart();
		crawl();
		onFinish();
		if(frontier.getConfig().isPrintStats()) {
			printStats();
		}
	}
	
	private void printStats() {
		System.out.println("===Crawl Statistics===");
		System.out.println("-Max depth detected: " + frontier.getMaxDepth());
		System.out.println("-Directories visited: " + frontier.getSeenDirCount());
		System.out.println("-Directories skipped: " + frontier.getSkippedDirCount());
		System.out.println("-Files seen: " + frontier.getSeenFileCount());
		System.out.println("-Files processed: " + frontier.getProcessedFileCount());
		System.out.println("-Files skipped: " + frontier.getSkippedFileCount());
	}
	
	/**
	 * Begin the crawl
	 */
	private void crawl() {
		System.out.println("Crawling: " + topDirectory);
		File dir = new File(pathToDirectory);
		recursiveProcedure(dir, 0);
		if(frontier.isFinished()) {
			frontier.finished();
		}
		logger.info("Crawler is done");
	}
	
	/**
	 * A sub-routine for the crawl() method.
	 * Recursively go through the top directory and process files.
	 */
	private void recursiveProcedure(File file, int depth) {
		// Determine if this is the deepest depth.
		frontier.setMaxDepth(depth);
		// If this file is a directory...
		if(file.isDirectory()) {
			if(file.isHidden() && crawlHiddenFiles) {
				/*
				 * The directory is hidden and we're allowed to process it.
				 * shouldVisit returned true.
				 */
				directoryProcedure(file, depth);
			} else if(!file.isHidden()) {
				/*
				 * The directory is not hidden.
				 */
				directoryProcedure(file, depth);
			} else {
				/*
				 * You're here because the directory is hidden
				 * and we're not allowed to visit hidden directories.
				 */
				frontier.incrementSkippedDirs();
			}
		} else if(file.isFile()) {
			/* If this is a file, run the fileProcedure.
			 * The depth of this file would be one greater than the current depth.
			 */
			fileProcedure(file, depth+1);
		}
	}
	
	/**
	 * Visit a directory if allowed to, process it if specified.
	 * Process traverse through all sub directories and files.
	 */
	private void directoryProcedure(File dir, int depth) {
		if(shouldVisit(dir.getPath(), depth)) {
			DirProperties dirProperties = new DirProperties(dir, depth);
			visit(dirProperties);
			frontier.incrementVisitedDirs();
			File[] subDirs = dir.listFiles();
			if(subDirs.length > 0) {
				depth++;
				for(int i = 0; i < subDirs.length; i++) {
					recursiveProcedure(subDirs[i], depth);
				}
			}
		} else {
			frontier.incrementSkippedDirs();
		}
	}
	
	/**
	 * Determine if we've seen this file.
	 * If we have, skip it.
	 * If we haven't, process it, if we're allowed to.
	 */
	private void fileProcedure(File file, int depth) {
		int hash = file.getAbsolutePath().hashCode();
		if(!frontier.seen(hash)) {
			delay();
			FileProperties fileProperties = new FileProperties(file, depth);
			if(file.isHidden() && crawlHiddenFiles) {
				if(shouldProcess(file.getPath(), depth)) {
					/*
					 * The file is hidden and we're allowed to process it.
					 * shouldProcess returned true.
					 */
					process(fileProperties);
					frontier.incrementProcessedFiles();
				}
			} else if(!file.isHidden()) {
				if(shouldProcess(file.getPath(), depth)) {
					/*
					 * The file is not hidden and shouldProcess returned true.
					 */
					process(fileProperties);
					frontier.incrementProcessedFiles();
				}
			} else {
				/*
				 * You're here because the file is hidden
				 * and we're not allowed to process hidden files.
				 */
				frontier.incrementSkippedFiles();
			}
			frontier.add(hash);
		} else {
			//TODO maybe notify user of files skipped?
			//We don't need to increment skipped files because we did it above.
		}
	}
	
	/**
	 * Pause the crawl for the specified delay
	 */
	private void delay() {
		long end = System.currentTimeMillis()+delay;
		while(System.currentTimeMillis()<end);
	}
	
	/**
	 * By default, visits all directories.
	 * This can be edited to visit directories based on your crawling logic.
	 */
	public boolean shouldVisit(String path, int depth) {
		/*
		 * TODO implement your crawling logic
		 */
		return true;
	}
	
	/**
	 * This allows you to do whatever you please with each directory visited.
	 * Use with caution as permanent data loss is possible if used incorrectly.
	 */
	public void visit(DirProperties dirProperties) {
		/*
		 * TODO implement your crawling logic
		 */
	}
	
	/**
	 * By default, processes all files.
	 * This can be edited to process files based on your crawling logic.
	 */
	public boolean shouldProcess(String path, int depth) {
		/*
		 * TODO implement your crawling logic
		 */
		return true;
	}
	
	/**
	 * This allows you to do whatever you please with each file crawled.
	 * Use with caution as permanent data loss is possible if used incorrectly.
	 */
	public void process(FileProperties fileProperties) {
		/*
		 * TODO implement your processing technique
		 */
	}
	
	/**
	 * What do you want to do before the crawler starts crawling?
	 */
	public void onStart() {
		/*
		 * TODO implement your starting method
		 */
	}
	
	/**
	 * What do you want to do when the crawler is done crawling?
	 */
	public void onFinish() {
		/*
		 * TODO implement your finishing method
		 */
	}
	
	/**
	 * WARNING: This method is experimental!
	 * This will allow you to save the changes to the Frontier's
	 * work queue whenever you want. It is best used within the process method.
	 * You may choose to save at specific intervals or when you've processed a certain number of files.
	 * Calling this method too often will likely corrupt your crawl data.
	 * 
	 * TODO: Find a faster way to serialize the data.
	 */
	public void sync() {
		frontier.syncAll();
	}
	
	/**
	 * Get this crawler's controller.
	 */
	public CrawlController getController() {
		return crawlController;
	}
}
