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

import java.io.File;

/**
 * This project is based on the open-source project "Crawler4j" 
 * by Yasser Ganjisaffar, and contains both duplicate and modified
 * portions of code from the "Crawler4j" project.
 * 
 * @author Paul Tapalla <ptapalla at uci dot edu>
 */
public class DirProperties extends Properties {
	
	public DirProperties(File file, int depth) {
		super(file, depth);	
	}
	
	/**
	 * Return an array of type File of the files inside
	 * this directory. 
	 */
	public File[] listFiles() {
		return file.listFiles();
	}
	
	/**
	 * Return an array of type String containing the names
	 * of the files inside of this directory. 
	 */
	public String[] list() {
		return file.list();
	}
	
	/**
	 * Return the number of files inside of this directory.
	 */
	public int getFileCount() {
		return file.list().length;
	}
}
