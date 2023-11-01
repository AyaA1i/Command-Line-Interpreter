import java.util.List;
import java.util.ArrayList;

/**
 * The Parser class handles commands entered by the user that is parse it
 * into command name and argument list.
 */
class Parser {
    private String commandName;
    private String[] args = null;
    public String cmd_line;
    public static String RFname;
    /**
     * 0 - standard output
     * 1 - file (write)
     * 2 - file (append)
     */
    public static int write_mode = 0;

    /**
     * Parses the user's input into cmd_name and arg_list.
     *
     * @param input line to be parsed.
     * @return True if success, False otherwise.
     */
    public boolean parse(String input) {
        write_mode = 0;
        cmd_line = input;
        input = input.trim();
        String[] arr1 = null, arr = null;

        // Check if there is redirections
        if (input.contains(">>")) {
            arr1 = _split(input, ">>");
            write_mode = 2;
        } else {
            arr1 = _split(input, ">");
            if (input.contains(">"))
                write_mode = 1;
        }

        if (arr1.length > 2) {
            System.err.println("Usage:command [> or >>] FileName");
            return false;
        }

        if (arr1.length > 0) {
            arr = _split(arr1[0], " ");
            args = new String[arr.length - 1];
            for (int i = 1; i < arr.length; i++)
                if (!arr[i].isEmpty())
                    args[i - 1] = arr[i];
            commandName = arr[0];
        }

        if (arr1.length > 1) {
            arr = _split(arr1[1], " ");
            for (int i = 0; i < arr.length; ++i) {
                if (!arr[i].isEmpty()) {
                    RFname = arr[i];
                    break;
                }
            }
        }

        return true;
    }

    /**
     * 
     */
    public static String[] _split(String s, String c) {
        String[] arr1 = s.split(c);
        List<String> filteredList = new ArrayList<>();

        for (String item : arr1) {
            if (!item.isEmpty()) {
                filteredList.add(item);
            }
        }
        String[] filteredArray = filteredList.toArray(new String[0]);
        return filteredArray;
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