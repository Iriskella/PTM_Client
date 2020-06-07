package shuntingYard;

import shuntingYard.Expression;

/**
 * Represent a binary expression ==> left exression -- operator -- right expression
 */
public abstract class BinaryExpression implements Expression {
	protected Expression left,right;
	public BinaryExpression(Expression left,Expression right) {
		this.left=left;
		this.right=right;
	}

}