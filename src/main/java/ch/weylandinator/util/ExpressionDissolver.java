package ch.weylandinator.util;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;
import ch.weylandinator.util.token.Token;
import ch.weylandinator.util.token.operands.Real;
import ch.weylandinator.util.token.operands.Variable;
import ch.weylandinator.util.token.operations.Addition;
import ch.weylandinator.util.token.operations.Assignment;
import ch.weylandinator.util.token.operations.Multiplication;
import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.List;
public class ExpressionDissolver
{
    public ExpressionDissolver(){
        
    }

    public String dissolveExpression(List<Token> tokenListWithVar, List<Token> tokenListWithoutVar)
    {
        if(containsVariable(tokenListWithoutVar)){
            List<Token> tokenList = new ArrayList<>();
            tokenList = tokenListWithVar;
            tokenListWithVar = tokenListWithoutVar;
            tokenListWithoutVar = tokenList;
        }
        
        while(variableIsNotIsolated(tokenListWithVar))
        {
            Operator          mainOperation  = (Operator) tokenListWithVar.get(tokenListWithVar.size() - 1);
            List<List<Token>> tokenGroupList = getTokenGroupList(tokenListWithVar);
            tokenListWithVar.clear();

            List<List<Token>> tokenTest = new ArrayList<>();
            
            for (List<Token> tokenList : tokenGroupList) {
                tokenTest.add(tokenList);
                if (!containsVariable(tokenTest.get(0))) {
                    Operator operator = (Operator) tokenList.get(tokenList.size() - 1);

                    if (operator.getPriority() != (mainOperation).getPriority()) {
                        tokenList.add(OperatorMap.INSTANCE.getFor(mainOperation.getSymbol()));
                    }

                    invertLast(tokenList);
                    addTokens(tokenListWithoutVar, tokenList);
                } else {
                    addTokens(tokenListWithVar, tokenList);
                }
            }

        }

        if(variableHasOperator(tokenListWithVar))
        {
            if(onlyPositivOperation(tokenListWithVar)){
                removeOperators(tokenListWithVar);
            }
            else{
                try {
                    throw new ExecutionControl.NotImplementedException("Negativ Operator with isolated Variable");
                } catch (ExecutionControl.NotImplementedException e) {
                    e.printStackTrace();
                }
            }
        }

        return tokenListToString(tokenListWithVar) + Assignment.INSTANCE.getSymbol() + tokenListWithoutVar.toString();
    }

    private void removeOperators(List<Token> tokenListWithVar)
    {
        tokenListWithVar.removeIf(token -> token instanceof Operator);
    }

    private boolean onlyPositivOperation(List<Token> tokenListWithVar)
    {
        for (Token token : tokenListWithVar){
            if(!(token instanceof Addition) && !(token instanceof Multiplication)){
                return false;
            }
        }
        return true;
    }

    private boolean variableHasOperator(List<Token> tokenListWithVar)
    {
        for (Token token : tokenListWithVar){
            if(token instanceof Operator){
                return true;
            }
        }
        return false;
    }

    private boolean variableIsNotIsolated(List<Token> tokenListWithVar)
    {
        for (Token token : tokenListWithVar){
            if(token instanceof Real){
                return true;
            }
        }
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


    private String tokenListToString(List<Token> tokenList){
        return tokenList.toString();
    }

    
}
