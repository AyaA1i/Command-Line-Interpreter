# Command-Line-Interpreter
Command Line Interpreter (CLI) for the operating system.
CLI allows the user to enter the input through the keyboard. After the
user writes the command and presses enter, the string is parsed, and the indicated command executed.
The CLI will keep accepting different commands from the user until the user writes `exit`, then the CLI terminates.

## Requirements
- [Java](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-20-04)

## Quickstart
1. Clone this repo
```sh
git clone https://github.com/AyaA1i/Command-Line-Interpreter.git
```

2. Compile
```sh
javac Parser.java Terminal.java
```

3. Run
```sh
java Terminal
```

4. Enter a command or `exit` to terminate the program
```sh
> echo "Hello, World!"
Hello, World!
```

## Supported commands:
| Command | Description |
| ------- | ----------- |
| `echo`  | Takes 1 argument and prints it. |
| `pwd`   | Takes no arguments and prints the current path. |
| `cd`    | Takes no arguments and changes the current path to the home directory.<br>Takes 1 argument which is ".." (e.g. `cd ..`) and changes the current directory to the previous directory.<br>Takes 1 argument which is either the full path or the relative (short) path and changes the current path to that path. |
| `ls`    | Takes no arguments and lists the contents of the current directory sorted alphabetically. |
| `ls -r` | Takes no arguments and lists the contents of the current directory in reverse order. |
| `mkdir` | - Takes 1 or more arguments and creates a directory for each argument. Each argument can be a directory name (in this case, the new directory is created in the current directory) or a path (full/short) that ends with a directory name (in this case, the new directory is created in the given path). |
| `rmdir` | - Takes 1 argument which is "*" (e.g. `rmdir *`) and removes all the empty directories in the current directory.
           - Takes 1 argument which is either the full path or the relative (short) path and removes the given directory only if it is empty. |
| `touch` | Takes 1 argument which is either the full path or the relative (short) path that ends with a file name and creates this file. |
| `cp`    | - Takes 2 arguments, both are files, and copies the first onto the second.
           - `cp -r` Takes 2 arguments, both are directories (empty or not) and copies the first directory (with all its content) into the second one. |
| `rm`    | Takes 1 argument which is a file name that exists in the current directory and removes this file. |
| `cat`   | - Takes 1 argument and prints the file's content.
           - Takes 2 arguments and concatenates the content of the 2 files and prints it. |
| `wc`    | Displays the number of lines, words, characters with spaces, and file name.
           Example: `wc file.txt` (Output: 9 79 483 file.txt) |
| `>`     | Redirects the output of the first command to be written to a file. If the file doesn't exist, it will be created. If the file exists, its original content will be replaced. |
| `>>`    | Like `>`, but appends to the file if it exists. |
| `history` | Takes no parameters and displays an enumerated list with the commands you've used in the past. Example: `history` |

