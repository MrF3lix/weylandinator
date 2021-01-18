package ch.weylandinator.util;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.exceptions.ImproperParenthesesException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;
import ch.weylandinator.util.token.Token;
import ch.weylandinator.util.token.operands.Real;
import ch.weylandinator.util.token.operands.Variable;
import ch.weylandinator.util.token.operations.Assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Subhomoy Haldar
 * @version 2017.04.21
 */
public class Expression
{
    private final List<Token> tokens;

    private final String originalExpression;
    private       String dissolvedExpression;

    public Expression(final String expressionString) throws ExpressionConverter.ConversionException, ImproperParenthesesException, ExpressionTokenizer.UnrecognizedCharacterException, ExpressionTokenizer.UnrecognizedOperatorException
    {
        originalExpression = expressionString;

        if (containsAssignement(originalExpression)) {
            ExpressionDissolver expressionDissolver = new ExpressionDissolver();
            //Split between assignment
            //tokenize left and right separately

            List<Token> tokensLeft  = new ArrayList<>();
            for(Token token : ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString.split("=")[0]))){
                Token t = null;
                if(token instanceof Operator){
                    t = OperatorMap.INSTANCE.getFor(token.toString());
                }
                if(token instanceof Real){
                    t = new Real(((Real) token).getValue());
                }
                if(token instanceof Variable){
                    t = new Variable(((Variable) token).toString());
                }
                tokensLeft.add(t);
            }

            List<Token> tokensright  = new ArrayList<>();
            for(Token token : ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString.split("=")[1]))){
                Token t = null;
                if(token instanceof Operator){
                    t = OperatorMap.INSTANCE.getFor(token.toString());
                }
                if(token instanceof Real){
                    t = new Real(((Real) token).getValue());
                }
                if(token instanceof Variable){
                    t = new Variable(((Variable) token).toString());
                }
                tokensright.add(t);
            }
            
            dissolvedExpression = expressionDissolver.dissolveExpression(tokensLeft, tokensright);
        }

        tokens =
            Collections.unmodifiableList(ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString)));
    }

    public Operand evaluate() throws ArityException, ExpressionConverter.ConversionException
    {
        return ExpressionEvaluator.evaluate(tokens);
    }
    


    private boolean containsAssignement(String expression)
    {
        String pattern = ".*[" + Assignment.INSTANCE.getSymbol() + "].*";
        return Pattern.compile(pattern).matcher(expression).matches();
    }
    
}
