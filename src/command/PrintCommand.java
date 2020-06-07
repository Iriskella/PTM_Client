package command;

/**
 * Command : print string
 * Execute : print the string
 */
public class PrintCommand implements Command {
	
	@Override
	public int execute(Interpreter i, int index) {
		String sToPrint = i.code.get(index +1);
		if(i.varTable.containsKey(sToPrint))
			System.out.println(i.varTable.get(sToPrint));
		else System.out.println(sToPrint);
		return 1;
	}

}
