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

import java.io.Serializable;

import edu.ics.uci.backend.Parser;

/**
 * This project is based on the open-source project "Crawler4j" 
 * by Yasser Ganjisaffar, and contains both duplicate and modified
 * portions of code from the "Crawler4j" project.
 * 
 * @author Paul Tapalla <ptapalla at uci dot edu>
 */
public class CrawlConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the Frontier file where crawl data will be stored.
	 * By default, saves in the current working directory.
	 */
	private String crawlStorageFolder;
	
	/**
	 * The path of the directory where crawling will take place.
	 */
	private String pathToDirectory;
	
	/**
	 * The name of the directory where crawling will take place.
	 */
	private String topDirectory;
	
	/**
	 * If enabled, crawl data will be saved in the crawl storage folder.
	 * Crawls can then be continued where left off.
	 */
	private boolean resumableCrawling = false;
	
	/**
	 * If enabled, crawl hidden files.
	 */
	private boolean crawlHiddenFiles = false;
	
	/**
	 * If enabled, print crawl statistics when finished
	 */
	private boolean printStats = false;
	
	/**
	 * How long to wait (in milliseconds) before processing a file.
	 */
	private long delay = 100;
	
	/**
	 * Validate these configurations
	 */
	protected void validate() throws Exception {
		if(crawlStorageFolder == null) {
			throw new Exception("Frontier filename is not set in CrawlConfig");
		}
		if(pathToDirectory == null) {
			throw new Exception("Path to directory is not set in CrawlConfig");
		} else {
			if(!pathToDirectory.endsWith("/")) {
				pathToDirectory = pathToDirectory + "/";
			}
			topDirectory = Parser.getActualTopDirectory(pathToDirectory);
		}
		if(delay < 0) {
			throw new Exception("Delay cannot be less than 0");
		}
	}
	
	public void setCrawlStorageFolder(String crawlStorageFolder) {
		this.crawlStorageFolder = crawlStorageFolder;
	}
	
	public void setPathToDirectory(String pathToDirectory) {
		this.pathToDirectory = pathToDirectory;
	}
	
	public void setResumableCrawling(boolean resumableCrawling) {
		this.resumableCrawling = resumableCrawling;
	}
	
	public void setCrawlHiddenFiles(boolean crawlHiddenFiles) {
		this.crawlHiddenFiles = crawlHiddenFiles;
	}
	
	public void setPrintStats(boolean printStats) {
		this.printStats = printStats;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public String getCrawlStorageFolder() {
		return crawlStorageFolder;
	}
	
	public String getPathToDirectory() {
		return pathToDirectory;
	}
	
	public String getTopDirectory() {
		return topDirectory;
	}
	
	public boolean isResumableCrawling() {
		return resumableCrawling;
	}
	
	public boolean isCrawlHiddenFiles() {
		return crawlHiddenFiles;
	}
	
	public boolean isPrintStats() {
		return printStats;
	}
	
	public long getDelay() {
		return delay;
	}
}
