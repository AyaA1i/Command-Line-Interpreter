import java.util.Scanner;
import java.io.*;
import java.nio.file.*;
import java.util.Vector;
import java.util.Arrays;
import java.util.Collections;

/**
 * Terminal - Executes some basic commands.
 *
 * @author 1.Adham Allam
 * @author 2.Abdullah Farg
 * @author 3.Aya Ali
 * @author 4.Rana Emara
 * @version 1.0
 * @since 27 Oct 2023
 **/
public class Terminal {
    public static Parser parser = new Parser();
    static Path currentDirectory = Path.of(System.getProperty("user.dir"));
    public static Vector<String> CommandHistory = new Vector<>();
    public static BufferedWriter buf = null;
    public static File redirectFile = null;
    /**
     * 0 - success
     */
    public static int last_status = 0;

    /**
     * This method will choose the suitable command method to be called.
     */
    public static void chooseCommandAction() throws IOException {
        String s = "";
        s = parser.getCommandName();
        String[] args = parser.getArgs();
        if (s.equals("echo")) {
            last_status = echo(args);
        } else if (s.equals("pwd")) {
            last_status = pwd();
        } else if (s.equals("cd")) {
            last_status = cd(args);
        } else if (s.equals("ls")) {
            last_status = ls(args);
        } else if (s.equals("mkdir")) {
            last_status = mkdir(args);
        } else if (s.equals("rmdir")) {
            last_status = rmdir(args);
        } else if (s.equals("touch")) {
            last_status = touch(args);
        } else if (s.equals("cp")) {
            last_status = cp(args);
        } else if (s.equals("rm")) {
            last_status = rm(args);
        } else if (s.equals("cat")) {
            last_status = cat(args);
        } else if (s.equals("wc")) {
            last_status = wc(args);
        } else if (s.equals("history")) {
            last_status = history();
        } else {
            if (!s.equals("")) {// check if a string is not equal to the "Enter" key
                System.err.println(s + " is not recognized as a command");
                last_status = 1;
            } // command not found
        }

    }

    static public int ls(String[] args) {
        Path directory = Paths.get(currentDirectory.toUri());
        // Get a list of files and directories in the current directory
        File[] contents = directory.toFile().listFiles();
        if (contents == null) {
            System.out.println("Not found files in this directory ");
            return 0;
        } else if (args.length == 0)   // Sort the contents alphabetically
            Arrays.sort(contents);
        else  // Sort the contents alphabetically in reverse order
            Arrays.sort(contents, Collections.reverseOrder());

        // Print the sorted contents
        for (File item : contents) {
            System.out.println(item.getName());
        }
        return 0;
    }

    /**
     * Creates new and empty file
     *
     * @param args files names
     * @return int indicates the exit status (
     * 0 - success, error otherwise).
     * @throws IOException if an error occurs while creating the file.
     * @author Adham Allam
     */
    public static int touch(String[] args) {
        if (args.length == 0) {
            _print("Usage: touch file1 file2 ...");
            return (98);
        }

        System.out.println(currentDirectory.toString());
        for (String fileName : args) {
            try {
                File file = new File(currentDirectory.toFile(), fileName);

                if (file.createNewFile()) {
                    _print("File created: " + file.getName());
                } else {
                    _print("File already exists: " + file.getName());
                }
            } catch (IOException e) {
                _print("Error creating file: " + e.getMessage());
                return (99);
            }
        }
        return (0);
    }

    /**
     * Takes 2 arguments, both are files and copies the first onto the second.
     * if -r option specified: Takes 2 arguments, both are directories (empty or
     * not) and copies the first directory (with all its content) into the second
     * one.
     *
     * @param args argument list (files names)
     * @return int indicates the exit status (
     * 0 - success, error otherwise).
     * @throws IOException
     * @author Adham Allam
     */
    public static int cp(String[] args) throws IOException {
        if (args.length < 2) {
            _print("Usage: cp file1 file2");
            return (98);
        }

        Path source, target;
        if (args.length == 3 && args[0].equals("-r")) {
            try {
                source = currentDirectory.resolve(args[1]);
                target = currentDirectory.resolve(args[2]);
                copyDirectory(source, target);
            } catch (IOException e) {
                _print("Error copying directory: " + e.getMessage());
            }
            return (0);
        }

        source = currentDirectory.resolve(args[0]);
        target = currentDirectory.resolve(args[1]);
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (NoSuchFileException e) {
            _print("Error source file: " + e.getMessage());
        }
        return (0);
    }

    /**
     * Takes 2 arguments, both are directories (empty or not) and copies the first
     * directory (with all its content) into the second one.
     *
     * @param source source directory
     * @param target target directory
     * @throws IOException
     * @author Adham Allam
     */
    public static void copyDirectory(Path source, Path target) throws IOException {
        if (!Files.exists(target)) {
            Files.createDirectories(target);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            for (Path entry : stream) {
                Path targetEntry = target.resolve(entry.getFileName());

                if (Files.isDirectory(entry)) {
                    copyDirectory(entry, targetEntry);
                } else {
                    Files.copy(entry, targetEntry, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    /**
     * echo command Takes an argument and prints it
     */
    public static int echo(String[] args) {
        for (String element : args) {
            _print(element + " ", false);
        }
        _print("");
        return (0);
    }

    /**
     * mkdir command
     * Takes 1 or more arguments and creates a directory for each argument
     */
    public static int mkdir(String[] args) {
        for (String element : args) {
            try {
                Path newdir = currentDirectory.resolve(element);
                try {
                    if(Files.exists(newdir)){
                        System.out.println("dir already exists: " + newdir);
                        return (99);
                    }
                    Files.createDirectories(newdir);
                    System.out.println("dir created: " + newdir);
                } catch (IOException e) {
                    System.err.println("Error creating directory: " + e.getMessage());
                    return (99);
                }
            } catch (InvalidPathException e) {
                System.err.println("Invalid Path" + e.getMessage());
                return (99);
            }
        }
        return (0);
    }

    /**
     * rmdir command
     * takes 1 argument which is “*” (e.g. rmdir *) and removes all the empty
     * directories in
     * the current directory.
     * takes 1 argument which is either the full path or the
     * relative (short) path and removes the given directory only if
     * it is empty.
     */
    public static int rmdir(String[] args) {
        if (args[0].equals("*")) {
            removeEmptyDirectories(currentDirectory.toFile());
        } else {
            for (String element : args) {
                try {
                    Path dirToRemove = currentDirectory.resolve(element);
                    removeEmptyDirectories(dirToRemove.toFile());
                } catch (InvalidPathException e) {
                    System.err.println("Invalid Path" + e.getMessage());
                    return (99);
                }
            }
        }
        return (0);
    }

    /**
     * recursive function to navigate through the dir.
     *
     * @param dir File object
     * @throws IOException if an error occurs while starting navigating through dirs
     */
    private static int removeEmptyDirectories(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    removeEmptyDirectories(file);
                }
            }
        }
        if (dir.listFiles() == null || dir.listFiles().length == 0) {
            try {
                Files.delete(dir.toPath());
            } catch (IOException e) {
                System.err.println("Error deleting directory: " + e.getMessage());
                return (99);
            }

        }
        return 0;
    }

    /**
     * Prints the current directory to the console.
     *
     * @return Returns 0 upon successful execution.
     */
    public static int pwd() {
        System.out.println(currentDirectory);
        return 0;
    }

    /**
     * Prints the command history along with enumeration.
     *
     * @return Returns 0 upon successful execution.
     */
    public static int history() {
        int cut = 1;
        for (String i : CommandHistory)
            System.out.println(cut++ + " " + i);
        return 0;
    }

    /**
     * Attempts to delete a file specified in the args array.
     *
     * @param args An array containing the file name to be deleted.
     * @return Returns 0 upon successful execution.
     */
    public static int rm(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java DeleteFile <file_name>");
            return 0;
        }
        String fileName = args[0];
        File file = new File(fileName);

        if (file.exists()) {
            if (file.delete())
                System.out.println(fileName + " has been deleted.");
            else
                System.out.println("Failed to delete " + fileName);
        } else
            System.out.println(fileName + " does not exist in the current directory.");
        return 0;
    }

    /**
     * Displays the content of one or more files or concatenates the content of two
     * files.
     *
     * @param args An array of one or two file names to display or concatenate.
     * @return Returns 0 upon successful execution, error otherwise.
     * @throws IOException
     */
    public static int cat(String[] args) throws IOException {
        if (args.length > 2 || args.length < 1) {
            System.out.println("Usage: cp file1 file2");
            return (98);
        }
        for (String fileName : args) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                return 1;
            }
        }
        return 0;
    }

    /**
     * Displays the line count, word count, and character count of a given file.
     *
     * @param args An array containing a single file name.
     * @return Returns 0 on success, or a non-zero value to indicate an error.
     * @throws IOException if an error occurs while reading the file or if the
     *                     number of arguments is incorrect.
     */
    public static int wc(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: wc <file>");
        }

        String fileName = args[0];
        int lineCount = 0;
        int wordCount = 0;
        int charCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCount++;
                charCount += line.length();
                String[] words = line.split("\\s+"); // Split by whitespace
                wordCount += words.length;
            }
        } catch (IOException e) {
            throw new IOException("Error reading file: " + e.getMessage());
        }

        System.out.println(String.format("%d %d %d %s", lineCount, wordCount, charCount, fileName));

        return 0;
    }

    //

    /**
     * Changes the current directory.
     *
     * @param args The arguments for the cd command.
     * @return 0 if the directory was successfully changed, 1 if an error occurred.
     */
    public static int cd(String[] args) {

        // Case 1: cd takes no arguments and changes the current path to the path of
        // your home directory.
        if (args.length == 0) {
            Path homeDir = Path.of(System.getProperty("user.home"));
            try {
                currentDirectory = currentDirectory.resolve(homeDir);
                System.out.println(currentDirectory);
                return 0;
            } catch (Exception e) {
                System.err.println("Error changing directory: " + e.getMessage());
                return 1;
            }
        }

        // Case 2: cd takes 1 argument which is ". ." and changes the current directory
        // to the previous directory.
        if (args.length == 1 && args[0].equals("..")) {
            try {
                Path parentDir = currentDirectory.getParent();
                if (parentDir != null) {
                    currentDirectory = currentDirectory.resolve(parentDir);
                    System.out.println(currentDirectory);
                    return 0;
                } else {
                    System.err.println("Already at the root directory.");

                    return 1;
                }
            } catch (Exception e) {
                System.err.println("Error changing directory: " + e.getMessage());
                return 1;
            }
        }

        // Case 3: cd takes 1 argument which is either the full path or the relative
        // (short) path and changes the current path to that path.
        if (args.length == 1) {
            Path newDir = currentDirectory.resolve(args[0]);
            if (Files.exists(newDir) && Files.isDirectory(newDir)) {
                try {
                    currentDirectory = newDir;
                    System.out.println(currentDirectory);
                    return 0;
                } catch (Exception e) {
                    System.err.println("Error changing directory: " + e.getMessage());
                    return 1;
                }
            } else {
                System.err.println("The specified directory does not exist or is not a directory.");
                return 1;
            }
        }

        // If none of the cases match, return an error.
        System.err.println("Invalid cd command. Usage: 'cd', 'cd ..', or 'cd <directory>'");
        return 1;
    }

    /**
     * Initializes the buffer to write into
     *
     * @return true if success, false otherwise.
     * @author Adham Allam
     */
    public static boolean initBuffer() {
        if (Parser.write_mode == 0)
            return false;

        redirectFile = new File(currentDirectory.toFile(), Parser.RFname);
        /* Write to file */
        if (Parser.write_mode == 1) {
            try {
                buf = Files.newBufferedWriter(redirectFile.toPath(), StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            } catch (IOException e) {
                System.err.println("Error Creating file: " + e.getMessage());
                return false;
            }
        }
        /* Append to file */
        if (Parser.write_mode == 2) {
            if (!redirectFile.exists()) {
                System.err.println("Error: File doesn't exist");
                return false;
            }
            try {
                buf = Files.newBufferedWriter(redirectFile.toPath(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.err.println("Error Creating file: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * print a statement based of the write_mode
     *
     * @param s       statememnt to be printed
     * @param newLine takes two values:
     *                true: pritn new line at the end
     *                false don't print new line at the end
     * @author Adham Allam
     */
    public static void _print(String s, boolean newLine) {
        /* Print to screen */
        if (Parser.write_mode == 0) {
            System.out.print(s);
            if (newLine)
                System.out.println();
            return;
        }
        if (buf != null) {
            try {
                buf.write(s);
                if (newLine)
                    buf.newLine();
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
                return;
            }
        }
    }

    public static void _print(String s) {
        _print(s, true);
    }

    /**
     * main - Entry point
     * Creates a new parser object and allow the user to enter
     * the input through the keyboard, untill he writes 'exit'.
     *
     * @param args an array of String arguments passed to the program
     * @throws Exception if an error occurs while starting the store
     */
    public static void main(String[] args) throws Exception {
        String cmd = "";
        try (Scanner input = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                cmd = input.nextLine();
                if (cmd.equals("exit")) {
                    System.out.println("Bye :)");
                    break;
                }

                if (!parser.parse(cmd)) {
                    System.err.println("Error parsing command line.");
                    break;
                }
                if (!initBuffer())
                    buf = null;
                CommandHistory.add(parser.getCommandName());
                chooseCommandAction();
                if (buf != null)
                    buf.close();
            }
        }
    }
}