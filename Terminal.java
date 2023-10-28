import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.NoSuchFileException;
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
	Path currentDirectory;

	public Terminal() {
		parser = new Parser();
		currentDirectory = Path.of(System.getProperty("user.dir"));
	}

	/**
	 * 0 - standard output
	 * 1 - file (write)
	 * 2 - file (append)
	 */
	public static int write_mode = 0;
	/**
	 * 0 - success
	 */
	public static int last_status = 0;

	/**
	 * This method will choose the suitable command method to be called.
	 */
	public static void chooseCommandAction() throws IOException{
		String s = "";
		s = parser.getCommandName();
		String[] args = parser.getArgs();
		if (s.equals("echo")) {
			// last_status = echo(args);
		} else if (s.equals("pwd")) {
			// last_status = pwd(args);
		} else if (s.equals("cd")) {
			// last_status = cd(args);
		} else if (s.equals("ls")) {
			// last_status = ls(args);
		} else if (s.equals("mkdir")) {
			// last_status = mkdir(args);
		} else if (s.equals("rmdir")) {
			// last_status = rmdir(args);
		} else if (s.equals("touch")) {
			last_status = touch(args);
		} else if (s.equals("cp")) {
			last_status = cp(args);
		} else if (s.equals("rm")) {
			// last_status = rm(args);
		} else if (s.equals("cat")) {
			// last_status = cat(args);
		} else if (s.equals("wc")) {
			// last_status = wc(args);
		} else if (s.equals("history")) {
			// last_status = history(args);
		} else {
			// last_status = 1; // command not found
		}
	}

	public static void check_mode() {
		for (String s : parser.getArgs()) {
			if (s.equals(">")) {
				write_mode = 1;
				break;
			}
			if (s.equals(">>")) {
				write_mode = 2;
				break;
			}
		}
		write_mode = 0;
	}

	/**
	 * Creates new and empty file
	 * 
	 * @param args files names
	 * @author Adham Allam
	 * @return int indicates the exit status (
	 *         0 - success, error otherwise).
	 * @throws IOException if an error occurs while creating the file.
	 */
	public static int touch(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: touch file1 file2 ...");
			return (98);
		}

		for (String fileName : args) {
			try {
				File file = new File(fileName);

				if (file.createNewFile()) {
					System.out.println("File created: " + file.getName());
				} else {
					System.out.println("File already exists: " + file.getName());
				}
			} catch (IOException e) {
				System.err.println("Error creating file: " + e.getMessage());
				return (99);
			}
		}
		return (0);
	}

	/**
	 * Takes 2 arguments, both are files and copies the first onto the second.
	 * 
	 * @author Adham Allam
	 * @param args argument list (files names)
	 * @return int indicates the exit status (
	 *         0 - success, error otherwise).
	 * @throws IOException
	 */
	public static int cp(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("Usage: touch file1 file2");
			return (98);
		}

		Path source = Paths.get(args[0]);
		Path target = Paths.get(args[1]);
		try {
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		} catch(NoSuchFileException e) {
			System.err.println("Error source file: " + e.getMessage());
		}
		return (0);
	}

	/**
	 * echo command Takes an argument and prints it
	 */
	public void echo() {
		for (String element : parser.getArgs()) {
			System.out.println(element);
		}
	}

	/**
	 * mkdir command
	 * Takes 1 or more arguments and creates a directory for each argument
	 * 
	 * @throws IOException if an error occurs while starting navigating through dirs
	 */
	public void mkdir() throws IOException {
		for (String element : parser.getArgs()) {
			Path newdir = currentDirectory.resolve(element);
			Files.createDirectory(newdir);
		}
	}

	/**
	 * rmdir command
	 * takes 1 argument which is “*” (e.g. rmdir *) and removes all the empty
	 * directories in
	 * the current directory.
	 * takes 1 argument which is either the full path or the
	 * relative (short) path and removes the given directory only if
	 * it is empty.
	 * 
	 * @throws IOException if an error occurs while starting navigating through dirs
	 */
	public void rmdir() throws IOException {
		if (parser.getArgs()[0].equals("*")) {
			removeEmptyDirectories(currentDirectory.toFile());
		} else {
			for (String element : parser.getArgs()) {
				Path dirToRemove = currentDirectory.resolve(element);
				removeEmptyDirectories(dirToRemove.toFile());
			}
		}
	}

	/**
	 * recursive function to navigate through the dir.
	 * 
	 * @param dir File object
	 * @throws IOException if an error occurs while starting navigating through dirs
	 */
	private void removeEmptyDirectories(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					removeEmptyDirectories(file);
				}
			}
		}
		if (dir.listFiles() == null || dir.listFiles().length == 0) {
			Files.delete(dir.toPath());
		}
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
			while (!cmd.equals("exit")) {
				System.out.print("> ");
				cmd = input.nextLine();
				if (cmd.equals("exit")) {
					System.out.println("Bye :)");
					break;
				}
				parser.parse(cmd);
				check_mode();
				chooseCommandAction();
				if (last_status != 0) {
					System.out.println("Error Occured");
					break;
				}
			}
		}
	}
}
