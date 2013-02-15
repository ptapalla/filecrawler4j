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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This project is based on the open-source project "Crawler4j" 
 * by Yasser Ganjisaffar, and contains both duplicate and modified
 * portions of code from the "Crawler4j" project.
 * 
 * @author Paul Tapalla <ptapalla at uci dot edu>
 */
public abstract class Properties {
	
	protected File file;
	protected int depth;
	
	protected Properties(File file, int depth) {
		this.file = file;
		this.depth = depth;
	}

	public File getFile() {
		return file;
	}
	
	public String getName() {
		return file.getName();
	}
	
	public String getPath() {
		return file.getPath();
	}
	
	public String getParent() {
		return file.getParent();
	}
	
	public File getParentFile() {
		return file.getParentFile();
	}
	
	public int getDepth() {
		return depth;
	}
	
	public boolean isHidden() {
		return file.isHidden();
	}
	
	public boolean delete() {
		return file.delete();
	}
	
	public void deleteOnExit() {
		file.deleteOnExit();
	}
	
	public InputStream getInputStream() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fis;
	}
}
