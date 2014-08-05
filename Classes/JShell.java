package a2;
import java.io.*;
import java.util.ArrayList;

public class JShell {
	/**The main class
	 * Calls the commands in the command class
	 * Gets the users inputs and checks if valid
	 */
	
/**Initialize BufferedReader to read user input*/
    public static BufferedReader br =
            new BufferedReader(new InputStreamReader(System.in));
    
    /** Array list that initializes the valid regex command list that would be used later.*/
    public ArrayList<String> commandList = new ArrayList<String>();

    /**
     * JShell constructor, mainly used to populate commandList with regular
     * expression.
     * Creates a new JShell with the appropriate commands.
     */
    public JShell() {
        // Populate the command list with appropriate regex.  
    	this.commandList.add("()");
        this.commandList.add("^mkdir(( )+[^( )]+)+$");
        this.commandList.add("^cd( )+[^( )]+$");
        this.commandList.add("^ls(( )+[^( )]+)*$");
        this.commandList.add("^pwd$");
        this.commandList.add("^mv( )+[^( )]+( )+[^( )]+$");
        this.commandList.add("^cp( )+[^( )]+( )+[^( )]+$");
        this.commandList.add("^cat( )+[^( )]+$");
        this.commandList.add("^echo(|( )+(\"|\')[^\'\"]*(\"|\')(|( )*(>|>>)( )*"
                + "[^>( )]+))$");
        this.commandList.add("^man( )+[^( )]+$");
        this.commandList.add("^exit$");
        this.commandList.add("^grep(( )+[^( )]+)+$");
    }

    /**
     * JShell main method.
     * <p>
     * Prompts user for command input and process accordingly. Displays current
     * path.
     */
    public static void main(String[] args) throws IOException {
        // Initialize the JShell.
        JShell shell = new JShell();

        // Make BashCommands objects to be used as reference.
        Command commands = new Command();
        
        //// Initialize the root.
        Directory root = new Directory("/",null);
        Directory currentDirectory = root;

        // Setup variables for user input.

        String prompt = currentDirectory.getWholePath() + " /# ";
        Boolean loop = true;

        // Loops infinitely, asks user for prompt input(s).
        while (loop) {

            // The string line is then broken down to string arrays.
            String input;
            String[] inputParts;
 
            // Continuously prompt user.

            System.out.print(prompt);
            // Scan for line typed by user and store in line.
            input = br.readLine().trim();
            inputParts = input.split("( )++");

            // Check if the user inputs the valid command.
            if (shell.checkCommand(input)) {

                    // Execute the command accordingly.
            		if (inputParts[0].equals("")){
            			prompt = currentDirectory.getWholePath() + " /# ";
            		}else if (inputParts[0].equals("cd")){ //call this separately so can save the current directory
                    	currentDirectory= (Directory) commands.cd(inputParts[1], currentDirectory, root);
                    	prompt = currentDirectory.getWholePath() + " /# ";
                    }else if (inputParts[0].equals("echo")){ 
                    	commands.echo(input,currentDirectory,root);
                    }else if (inputParts[0].equals("grep")){
                    	commands.grep(inputParts,currentDirectory,root);
                    
                    }else{
                    	commands.commandParser(inputParts,currentDirectory,root);
                    }
                }

             // If the user inputs invalid command.
            else {
                System.out.println(inputParts[0] + ": Invalid command "
                        + "or parameter.");
            }
        }
    }

    /**
     * Returns a boolean value to check if the command is valid.
     * <p>
     * Checks the user's shell command if it is valid or not. If the user inputs
     * an invalid command or an invalid syntax, returns false, otherwise, true.
     *
     * @param command the user's command in one string.
     * @return boolean value depending on user input.
     */
    public boolean checkCommand(String command) {
        // Initialize varibles for check command.
        boolean isMatch = false;
        int i = 0;

        // Check whether the user's command fits with any command 
        // in the command list.
        while ((isMatch == false) && (i < commandList.size())) {
            isMatch = command.matches(commandList.get(i));
            i++;
        }
        return isMatch;
    }
}
