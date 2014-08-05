package a2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/** This class holds all methods and helper functions for commands*/
public class Command {
	
	private static final String NL = System.getProperty("line.separator");
	
	/**INSTANTIATE**/
	/**Creates an empty constructor of the class Command*/
	public Command () {
		;
	}
	
	
    /** 
     * Method that exits the program.
     */
    public void exit() {
        System.exit(0);
    }
    
    
	/** 
	 * Method that makes directories object.
     * Makes and names directories according to the user input. 
     * Can make several directories at once. 
     * Can not make a directory if there exist a directory or file object in CURRENT directory with the same name.
     * In which case prints an error message.
     *
     * @param directoryNames Array representation of the user input 
     */
    //Method always has access to current directory
    public void mkdir(String[] directoryNames, Directory currentDir, 
    		Directory root){
    	// loop over every path the user inputs
    	for (int i=1; i<directoryNames.length;i++){
    		String[] inputs = directoryNames[i].split("/");
    		// check if the name entered by the user is valid
    		if (inputs.length==0||inputs[inputs.length-1].equals(".")||
    				inputs[inputs.length-1].equals("..")){
    			System.out.println("Invalid name");
    		}else{
    			// check if the directory exists
	    		if (getFileItem(currentDir,root,inputs,-1) != null){
	    			System.out.println("Directory or file already exists");
	    		}else{
	    			if (inputs.length == 1){
	    				//create a new directory
	    				Directory dir = new Directory(inputs[0], currentDir);
	    			}else{
	    				//check if the path is valid
	    				//i.e if each of the dir in the path exists
	    				Item target = getFileItem(currentDir,root,inputs,-2);
	    				if (target == null || target instanceof File){
	    					System.out.println("Invalid path");
	    				}else{
							Directory dir = new Directory
							(inputs[inputs.length-1], (Directory)target);
	    				}
	    			}
	    		}
    		}
    	}
    }
	
	/** 
	 * Method that changes current directory to user specified directory.
     * Can only change directory if user enters a valid directory name 
     * 		(i.e: newDirectory must exist in the
     * current directory).
     * If directory does not exist, prints an error message.
     * Change directory may also move the user back to the previous directory 
     * 		given the input ".." or stay in the same direcotry given input "."
     *
     * @param newDir the name of the directory, "..", or "."
     *  sets the new directory as the current directory

     */
	public Item cd (String newDir, Directory currentDir, Directory root) {
		// take the user input and split them into array
		String[] inputs = newDir.split("/");
		Item activeDir = getFileItem(currentDir, root, inputs, -1);
		if (activeDir instanceof File){
		        System.out.println(activeDir.getName() + " is a file");
		}else if (activeDir == null){
			System.out.println("No such directory");
		}
		else{
			//call use helper function 
			//only cd will return current Directory
			currentDir = (Directory) activeDir; 
		}
		return currentDir;
	}
	
    /**
     * Method that prints the content of a folder or a file.
     * 	If no paths are given, print the contents (file or directory) 
     *      of the current directory with a new line.
     * If input specifies a file, print input
     * If input specifies a directory, print input, a colon, then the 
     * 		contents of that directory, then an extra new line. 
     * If input does not exist, print a suitable message
     * Path can be absolute(full path) or relative(specific file /directory)
     * 
     * @param inputParts Array representation of the user input (depending 
     * on user, could be empty or may contain path)
     */
     public void ls(String[] input, Directory currentDir, Directory root) {
    	//initialize a print object to save the things that printed by ls
    	Print print = new Print();
    	int length = input.length;
    	String out = "", outFile = "";
    	if (length > 2){ // if the input contains things other than ls
    		// if the input has > OUTFILE or >> OUTFILE
    		if (input[length-2].equals(">")||input[length-2].equals(">>")){
    			length -= 2;
    			out = input[length];
    			outFile = input[length+1];
    		}
    	}if (length == 1){ // if the input is ls
    		// get the sub items of current directory
            for (int i = 0; i < currentDir.subItems.size(); i++){
                print.buff(currentDir.subItems.get(i).getName());
            }
        } else if (length == 2 && input[1].equals("-R")){ // if input == "ls -R"
            recursivelyGetSubItems(currentDir, print);
        } else {
            if (input[1].equals("-R")){
            	//calls the helper function of ls with recursion == true
            	checkR(length, input, currentDir, root, true, print);
            }else{
            	//calls the helper function of ls with recursion == false
            	checkR(length, input, currentDir, root, false, print);
            }
        }
    	echo(("echo \""+print.flush()+"\" "+out+" "+outFile).trim(),
    			currentDir,root);
    }

	/**
	 * Method that prints the whole path of the current working directory
	 * @param currentDirectory representation of the current working directory
	 */
	public void pwd(String[] inputParts, Directory currentDir, Directory root){
		Print print = new Print();
		print.buff(currentDir.getWholePath());
		if (inputParts.length == 1){
        	System.out.println(print.flush());
        }else{
        	echo("echo \""+print.flush()+"\" "+inputParts[1]+" "+inputParts[2],
    			currentDir,root);
        }
	}
    
	/**
     * Method that moves directory or file to the specified location.
     * Move item OLDPATH to NEWPATH. 
     * Both OLDPATH and NEWPATH may be relative to
     * the current directory or may be full paths. 
     * If NEWPATH is a directory,move the item into the directory. 
     * 		need to be in current dir?
     *
     * @param inputParts Array representation of the user input 
     */
    
	public void mv(String[] inputParts,Directory current, Directory root) {
		//inputParts1] -OLDPATH, [2] = NEWPATH //CHANGED INDEXES
        Item origin = getFileItem(current, root, inputParts[1].split("/"),-1); 
        Item target = getFileItem(current, root, inputParts[2].split("/"),-1);
        String originPath = origin.getWholePath();        
        String targetPath = target.getWholePath();
        if (target instanceof Directory){
        	if (origin == target){
            	System.out.println("Move failed, they are the same directory");
        	}else if(target.getSubItems().contains(origin)){
            	System.out.println(inputParts[1]+  " is already in " + 
        	target.getWholePath());
        	}else if(targetPath.substring(0, 
        			originPath.length()).equals(originPath)){
        		System.out.println("Move failed, can not move a directory "
        				+ "into its sub-directory");
        	}
        	else {
        		//get parent dir 
        		// remove origin from parent's subdir
		        origin.getParentDir().removeItem(origin);
		        target.addItem(origin); //add origin to target's sub;
		        }
        }else {
        	System.out.println("Target path is not a directory");
        }
    }

    /**
     * Method that copies a folder or file into the specified location.
     * Copy item OLDPATH to NEWPATH. 
     * Both OLDPATH and NEWPATH may be relative to the current directory or 
     * 		may be full paths. 
     * If NEWPATH is a directory,move the item into the directory.
     *If OLDPATH a directory, recursively copy all the content
     *		(files and directory) of OLDPATH
     * @param inputParts Array representation of the user input
     */
    public void cp(String[] inputParts,Directory current,Directory root) {
    	Item origin = getFileItem(current, root, inputParts[1].split("/"),-1);
        Item target = getFileItem(current, root,inputParts[2].split("/"),-1);
        
        String originPath = origin.getWholePath();        
        String targetPath = target.getWholePath();
        if (targetPath.substring(0, originPath.length()).equals(originPath)){
        	System.out.println
        	("Copy failed, can not copy a directory into its sub-directory");
        }
        else if (target instanceof Directory){
        	Item copy = origin.clone();
        	if (copy == target){
            	System.out.println("Copy failed, they are the same directory");
        	}else if(target.getSubItems().contains(copy)){
            	System.out.println(inputParts[0]+  " is already in " + 
        	inputParts[1]);
        	}else {
        //copy.getParentDir().removeItem(copy);
        target.addItem(copy);
        }
        }else {
        	System.out.println("Target path is not a directory");
        }
    }

    /**
     * Method that displays the content of a file.
     * Display the contents of file in the shell. 
     * If the file does not exist then displays error message instead.
     *
     * @param input String representation of the user input (name of file)
     *
     */
    public void cat(String[] inputParts,Directory currentDir, Directory root) {
    	Item item = currentDir.getSubItem(inputParts[1]);
    	Print print = new Print();
		if (item instanceof File){
        	File file = (File) item;
        	print.buff(file.getFileContents());
        	if (inputParts.length == 2){
        		System.out.println(print.flush());
        	}else{
        		echo("echo \""+print.flush()+"\" "+inputParts[2]+" "+
        	inputParts[3],
        			currentDir,root);
        	}
        }else{
        	System.out.println("No such file");
        }
    }

	/**
	 * If  OUTFILE  is  not  provided,  print  STRING  on  the  shell.  
	 * Otherwise,  put  STRING  into  file  OUTFILE.  
	 * STRING  is  a  string  of  characters  surrounded  by  double quotation
	 *  marks. 
	 * If echoSTRING  [>  OUTFILE], this   creates   a   new   file   if   
	 * 		OUTFILE   does   not   exists   and rases  the  old  contents  if  
	 * 		OUTFILE  already  exists.  
	 * In  either  case,  the  only thing  in  OUTFILE  should  be  STRING
	 * If echoSTRING  [>>  OUTFILE] appends  instead  of  overwrites. 
	 * @param input
	 */    
    public void echo(String input, Directory currentDir, Directory root){
    	String[] inputParts = input.split("\"");
    	String s ="";
    	if (inputParts.length > 1){s = inputParts[1];}
    	if (inputParts.length < 3){System.out.println(s);}
    	else{
        	String[] out = inputParts[2].split(" ");
    		String[] inputs = out[2].split("/");
    		if (getFileItem(currentDir,root,inputs,-1) instanceof Directory){
    			System.out.println("Directory already exists.");
    		}else if (!(getFileItem(currentDir,root,inputs,-1) 
    				instanceof File)){
    			Directory target;
    			if (inputs.length == 1){target = currentDir;}
    			else{
    				target = (Directory) getFileItem(currentDir,root,inputs,-2);
    			}
    			File newFile = new File(inputs[inputs.length-1], target);							
				newFile.addContent(s);
    		}else{
    			File f = (File) getFileItem(currentDir,root,inputs,-1);
    			if (out[1].equals(">")){
	    			f.eraseContent();
	    			f.addContent(s);
    			}else{f.addContent(NL);f.addContent(s);
    			}
    		}
    	}
    }
    
    public void rm(String[] input, Directory currentDir,Directory root){
    	int j;
    	boolean confirm;
    	Directory current=currentDir;
    	if (input[1].equals("-f")){j = 2; confirm = false;
    	}else{j = 1; confirm = true;
    	}for (int i=j; i<input.length; i++){
    		String[] inputs = input[i].split("/");
    		Item target = getFileItem(current,root,inputs,-1);
    		if (currentDir == target){currentDir = currentDir.parentDir;}
    		if (target instanceof File){
    			if (confirm){
    				System.out.println("File "+target.getName()+" is removed");
    			}current.removeItem(target);
    		}else if(target instanceof Directory && target!=root){
    			if(target==currentDir){currentDir = currentDir.parentDir;}
    			ArrayList<Item> subItems = target.getSubItems();
    			for(int k=0;k<subItems.size();){
    				String[] recInput;
    				if (confirm){recInput = 
    						new String[] {"rm",subItems.get(0).getName()};
    				}else{recInput = 
    						new String[] {"rm","-f",subItems.get(0).getName()};}
    				rm(recInput, (Directory)target, root);
    			}if (confirm){
    				System.out.println(
    						"Directory "+target.getName()+" is removed");
    			}current.removeItem(target);
    		}else{
    			System.out.println("Remove failed");
    		}
    	}
    }
    
    public void man(String[] inputParts,Directory currentDir, Directory root){
    	String fileName = inputParts[1];
		FileInputStream fStream = null;
		Print print = new Print();
		try {
			fStream = new FileInputStream ("./commandDoc/"+fileName);
	        BufferedReader br = new BufferedReader(new 
	        		InputStreamReader(fStream));
	        String line = null;
	        while ((line = br.readLine())!=null){
	        	print.buff(line);
	        }
	        if (inputParts.length == 2){
	        	System.out.println(print.flush());
	        }else{
        	echo("echo \""+print.flush()+"\" "+inputParts[2]+" "+inputParts[3],
        			currentDir,root);
	        }
		}catch(FileNotFoundException e){
            System.out.println("No such command");
        	//System.out.print(e.getMessage());
        }catch(IOException e){
            System.out.print(e.getMessage());
        }
    }
    /**
     * Method takes a regex and looks for the pattern in the PATH
     * If PATH is a file, looks for line with regex
     * If PATH is a directory, and [-R] is supplied,recursively looks for line 
     * 		in all files with regex
     * If not, produce an error.
     * @param inputParts
     * line a representation of the line in the file containing the regex
     * if PATH is a directory , line = PATH: line
     */

    // grep [-R] REGEX PATH...
    public void grep(String [] inputParts,Directory currentDir, Directory root){
        //if -R is supplied AND PATH is a directory, 
    	// recursively go through each file and find all line
    	if (inputParts[1].equals("-R")){
    		Pattern p = Pattern.compile(inputParts[2]);
    		String [] recPathNames = Arrays.copyOfRange(inputParts, 3, 
    				(inputParts.length));
    		for (int i=0; i<recPathNames.length;i++){
        		String[] path = recPathNames[i].split("/");
        		Item target = getFileItem(currentDir, root, path, -1);
    		//if fileItem type == dir
        		if (target instanceof Directory){recursivelySearch(target,p);}
        		else{System.out.println("Cannot recursively search a file");}
    		}
    	}else { // else look for inputParts[1] in File
    		//grep REGEX PATH...
    		Pattern newP = Pattern.compile(inputParts[1]);
    		String [] pathNames = Arrays.copyOfRange(inputParts, 2, 
    				(inputParts.length)); //make new array of paths only
    		for (int i=0; i<pathNames.length;i++){
        		String[] path = pathNames[i].split("/");
        		Item target = getFileItem(currentDir, root, path, -1);
        		System.out.println(target.getName()); //works for local
	    		if (target instanceof File){ // if targetItem is file go ahead
	    		File targetFile = (File) target;//downcasting works
	    		//look for pattern p in file 
	    		//WORKS WHEN PATTERN GIVEN W/O STR QUOTES
	    		System.out.println(targetFile.searchFile(newP)); 
	    	    }else{ //else if its a directory
	    	    	System.out.println("Cannot search a directory"); 
	    	    }
    		}
        }
    }
    
    /**
     *******************
     * HELPER FUNCTIONS * 
     ******************
     */
    
    /**
     * Method that executes the appropriate command
     * <p>
     * This method executes the user command accordingly.
     *
     * @param inputParts Array representation of the user input (contains
     * command and it's parameter), currentDirectory is the directory we are in
     */
    public void commandParser(String[] inputParts, Directory currentDirectory,
    		Directory rootDirectory) { 
    	// Needs to take directory so functions know current directory
    	if (inputParts[0].equals("exit")) {
            exit();
        }else if (inputParts[0].equals("mkdir")) {
            mkdir(inputParts, currentDirectory, rootDirectory);
        }else if (inputParts[0].equals("cd")) {
            currentDirectory= (Directory)cd(inputParts[1],currentDirectory, 
            		rootDirectory);
        }else if (inputParts[0].equals("ls")) {
            ls(inputParts, currentDirectory, rootDirectory);
        }else if (inputParts[0].equals("pwd")) { 
            pwd(inputParts,currentDirectory,rootDirectory);
        }else if (inputParts[0].equals("mv")) {
        	mv(inputParts, currentDirectory, rootDirectory);
        }else if (inputParts[0].equals("cp")) {
        	cp(inputParts, currentDirectory, rootDirectory);
        }else if (inputParts[0].equals("cat")) {
            cat(inputParts,currentDirectory,rootDirectory);
        }else if (inputParts[0].equals("man")) {
        	man(inputParts,currentDirectory,rootDirectory);
        }else if (inputParts[0].equals("rm")) {
            rm(inputParts, currentDirectory, rootDirectory);
        }
    }
    
    /**
     * Helper method for other functions 
     * Takes in a string input which could be a path for a dir or file and 
     * 		turns the Item obj with that name
     * @param input is the local,relative,or absolute path of a directory
     * @param type is the type of item the string represents
     * @return Item input

     */
    //looking for Item with name String input
	public Item getFileItem(Directory currentDir, Directory root, 
			String[] names, int index){
		Item activeItem = currentDir; //starts at current
		if (index <= -1){
			index = names.length + index + 1;
		}if (names.length == 0){
			activeItem = root;
		}
		for (int i = 0; i < index; i++) {
			if (names[i].equals("")) {
				activeItem = root;
				continue;
			}if (activeItem instanceof File){
				return null;
			}else {
				if (names[i].equals("..")) {
					if (activeItem == root){
						return null;
					}else{
				// go to the parent directory
						activeItem = activeItem.parentDir;
					}
				} else if (names[i].equals(".")) { // do nothing..
					activeItem = currentDir;
				} else {
					//check if the subDir exist
					//if activeItem is a File, File has no 
					//if activeItem is a directory can check subDir for name
					Item sub = activeItem.getSubItem(names[i]);
					if (sub != null) {
						// if exist, move the current directory 
						// to it's sub directory
						activeItem = sub;			
					} else { // if not, print error message
						return null;
					}
				}
			}
		} 
	  return activeItem;
	}
    
    /**
	 * helper function which take a directory as parameter and
	 * recursively display the contents of it's subdirectory(s)
	 * @param current Directory
	 */
	public void recursivelyGetSubItems(Directory currentDir, Print print){
		for (int i = 0; i < currentDir.getSubItems().size(); i++){
			//check if the subItem is a Directory
			if (currentDir.getSubItems().get(i) instanceof Directory){
				Directory tempDir = (Directory)currentDir.getSubItems().get(i);
				// i.e /b and /a/b share the same name
				//System.out.print(tempDir.getName() + ": ");
				print.buff(tempDir.getName() + ": ");
				// print it's subItem's name
				for (int j = 0; j < tempDir.getSubItems().size(); j++){
					print.buff(tempDir.getSubItems().get(j).getName() + " ");
				}
				print.buff("");
				//recursive call on it's sub itemss
				recursivelyGetSubItems(
						(Directory)currentDir.getSubItems().get(i), print);
			}else{ //If it's a file
				print.buff(currentDir.getSubItems().get(i).getName());
			}
		}
	}
	
	/**
	 * A helper function of ls which based on the users input to 
	 * 	print the contents of a directory
	 * @param length, the length of input
	 * @param input, input path
	 * @param currentDir , current Directory
	 * @param root, root Directory
	 * @param recursion, a boolean, if is true then recursively get sub items
	 * @param print a print object to store print contents
	 */
	public void checkR(int length, String[] input, Directory currentDir, 
			Directory root, boolean recursion, Print print){
		if (recursion){
	        for (int i = 2; i <length; i++){
	            String[] path = input[i].split("/");
	            Item target = getFileItem(currentDir, root, path, -1);
	            if (target == null){System.out.println("Directory not exists");}
	            else if (target instanceof Directory){
	            	//for every directory in the target directory
	            	//recursively get its sub directories
	                recursivelyGetSubItems((Directory)target, print);
	            }else{print.buff(target.getName());}
	        }
		}else{
	        for (int i = 1; i <length; i++){
	            String[] path = input[i].split("/");
	            Item target = getFileItem(currentDir, root, path, -1);
	            if (target == null){
	                System.out.println("Directory not exists");
	            }
	            else if (target instanceof Directory){
	                print.buff(target.getName()+": ");
	                for (Item item : target.getSubItems()){
	                    print.buff(item.getName() + " ");
	                }
	                print.buff("");
	            }else{print.buff(target.getName());}
	        }
		}
	}

	/*
	 * Get method copies the items on a URL and copies it to the code
	 */
	public void get(String urlAddress) throws Exception{
        URL oracle = new URL(urlAddress);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));

        String inputLine;
        String total = "";
        while ((inputLine = in.readLine()) != null)
        	total += inputLine + "\n";
        in.close(); 
        
        //split into parts the URL address
    	ArrayList<String> sNew = new ArrayList<String>();
	    for (String retval: urlAddress.split("/")){
	    	sNew.add(retval); 
		}
	    //get the last part of the URL string
	    String nameFolder = sNew.get(sNew.size()-1);
	    //Make a new file with the name of the last part of the 
	    //URL string and add content
	    //takes in the current directory
	    File newFile = new File(nameFolder, null);
	    newFile.addContent(total);
	    //System.out.println(newFile.getFileContents());
	}

	/**
	 * helper function which take a directory as parameter and 
	 * recursively looks for a pattern in the contents of its subfiles. 
	 * It prints the line containing the the pattern in the following format. 
	 * 		line = Path: line
	 * 
	 * @param currentDir, this is the current directory we ar searching in
	 * @param regex, this is the pattern we are searching for
	 */
	public void recursivelySearch(Item currentDir, Pattern regex){
		// go thru the dir's subitems 
		//looks in each file in dir
		for (int i = 0; i < currentDir.getSubItems().size(); i++){ 
			//check if the subItem is a Directory
			if (currentDir.getSubItems().get(i) instanceof Directory){
				// get that subdir
				Item tempDir = currentDir.getSubItems().get(i);
				//recursively search that subdir's subdir
				recursivelySearch(tempDir,regex);
			}else{ //If it's a file
				String line = currentDir.getSubItems().get(i).getWholePath() 
						+ ": ";
				//downcasting works
				File currentDirFile = (File) currentDir.getSubItems().get(i);
				//look for pattern p in file
	    		System.out.println(line + currentDirFile.searchFile(regex)); 
			}
		}
	}
}
