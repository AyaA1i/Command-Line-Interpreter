import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

/**
 * Terminal - Executes some basic commands.
 *
 * @author 1.Adham Allam
 * @author 2.Abdullah Farg
 * @author 3.Aya Ali
 * @author 4.
 * @version 1.0
 * @since 27 Oct 2023
 **/
public class Terminal {

	public static  Parser parser = new Parser();
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
	public static void chooseCommandAction(){
		String s = "";
		s = parser.getCommandName();
		String[] args = parser.getArgs();
		if (s.equals("echo")){
			//last_status = echo(args);
		}
		else if (s.equals("pwd")){
			//last_status = pwd(args);
		}
		else if (s.equals("cd")){
			//last_status = cd(args);
		}
		else if (s.equals("ls")){
			//last_status = ls(args);
		}
		else if (s.equals("mkdir")){
			//last_status = mkdir(args);
		}
		else if (s.equals("rmdir")){
			//last_status = rmdir(args);
		}
		else if (s.equals("touch")){
			//last_status = touch(args);
		}
		else if (s.equals("cp")){
			//last_status = cp(args);
		}
		else if (s.equals("rm")){
			//last_status = rm(args);
		}
		else if (s.equals("cat")){
			//last_status = cat(args);
		}
		else if (s.equals("wc")){
			//last_status = wc(args);
		}
		else if (s.equals("history")){
			//last_status = history(args);
		}
		else{
			//last_status = 1; // command not found
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
	 * main - Entry point
	 * Creates a new parser object and allow the user to enter
	 * the input through the keyboard, untill he writes 'exit'.
	 * @param args an array of String arguments passed to the program
	 * @throws Exception if an error occurs while starting the store
	 */
	public static void main(String[] args) throws Exception{
		String cmd = "";
		Scanner input = new Scanner(System.in);

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
			check_mode();
			chooseCommandAction();
			if (last_status != 0) {
				System.out.println("Error Occured");
				break;
			}
		}
	}
}
