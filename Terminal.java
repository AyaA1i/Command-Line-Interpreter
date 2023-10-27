import java.util.Scanner;

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
	Parser parser;

	/**
	 * main - Entry point.
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
			System.out.println(cmd);
		}
	}
}
