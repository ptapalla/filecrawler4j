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

import edu.ics.uci.backend.Configurable;
import edu.ics.uci.backend.Frontier;
import edu.ics.uci.backend.IO;

/**
 * This project is based on the open-source project "Crawler4j" 
 * by Yasser Ganjisaffar, and contains both duplicate and modified
 * portions of code from the "Crawler4j" project.
 * 
 * @author Paul Tapalla <ptapalla at uci dot edu>
 */
public class CrawlController extends Configurable {
	
	private static Logger logger = Logger.getLogger(CrawlController.class.getName());
	
	protected Frontier frontier;
	
	/**
	 * Whether or not the crawling is finished.
	 */
	protected boolean finished;
	
	public CrawlController(CrawlConfig config) throws Exception {
		super(config);
		config.validate();
		File crawlStorageFolder = new File(config.getCrawlStorageFolder());
		if(!crawlStorageFolder.exists()) {
			if(!crawlStorageFolder.mkdirs()) {
				throw new Exception("Couldn't create this folder: " + crawlStorageFolder.getPath());
			}
		}
		File topDir = new File(config.getPathToDirectory());
		if(!topDir.exists()) {
			throw new Exception("This directory doesn't exist: " + topDir);
		}
		boolean resumable = config.isResumableCrawling();		
		if(!resumable) {
			IO.deleteFolderContents(crawlStorageFolder);
		}
		frontier = new Frontier(config);
		finished = false;
	}
	
	public <T extends FileCrawler> void start(final Class<T> _c) {
		try {
			T crawler = _c.newInstance();
			crawler.init(this);
			finished = true;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	protected Frontier getFrontier() {
		return frontier;
	}
	
	public int getProcessedFileCount() {
		return frontier.getProcessedFileCount();
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void shutDown() {
		logger.info("Controller is shutting down...");
		frontier.syncAll();
	}
}
