package shuntingYard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/**
 * Calculate infix String expression while using shuningYard algotirthem
 */
public class ShuntingYardAlgorithem {

    /**
     * Calculate infix String expression while using shuningYard algotirthem
     *
     * @param infixExpression infix expression
     * @return double result
     */
    public static double calc(String infixExpression) {

        double result = 0;

        //Define operators map by operator and its precedence
        HashMap<Character, Integer> operators = new HashMap<Character, Integer>();
        operators.put('+', 1);
        operators.put('-', 1);
        operators.put('*', 2);
        operators.put('/', 2);
        operators.put('(', 0);
        operators.put(')', 0);

        //Fix expression
        infixExpression = ShuntingYardAlgorithem.fixExpression(infixExpression, operators);

        //Create a postfix representation for the infix expression
        String postfixExpression = ShuntingYardAlgorithem.infixToPostfix(infixExpression, operators);

        //Calculate the postfix expression
        result = ShuntingYardAlgorithem.postfixToResult(postfixExpression, operators);

        System.out.println(result);
        return result;
    }

    /**
     * @param infixExpression infix expression
     * @param operators       map of operators an their precedence
     * @return fixed infix expression
     */
    private static String fixExpression(String infixExpression, HashMap<Character, Integer> operators) {

		infixExpression = infixExpression.replaceAll(" ","");
        StringBuilder output = new StringBuilder();
        ArrayList<String> tokenList = new ArrayList<String>(Arrays.asList(infixExpression.split("")));
        boolean flag = true;

        for (int i = 0; i < tokenList.size(); i++) { // For each token in the expression
            char token = tokenList.get(i).charAt(0);
            if (operators.containsKey(token) && token != '(' && token != ')') { //If token is an operator
                if (i == 0) { //First token is an operator
                    tokenList.add(0, "(0");
                    i += 2;
                    char next = tokenList.get(i).charAt(0);
                    if (i + 1 != tokenList.size())
                        while (!operators.containsKey(next) && i < tokenList.size()) { // until next operator
                            i++;
                            next = i < tokenList.size() ? tokenList.get(i).charAt(0) : next;
                        }
                    else i++;
                    tokenList.add(i, ")");
                    i++;
                } else {
                    char before = tokenList.get(i - 1).charAt(0);
                    if (operators.containsKey(before) && before != '(' && before != ')' ) { //2 operators in a row
                        if (token != '-')
                            tokenList.remove(i);
                        else {
                            tokenList.add(i, "(0");
                            i += 2;
                            char next = tokenList.get(i).charAt(0);
                            while (!operators.containsKey(next) && i < tokenList.size()) { // until next operator
                                i++;
                                next = i < tokenList.size() ? tokenList.get(i).charAt(0) : next;
                            }
                            tokenList.add(i, ")");
                            i++;
                        }
                    }
                }
            } else if (token == '(') flag = false;
            else if (token == ')') {
                if (flag)
                    tokenList.remove(i);
                else flag = true;
            }
        }

        tokenList.forEach((s) -> output.append(s));//Add fix expression to output

        return (flag) ? output.toString() : output.append(")").toString();
    }

    /**
     * Take an infix expression and translate it to a postfix expression using shuntingYard algorithem
     *
     * @param infixExpression infix expression
     * @param operators       map of operators an their precedence
     * @return postfix expression
     */
    private static String infixToPostfix(String infixExpression, HashMap<Character, Integer> operators) {

        StringBuilder output = new StringBuilder();
        Stack<Character> oStack = new Stack<Character>(); //Operator stack

        for (int i = 0; i < infixExpression.length(); i++) { //For each char
            char token = infixExpression.charAt(i);
            //Token = operator
            if (operators.containsKey(token) && token != '(' && token != ')') {
                while (!oStack.isEmpty() && operators.get(token) < operators.get(oStack.peek()))
                    output.append(oStack.pop()).append(' ');
                oStack.add(token);
            } else if (token == '(') {
                oStack.add(token);
            } else if (token == ')') {
                while (!oStack.isEmpty() && oStack.peek() != '(')
                    output.append(oStack.pop()).append(' ');
                oStack.pop();
            } else {
                //Token = number or part of a number
                output.append(token);
                if (i + 1 < infixExpression.length()) {
                    int j = 1;
                    char nextToken = infixExpression.charAt(i + j);
                    while (nextToken == '.' || Character.isDigit(nextToken)) {
                        output.append(nextToken);
                        j++;
                        if (i + j < infixExpression.length())
                            nextToken = infixExpression.charAt(i + j);
                        else break;
                    }
                    i += j - 1;
                }
                output.append(' ');
            }
        }
        while (!oStack.isEmpty()) // while oStack is not empty
            output.append(oStack.pop()).append(' ');
        return output.toString();//Return the postfix shuntingYard Build
    }

    /**
     * Calculate the postfix expression
     *
     * @param postfixExpression postfix expression
     * @param operators         map of operators an their precedence
     * @return double result
     */
    private static double postfixToResult(String postfixExpression, HashMap<Character, Integer> operators) {

        Stack<Expression> nStack = new Stack<Expression>();//Expression stack

        for (int i = 0; i < postfixExpression.length(); i++) {
            char token = postfixExpression.charAt(i);
            if (token == ' ') continue;
            if (operators.containsKey(token)) {//Token = operator
                if (nStack.isEmpty())  //Underflow throw
                    return 0;

                Expression right = nStack.pop(), left = nStack.pop();
                ;
                switch (token) {
                    case '+':
                        nStack.push(new Plus(left, right));//Add the result to the stack
                        break;
                    case '-':
                        nStack.push(new Minus(left, right));//Add the result to the stack
                        break;
                    case '/':
                        nStack.push(new Div(left, right));//Add the result to the stack
                        break;
                    case '*':
                        nStack.push(new Mul(left, right));//Add the result to the stack
                        break;
                }
            } else { //Token = Number or part of a number
                double num = 0, tempNum = 0;
                while (Character.isDigit(token)) {//Check if the next token is another digit
                    tempNum = tempNum * 10 + (int) (token - '0');//Build the number
                    i++;
                    token = postfixExpression.charAt(i);
                }
                num = tempNum;
                if (token == '.') {//Check if the number is deximal
                    tempNum = 0;
                    i++;
                    token = postfixExpression.charAt(i);
                    double j = 0.0;
                    while (Character.isDigit(token)) {//Check if the next token is another digit
                        tempNum = tempNum * 10 + (int) (token - '0');//Build the number
                        i++;
                        j++;
                        token = postfixExpression.charAt(i);
                    }
                    tempNum = (tempNum) / (Math.pow(10.0, j));
                    num += tempNum;//Add the decimal Part of the number to num
                }
                i--;
                nStack.push(new Number(num));
            }
        }
        double result = Math.floor((nStack.pop().calculate() * 1000)) / 1000; //Return the result round up to 3 numbers after the digit
        return result;
    }

    //-----------------------------------------------Class check-----------------------------------------------------------
    public static void main(String[] args) {

        double result = 0;

        //Define operators map by operator and its precedence
        HashMap<Character, Integer> operators = new HashMap<Character, Integer>();
        operators.put('+', 1);
        operators.put('-', 1);
        operators.put('*', 2);
        operators.put('/', 2);
        operators.put('(', 0);
        operators.put(')', 0);

        //A problem
        String problem = "(0-0)/20";

        //Fix the problem
        problem = ShuntingYardAlgorithem.fixExpression(problem, operators);
        System.out.println(problem);

        //Translate to postfix
        problem = ShuntingYardAlgorithem.infixToPostfix(problem, operators);
        System.out.println(problem);

        //Calculate
        result = ShuntingYardAlgorithem.postfixToResult(problem, operators);
        System.out.println(result);

        //Another Problem
        problem = "-41+56";

        //All Togther
        result = ShuntingYardAlgorithem.calc(problem);
        System.out.println(result);

    }
}

