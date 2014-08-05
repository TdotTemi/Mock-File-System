package a2;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** A subclass of the item class
 *  Represents a file in the file system
 *  The file's contents can be modified.
 */
public class File extends Item{
	
	/** The content of the string */
	private String content = "";

	/** Creates a new file with the given name and parent directory
	 *  @param name String with file's name
	 *  @param parentDIr name of parent Directory
	 */
	//File constructor
	public File(String name, Directory parentDir){
		super(name, parentDir);
	}
	
	/**add content to current file
	 * 
	 * @param content to be added
	 */
	public void addContent(String content){
		this.content += content + "\n";
	}
	
	/**erase content of current file
	 */
	public void eraseContent(){
		this.content = "";
	}

	/**get content to current file
	 * 
	 * @return returns the contents of the file
	 */
	public String getFileContents(){
		return this.content;
	}
	/**
	 * This method takes a regex and looks for the pattern in a file. It returns the line(s) containing the pattern
	 * @param searchWord which represents the REGEX pattern
	 * @return found which represents the line(s) containing the regex
	 */
	public String searchFile(Pattern searchWord){
	    Scanner scanner = new Scanner(this.content);
	    String found = "";
	    while (scanner.hasNextLine()) { //now read the file line by line...
	        String line = scanner.nextLine();
	        Matcher m = searchWord.matcher(line); // searches line for the regex
	        if (m.find()) {  //if regex in line
	            found += line + "\n" ;
	        }
	    }return found;
	}

}
