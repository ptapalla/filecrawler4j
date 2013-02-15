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

import java.io.*;

import org.apache.log4j.Logger;

/**
 * This project is based on the open-source project "Crawler4j" 
 * by Yasser Ganjisaffar, and contains both duplicate and modified
 * portions of code from the "Crawler4j" project.
 * 
 * @author Paul Tapalla <ptapalla at uci dot edu>
 */
public class IO {
	
	private static Logger logger = Logger.getLogger(IO.class.getName());

	public static boolean deleteFolder(File folder) {
		return deleteFolderContents(folder) && folder.delete();
	}
	
	public static boolean deleteFolderContents(File folder) {
		System.out.println("Deleting content of: " + folder.getAbsolutePath());
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (!file.delete()) {
					return false;
				}
			} else {
				if (!deleteFolder(file)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void save(Object o, String filename) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
		} catch(IOException e) {
			logger.error("Caught IOException trying to save: " + filename);
		}
	}
	
	public static Object load(String filename) {
		Object toReturn = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(filename);
			ois = new ObjectInputStream(fis);
			toReturn = ois.readObject();
		} catch(IOException e) {
			logger.error("Caught IOException trying to load: " + filename);
		} catch(ClassNotFoundException e) {
			logger.error("Caught ClassNotFoundException trying to load: " + filename);
		}
		return toReturn;
	}
	
	public static boolean exists(String filename) {
		File file = new File(filename);
		return file.exists();
	}
}