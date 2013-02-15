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

package edu.ics.uci.backend;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.ics.uci.frontend.CrawlConfig;

/**
 * This project is based on the open-source project "Crawler4j" 
 * by Yasser Ganjisaffar, and contains both duplicate and modified
 * portions of code from the "Crawler4j" project.
 * 
 * @author Paul Tapalla <ptapalla at uci dot edu>
 */
public class Frontier extends Configurable {
	
	private static final Logger logger = Logger.getLogger(Frontier.class.getName());
	
	private Counters counters;
	private ArrayList<Integer> seenFiles;
	
	private boolean resumed;
	
	public Frontier(CrawlConfig config) throws Exception {
		super(config);
		resumed = false;
		if(config.isResumableCrawling()) {
			loadData();
		} else {
			counters = new Counters();
			seenFiles = new ArrayList<Integer>();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadData() throws Exception {
		counters = null;
		if(IO.exists(config.getCrawlStorageFolder() + "/counters.ser")) {
			counters = (Counters) IO.load(config.getCrawlStorageFolder() + "/counters.ser");
			if(!counters.getPathToDirectory().equalsIgnoreCase(config.getPathToDirectory())) {
				String counterPath = counters.getPathToDirectory();
				String configPath = config.getPathToDirectory();
				throw new Exception("\nThe config's top path (" + configPath + ") is not the same " +
				"as the path from the previous crawl: " + counterPath + ".\nSpecify the same path " +
				"or use a different crawl storage folder.");
			}
			resumed = true;
		} else {
			counters = new Counters();
			counters.setPathToDirectory(config.getPathToDirectory());
		}
		seenFiles = null;
		if(IO.exists(config.getCrawlStorageFolder() + "/files.ser")) {
			seenFiles = (ArrayList<Integer>) IO.load(config.getCrawlStorageFolder() + "/files.ser");
			resumed = true;
		} else {
			seenFiles = new ArrayList<Integer>();
		}
	}
	
	public void incrementVisitedDirs() {
		counters.incrementVisitedDirs();
	}
	
	public void incrementSkippedDirs() {
		counters.incrementSkippedDirs();
	}
	
	public int getSeenDirCount() {
		return counters.getVisitedDirCount();
	}
	
	public int getSkippedDirCount() {
		return counters.getSkippedDirCount();
	}
	
	public void incrementProcessedFiles() {
		counters.incrementProcessedFiles();
	}
	
	public void incrementSkippedFiles() {
		counters.incrementSkippedFiles();
	}
	
	public String getPathToDirectory() {
		return counters.getPathToDirectory();
	}
	
	public int getProcessedFileCount() {
		return counters.getProcessedFileCount();
	}
	
	public int getSkippedFileCount() {
		return counters.getSkippedFileCount();
	}
	
	public int getSeenFileCount() {
		return seenFiles.size();
	}
	
	public boolean isFinished() {
		return counters.isFinished();
	}
	
	public boolean isResumed() {
		return resumed;
	}
	
	public boolean seen(int hash) {
		return seenFiles.contains(hash);
	}

	
	public void add(int hash) {
		seenFiles.add(hash);
	}
	
	public void setMaxDepth(int maxDepth) {
		counters.setMaxDepth(maxDepth);
	}
	
	public int getMaxDepth() {
		return counters.getMaxDepth();
	}
	
	public void finished() {
		counters.finished();
		logger.info("Frontier signaled finished");
	}
		
	public void syncCounters() {
		if(config.isResumableCrawling()) {
			IO.save(counters, config.getCrawlStorageFolder() + "/counters.ser");
		}
	}
	
	public void syncFiles() {
		if(config.isResumableCrawling()) {
			IO.save(seenFiles, config.getCrawlStorageFolder() + "/files.ser");
		}
	}
	
	public void syncAll() {
		syncCounters();
		syncFiles();
	}
}
