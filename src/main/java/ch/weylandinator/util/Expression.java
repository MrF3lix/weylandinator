package ch.weylandinator.util;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.exceptions.ImproperParenthesesException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;
import ch.weylandinator.util.token.Token;
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

    //ShuntingYard replacement
    public Expression(
        final String expressionString) throws ExpressionConverter.ConversionException, ImproperParenthesesException, ExpressionTokenizer.UnrecognizedCharacterException, ExpressionTokenizer.UnrecognizedOperatorException
    {
        originalExpression = expressionString;

        if (containsAssignement(originalExpression)) {
            //Split between assignment
            //tokenize left and right separately

            List<Token> tokensLeft  = ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString));
            List<Token> tokensright = ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString));

            //dissolvedExpression = dissolveExpression(originalExpression);
        }

        tokens =
            Collections.unmodifiableList(ExpressionConverter.convert(ExpressionTokenizer.tokenize(expressionString)));
    }

    public String dissolveExpression(List<Token> tokenListWithVar, List<Token> tokenListWithoutVar)
    {
        while(variableIsNotIsolated(tokenListWithVar, tokenListWithoutVar))
        {
            Operator mainOperation = (Operator) tokenListWithVar.get(tokenListWithVar.size() - 1);
            List<List<Token>> tokenGroupList = getTokenGroupList(tokenListWithVar);
            tokenListWithVar.clear();

            for (List<Token> tokenList : tokenGroupList){
                List<Token> tokenListToAdd = tokenList;
                if(!containsVariable(tokenList)){
                    Operator operator = (Operator) tokenList.get(tokenList.size() - 1);

                    if(operator.getPriority() != (mainOperation).getPriority())
                    {
                        //add MainOperation
                        tokenListToAdd.add(OperatorMap.INSTANCE.getFor(mainOperation.getSymbol()));
                    }

                    invertLast(tokenListToAdd);
                    addTokens(tokenListWithoutVar, tokenListToAdd);
                }
                else{
                    addTokens(tokenListWithVar, tokenListToAdd);
                }

            }
        }
        
        if(variableHasOperator(tokenListWithVar))
        {
           //TODO 
        }
        
        return "";
    }

    private boolean variableHasOperator(List<Token> tokenListWithVar)
    {
        //TODO
        return false;
    }

    private boolean variableIsNotIsolated(List<Token> tokenListWithVar, List<Token> tokenListWithoutVar)
    {
        //TODO
        return false;
    }

    private void addTokens(List<Token> tokenList, List<Token> tokenListToBeAdded)
    {
        tokenList.addAll(tokenListToBeAdded);
    }
    
    private void invertLast(List<Token> tokenListToAdd){
        tokenListToAdd.set(tokenListToAdd.size() - 1, ((Operator) tokenListToAdd.get(tokenListToAdd.size() - 1)).getInvertedOperation());
    }

    private boolean containsVariable(List<Token> tokenList)
    {
        for (Token token : tokenList){
            if(token instanceof Variable){
                return true;
            }
        }
        return false;
    }

    private List<List<Token>> getTokenGroupList(List<Token> tokenListWithVar)
    {
        List<List<Token>> tokenGroupList = new ArrayList<>();

        int distanceUntilBlockEnd = 0;
        int blockEnd = tokenListWithVar.size() - 1;

        for (int i = tokenListWithVar.size() - 1; i >= 0; i--) {
            if (tokenListWithVar.get(i) instanceof Operand) {
                distanceUntilBlockEnd++;
            } else {
                distanceUntilBlockEnd--;
            }
            
            if(distanceUntilBlockEnd == 0){
                tokenGroupList.add(tokenListWithVar.subList(i, blockEnd));
                blockEnd = i - 1;
            }
        }
        return tokenGroupList;
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
