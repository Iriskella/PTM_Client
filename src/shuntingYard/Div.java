package shuntingYard;

import shuntingYard.Expression;

/**
 * calculate a binary expression left exression / right expression
 */
public class Div extends BinaryExpression {

	public Div(Expression left, Expression right) {
		super(left, right);
	}

	@Override
	public double calculate() {
		return left.calculate()/right.calculate();
	}

}