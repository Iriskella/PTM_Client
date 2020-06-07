package command;

/**
 * Extends conditionCommand
 * Command : if {...}
 * Execute : execute if condition applay
 */
public class IfCommand extends ConditionCommand {

	@Override
	protected int conditionParser(String[] predicat) {
		int counter = 0;
		if(super.predictCheck(predicat) && Interpreter.isOn) {
			for(counter = super.counter1 ; super.index + counter < super.interpeter.code.size() ; counter++) {
				if(super.interpeter.code.get(counter).equals("}")) break;
				Command c = Interpreter.cMap.get(super.interpeter.code.get(super.index + counter));
				if(c != null) {
					counter += c.execute(super.interpeter,super.index + counter);
				}
			}	
		}
		return counter;
	}

}
