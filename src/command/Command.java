
package command;

public interface Command {
	
	/**
	 * Activate command
	 * 
	 * @param i Interpreter running the command
	 * @param index position in the code
	 */
	int execute(Interpreter i , int index);
}
