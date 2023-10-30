/**
 * The Parser class handles commands entered by the user that is parse it
 * into command name and argument list.
 */
class Parser {
    private String commandName;
    private String[] args;
    private String cmd_line;

    /**
     * Parses the user's input into cmd_name and arg_list.
     *
     * @param input line to be parsed.
     * @return True if success, False otherwise.
     */
    public boolean parse(String input) {
        cmd_line = input;
        if (input.contains("\"")) {
            String[] arr = input.split("\"");
            args = new String[1];
            args[0] = arr[1];
            commandName = arr[0].trim();

        } else {
            String[] arr = input.split(" ");
            args = new String[arr.length - 1];
            for (int i = 1; i < arr.length; i++)
                if (!arr[i].isEmpty())
                    args[i - 1] = arr[i];
            commandName = arr[0];
        }
        return true;
    }


    /**
     * Returns the command name
     *
     * @return command name.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Returns the arguments list.
     *
     * @return arg list.
     */
    public String[] getArgs() {
        return args;
    }
}
