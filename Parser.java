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
    public boolean parse(String input)
    {
        String[] arr  = input.split(" ");
        args = new String[arr.length-1];
       for(int i = 1 ; i < arr.length ; i++)
          args[i-1] = arr[i];
       commandName = arr[0];
        return false;
    }



    /**
     * 
     * Returns the command name
     *
     * @return command name.
     */
    public String getCommandName(){
        return commandName;
    }

    /**
     * Returns the arguments list.
     *
     * @return arg list.
     */
    public String[] getArgs(){
        return args;
    }
}
