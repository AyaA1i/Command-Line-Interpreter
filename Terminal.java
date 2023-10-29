import java.util.Scanner;
import java.io.*;
import java.nio.file.*;

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
	public static void chooseCommandAction() throws IOException {
		String s = "";
		s = parser.getCommandName();
		String[] args = parser.getArgs();
		if (s.equals("echo")) {
			last_status = echo(args);
		} else if (s.equals("pwd")) {
			// last_status = pwd(args);
		} else if (s.equals("cd")) {
			// last_status = cd(args);
		} else if (s.equals("ls")) {
			// last_status = ls(args);
		} else if (s.equals("mkdir")) {
			last_status = mkdir(args);
		} else if (s.equals("rmdir")) {
			last_status = rmdir(args);
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
	 * if -r option specified: Takes 2 arguments, both are directories (empty or
	 * not) and copies the first directory (with all its content) into the second
	 * one.
	 *
	 * @author Adham Allam
	 * @param args argument list (files names)
	 * @return int indicates the exit status (
	 *         0 - success, error otherwise).
	 * @throws IOException
	 */
	public static int cp(String[] args) throws IOException {
		if (args.length > 3 || args.length < 2) {
			System.err.println("Usage: cp file1 file2");
			return (98);
		}

		if (args.length == 3) {
			if (args[0].equals("-r")) {
				try {
					copyDirectory(Paths.get(args[1]), Paths.get(args[2]));
				} catch (IOException e) {
					System.err.println("Error copying directory: " + e.getMessage());
					return (99);
				}
				return (0);
			} else {
				System.err.println("Usage: cp -r dir1 dir2");
				return (100);
			}
		}

		Path source = Paths.get(args[0]);
		Path target = Paths.get(args[1]);
		try {
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		} catch (NoSuchFileException e) {
			System.err.println("Error source file: " + e.getMessage());
			return (101);
		}
		return (0);
	}

	/**
	 * Takes 2 arguments, both are directories (empty or not) and copies the first
	 * directory (with all its content) into the second one.
	 *
	 * @author Adham Allam
	 * @param source source directory
	 * @param target target directory
	 * @throws IOException
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
			System.out.println(element);
		}
		return (0);
	}

	/**
	 * mkdir command
	 * Takes 1 or more arguments and creates a directory for each argument
	 *
	 */
	public static int mkdir(String[] args) {
		for (String element : args) {
			try {
				Path newdir = currentDirectory.resolve(element);
				try {
					Files.createDirectories(newdir);
					System.out.println("dir created: " + newdir);
				} catch (FileAlreadyExistsException e) {
					System.out.println("dir already exists: " + newdir);
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
	 *
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
				if (!parser.parse(cmd)) {
					System.err.println("Error parsing command line.");
					break;
				}
				check_mode();
				chooseCommandAction();
			}
		}
	}
}
