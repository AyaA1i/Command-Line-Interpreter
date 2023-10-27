import java.io.*;
import java.nio.file.*;
import java.util.*;

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
	public static  Parser parser = new Parser();
	Path currentDirectory;
	public Terminal(){
		parser = new Parser();
		currentDirectory = Path.of(System.getProperty("user.dir"));
	}

	/**
	 * echo command Takes an argument and prints it
	 */
	public void echo(){
		for (String element : parser.getArgs()) {
			System.out.println(element);
		}
	}
	/**
	 * mkdir command
	 * Takes 1 or more arguments and creates a directory for each argument
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
	 * takes 1 argument which is “*” (e.g. rmdir *) and removes all the empty directories in
	 * the current directory.
	 * takes 1 argument which is either the full path or the
	 * relative (short) path and removes the given directory only if
	 * it is empty.
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
	 * @param args an array of String arguments passed to the program
	 * @throws Exception if an error occurs while starting the store
	 */
	public static void main(String[] args) throws Exception{
		String cmd = "";
		Scanner input = new Scanner(System.in);
		Terminal t = new Terminal();
		while (!cmd.equals("exit"))
		{
			System.out.print("> ");
			cmd = input.nextLine();
			if (cmd.equals("exit"))
			{
				System.out.println("Bye :)");
				break;
			}
			parser.parse(cmd);
			System.out.println(parser.getCommandName());
			for(String i : parser.args)
				System.out.println(i);

		}
		t.rmdir();
	}
}
