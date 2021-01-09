package ch.weylandinator.util;

import java.util.Stack;

/**
 * Evaluate the value of an arithmetic expression in Reverse Polish Notation.
 * Valid operators are +, -, *, /. Each operand may be an integer or another expression.
 * <p/>
 * Some examples:
 * ["2", "1", "+", "3", "*"] -> ((2 + 1) * 3) -> 9
 * ["4", "13", "5", "/", "+"] -> (4 + (13 / 5)) -> 6
 */
public class EvaluateReversePolishNotation {

    /**
     * calculate the expression
     *
     * @param inputStrArr the input string which be split with char ','
     * @return the actually result
     * @throws Exception
     */
    private static double calcExpression(String[] inputStrArr) throws Exception {
        Stack<Double> stack = new Stack<>();

        double result = 0;

        for (String s : inputStrArr) {
            //if operator symbol then pop two stack element and do calculate
            char currentChar = s.charAt(0);
            if (isOperatorSymbol(currentChar)) {
                //Note: the first pop num is the right operate number
                double rightOperateNum = stack.pop();
                double leftOperateNum = stack.pop();
                double tmp = result = doCalcWithTwoNum(leftOperateNum, rightOperateNum, currentChar);

                stack.push(tmp);
            } else {
                stack.push(Double.valueOf(s));
            }
        }

        return result;
    }

    /**
     * judge the first char is a legal operator
     *
     * @param charStr the first char
     * @return if is one of +,-,*,/ return true then return false
     */
    private static boolean isOperatorSymbol(char charStr) {
        switch (charStr) {
            case '+':
            case '-':
            case '*':
            case '/':
                return true;
            default:
                return false;
        }
    }

    /**
     * do calculate with two operate number
     *
     * @param n1       left operate num
     * @param n2       right operate num
     * @param operator the operator. eg: +,-,*,/
     * @return the result after calculating
     * @throws Exception
     */
    private static double doCalcWithTwoNum(double n1, double n2, char operator) throws Exception {
        switch (operator) {
            case '+':
                return n1 + n2;
            case '-':
                return n1 - n2;
            case '*':
                return n1 * n2;
            case '/':
                return n1 / n2;
            default:
                throw new Exception("can not do calculate, the operator is illegal");
        }
    }


    public double solvePolishReverseNotation(String inputStr){
        
        String[] inputStrArr = inputStr.split(" ");
        
        double result = 0;
        try {
            result = calcExpression(inputStrArr);
            System.out.print("the result is : " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static void main(String[] args) {
        if (args.length < 1)
            throw new IllegalArgumentException("there must be a input value");

        //test case 1:
        //test with string: 4,13,5,/,+,4,13,5,/,+,+
        //result is: 12

        //test case 2:
        //test with string: 2,1,+,3,*
        //result is: 9
        String inputStr = args[0];
        System.out.printf("the input string is : %s%n", inputStr);

        String[] inputStrArr = inputStr.split(",");

        if (inputStrArr.length < 3)
            throw new IllegalArgumentException("there should be more than 3 args");

        double result = 0;
        try {
            result = calcExpression(inputStrArr);
            System.out.printf("the result is : %f", result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}