package command;

/**
 * Command : var varName 
 * execute : creat new varabile and insert it to the symboltable  
 */
public class DefineVarCommand implements Command {

	@Override
	public int execute(Interpreter i, int index) {
		String [] s = i.code.get(index + 1).split(String.format("((?<=%1$s)|(?=%1$s))","[-=+/*]"));// Fix code like this : var simx=34+2
		i.code.remove(index + 1);
		for(int j = s.length - 1 ; j >= 0 ; j--)
			i.code.add(index + 1,s[j]);
		i.varTable.put(i.code.get(index + 1), new Varible(0.0));
		return 1;
	}

}
