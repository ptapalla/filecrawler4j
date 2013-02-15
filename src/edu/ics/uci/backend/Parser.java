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

/**
 * This project is based on the open-source project "Crawler4j" 
 * by Yasser Ganjisaffar, and contains both duplicate and modified
 * portions of code from the "Crawler4j" project.
 * 
 * @author Paul Tapalla <ptapalla at uci dot edu>
 */
public class Parser {
	
	public static String getActualTopDirectory(String topDirectory) {
		String actualTopDir = "";
		String[] split = topDirectory.split("/");
		if(split.length == 1) {
			actualTopDir = topDirectory;
		} else if(split.length > 1) {
			actualTopDir = "/"+split[split.length-1]+"/";
		}
		return actualTopDir;
	}
	
	public static String getShortPath(String top, String path) {
		return "/"+path.replaceFirst(top, "").trim();

	}
	
	public static String getFileExtension(String filename) {
		String extension = "";
		if(filename.charAt(0) == '.') {
			extension = "hidden";
		} else {
			if(filename.indexOf(".") == -1) {
				extension = "unknown";
			} else {
				int lastDot = filename.lastIndexOf(".");
				int filenameLength = filename.length();
				extension = filename.substring(lastDot, filenameLength);
			}
		}
		return extension.toLowerCase();
	}
}
