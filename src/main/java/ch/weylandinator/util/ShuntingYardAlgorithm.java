package ch.weylandinator.util;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
public class ShuntingYardAlgorithm
{
    private final Map<String, Operator> operatorMap = new HashMap<String, Operator>()
    {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
    }};


    public String shuntingYard(String infix)
    {
        StringBuilder output = new StringBuilder();
        Stack<String> stack = new Stack<String>();

        for (String token : infix.split("")) {
            //operator
            if (operatorMap.containsKey(token)) {
                while (!stack.isEmpty() && isHigherPrec(token, stack.peek())) {
                    output.append(stack.pop()).append(' ');
                }
                stack.push(token);
            }
            //left parenthesis
            else if (token.equals("(")) {
                stack.push(token);
            }
            //right parenthesis
            else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    output.append(stack.pop()).append(' ');
                }
                stack.pop();
            }
            //digit
            else {
                output.append(token).append(' ');
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(' ');
        }

        return output.toString();
    }

    private boolean isHigherPrec(String op, String sub)
    {
        return (operatorMap.containsKey(sub) && operatorMap.get(sub).precedence >= operatorMap.get(op).precedence);
    }

    private enum Operator
    {
        ADD(1),
        SUBTRACT(2),
        MULTIPLY(3),
        DIVIDE(4)
        ;

        final int precedence;

        Operator(int p)
        {
            precedence = p;
        }
    }
}