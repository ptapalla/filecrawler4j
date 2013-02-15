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

import java.io.Serializable;

/**
 * This project is based on the open-source project "Crawler4j" 
 * by Yasser Ganjisaffar, and contains both duplicate and modified
 * portions of code from the "Crawler4j" project.
 * 
 * @author Paul Tapalla <ptapalla at uci dot edu>
 */
public class Counters implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String pathToDirectory;
	
	private boolean isFinished = false;
	
	private int maxDepth = -1;
	private int visitedDirs = 0;
	private int skippedDirs = 0;
	private int processedFiles = 0;
	private int skippedFiles = 0;
	
	protected void setPathToDirectory(String pathToDirectory) {
		this.pathToDirectory = pathToDirectory;
	}
	
	protected void setMaxDepth(int maxDepth) {
		if(this.maxDepth < maxDepth) {
			this.maxDepth = maxDepth;
		}
	}
	
	protected void incrementVisitedDirs() {
		visitedDirs++;
	}
	
	protected void incrementSkippedDirs() {
		skippedDirs++;
	}
	
	protected void incrementProcessedFiles() {
		processedFiles++;
	}
	
	protected void incrementSkippedFiles() {
		skippedFiles++;
	}
	
	protected String getPathToDirectory() {
		return pathToDirectory;
	}
	
	protected int getMaxDepth() {
		return maxDepth;
	}
	
	protected int getVisitedDirCount() {
		return visitedDirs;
	}
	
	protected int getSkippedDirCount() {
		return skippedDirs;
	}
	
	protected int getProcessedFileCount() {
		return processedFiles;
	}
	
	protected int getSkippedFileCount() {
		return skippedFiles;
	}
	
	protected boolean isFinished() {
		return isFinished;
	}
	
	protected void finished() {
		isFinished = true;
	}
}
