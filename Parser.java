/**
 * The Parser class handles commands entered by the user that is parse it 
 * into command name and argument list.
 */
class Parser {
	String commandName;
	String[] args;

	/**
	 * Parses the user's input into cmd_name and arg_list.
	 * 
	 * @param input line to be parsed.
	 * @return True if success, False otherwise. 
	 */
	public boolean parse(String input){
		return false;
	}

	/**
	 * Returns the command name
	 * 
	 * @return command name.
	 */
	public String getCommandName(){
		return "";
	}

	/**
	 * Returns the arguments list.
	 *
	 * @return arg list.
	 */
	public String[] getArgs(){
		String[] s = {""};
		return s;
	}
}
