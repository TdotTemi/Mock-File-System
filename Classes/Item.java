package a2;

import java.util.ArrayList;

/** The parent class of Directory and File classes
 *  Represents any item in the file system
 *  Contains all method and fields common to both classes
 *  Holds the contents of the directory and has methods to add and remove items 
 */

public class Item implements Cloneable {
	
	/** Stores the item's name*/
	private String itemName;
	
	/** Stores the item's subitems*/
	protected ArrayList<Item> subItems = new ArrayList<Item>();
	
	/** Stores the item's parent directory*/ 
	protected Directory parentDir;

	/**Constructor of Item
	 * Creates a new item with the given name and parent directory
	 *  @param name  String representing the name of the item
	 *  @param parentDir the parent directory of the item
	 */
	public Item(String name, Directory parentDir){
		this.itemName = name;
		this.setParentDir(parentDir);
		
		// If we are not in the root
		if (this.parentDir != null) {
			this.parentDir.addItem(this);
		}
	}
	
	/**Method sets the parent directory of an item
	 *  @param dir  The new parent directory of the item
	 */
	public void setParentDir(Directory dir){
		this.parentDir = dir;
	}

	/**Get an item's parent directory*/
	public Directory getParentDir(){
		return this.parentDir;
	}


	/**Method adds an item i to the items subitems array 
	 *  @param i item to be added
	 */
	public void addItem(Item i){
		this.subItems.add(i);
		i.parentDir = (Directory)this;
	}
	
	/**Method removes an item i from the items subitems array 
	 *  @param i item to be removed
	 */	
	public void removeItem(Item i){
		this.subItems.remove(i);
		i.parentDir = null;
	}

	/**Gets an item's name*/	
	public String getName() {
		return this.itemName;
	}
	
	/**
	 * Get whole path of an Item,including the roots.
	 *  @return String filePath this is the path of the item
	 */
	public String getWholePath(){
		String filePath = "";
        String i = this.itemName;
        Directory p = this.parentDir;//the path of the directoy is saved as the Dir, current dir in commands ?just a var path since not instatiated dir
        while (!i.equals("/") && !p.equals(null)) { //stops when gets to root
            filePath = "/" + i + filePath;
            i = p.getName();
            p = p.parentDir;
        }if (filePath.equals("")){
        filePath = "/";
        }
        return filePath;
	}
	
	/**Prints the name of all items in the current items subitem list*/
	public void getSubItemName() {
		for (Item i : this.subItems){
			System.out.println(i.getClass());
		}
	}
	
	/**Returns subitem array list of an item items.
	 *  @return subitems the ArrayList containing the items sub-elements
	 */
	public ArrayList<Item> getSubItems(){
		return subItems;
	}

	/**Method gets the sub-element in the subitem list that matches the string name.
	 *  @param String name the name of the sub-element to be retrieved
	 *  @return Item the item with the name 
	 */
	public Item getSubItem(String name) {
		for (Item i : this.subItems) { //loop thr subDir 
			if ( i.getName().equals(name)) {// if a dir // if name of subdirectory name == String name
				return (Item)i;// return the subdir with name
			}
		}
		return null;
	}

	/** Method makes the item cloneable.*/
	public Directory clone() {
		try{
			return (Directory)super.clone();
		}
		
		catch(CloneNotSupportedException e){
			return null;
		}
	}
}
