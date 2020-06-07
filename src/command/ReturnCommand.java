package command;

import shuntingYard.ShuntingYardAlgorithem;

/**
 * Command : return expression
 * Execute : setting return value to be expression solution
 */
public class ReturnCommand implements Command {

	@Override
	public int execute(Interpreter i, int index) {
		int counter;
		StringBuilder expression = new StringBuilder();
		for(counter = 1; index + counter < i.code.size(); counter++) {//Building the equation
			String s = i.code.get(index+counter);
			if((Interpreter.cMap.containsKey(s))) {// If we reached the next command
				counter--;
				break; 
			}
			String[] split = s.split(String.format("((?<=%1$s)|(?=%1$s))","[-=+/*]"));
			for(String s2 : split) {
				if(i.varTable.containsKey(s2))  // If s is a variable
					expression.append(i.varTable.get(s2).toString());
				else expression.append(s2);
			}
		}
		double result = ShuntingYardAlgorithem.calc(expression.toString());// Solving the expression
		i.setReturnValue((int)result);
	return counter;
	}

}
