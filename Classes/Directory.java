package a2;

import a2.Directory;
import a2.File;
import a2.Item;

/**A subclass of item class.
 * Represents a directory in the file system.
 * The directory's contents can be viewed .
 */
public class Directory extends Item{
	
	/** Creates a new directory with the given name and parent directory.
	 *  @param dir String with directory's name
	 *  @param parentDIr name of parent Directory
	 */
	//Directory constructor
	public Directory(String dir, Directory parentDir){
		super(dir, parentDir);
	}
	
	/**
	 * Get sub directories with the same name as name.
	 * @param name: name of directory we want to get
	 * @return return the directory if it exists, return null if it does not exists
	 */
	public Directory getSubDir(String name) {
		for (Item i : this.subItems) {
			if (i instanceof Directory && i.getName().equals(name)) {
				return (Directory)i;
			}
		}
		return null;
	}

	/**
	 * Get sub files with the same name as name.
	 * @param name: name of file we want to get
	 * @return return the File if it exists, return null if it does not exists
	 */
	public File getSubFile(String name) {
		for (Item i : this.subItems) {
			if (i instanceof File && i.getName().equals(name)) {
				return (File)i;
			}
		}
		return null;
	}
}
