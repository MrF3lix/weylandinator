
package ch.weylandinator.util;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.weylandinator.util.BasicOperator.*;


enum BasicOperator
{
    ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"), ASSIGN("=");

    private final String operator;

    BasicOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
}

public class FormulaSolver
{
    private static final String POSITIV_OPERATORS = ADD.getOperator() + MULTIPLY.getOperator();
    private static final String NEGATIV_OPERATORS = SUBTRACT.getOperator() + DIVIDE.getOperator();
    private static final String DELIMITER = " ";
    private static final Map<String, String> OPERATOR_INVERTER =
        Map.of(ADD.getOperator(), SUBTRACT.getOperator(), SUBTRACT.getOperator(), ADD.getOperator(), MULTIPLY.getOperator(), DIVIDE.getOperator(), DIVIDE.getOperator(), MULTIPLY.getOperator());
    
    private final EvaluateReversePolishNotation evaluateReversePolishNotation = new EvaluateReversePolishNotation();

    public FormulaSolver()
    {

    }

    public double solveFormula(String formula, String variable){
        formula = dissolveFormula(formula, variable).split(ASSIGN.getOperator())[1].trim();
        return evaluateReversePolishNotation.solvePolishReverseNotation(formula);
    }
    
    public String dissolveFormula(String formula, String variable)
    {
        boolean isFinished = false;
        formula = getShuntingYardFormat(formula);
        String[] splitFormula = splitFormula(formula);
        int variableLocation = getVariableLocation(splitFormula, variable);
        
        String[] sideWithVariable = splitFormula[variableLocation].split(DELIMITER);
        String[] sideWithOutVariable = splitFormula[1 - variableLocation].split(DELIMITER);
        
        while(!isFinished)
        {
            if (isVariableIsolated(arrayToString(sideWithVariable), variable)) {
                if(operationIsPositiv(sideWithVariable))
                {
                    isFinished = true;
                }
                else{
                    //applyInvertedOperation(sideWithVariable, sideWithOutVariable);
                }
            } else {
                List<String[]> operationList = getOperationList(sideWithVariable);

                for (String[] operation : operationList){
                    if(!containsVariable(arrayToString(operation),variable)){
                        String[] invertedOperation = invertOperator(operation);
                        sideWithOutVariable = concatenateArrays(sideWithOutVariable, invertedOperation);
                    }
                    else{
                        sideWithVariable = operation;
                    }
                }
            }
        }

        return sideWithVariable[0] + " " + ASSIGN.getOperator() + " " + arrayToString(sideWithOutVariable);
    }

    public boolean operationIsPositiv(String[] sideWithVariable)
    {
        return sideWithVariable[sideWithVariable.length - 1].equals(ADD.getOperator())
               || sideWithVariable[sideWithVariable.length - 1].equals(MULTIPLY.getOperator() );

    }

    private boolean isFinished(String[] sideWithVariable, String variable)
    {
        return (sideWithVariable.length == 1 && sideWithVariable[0].equals(variable));
    }

    private String[] invertOperator(String[] operation)
    {
        operation[operation.length - 1] = getInvertedOperator(operation[operation.length - 1]);
        return operation;
    }

   /* private List<String[]> getOperationList(String[] sideWithVariable)
    {
        List<String[]> operationList = new ArrayList<>();

        String mainOperation = getMainOperator(sideWithVariable);

        int currentBlockStart = 0;
        int currentBlockEnd = sideWithVariable.length;

        while(currentBlockEnd > 0)
        {
            currentBlockStart = getLastNonOperatorLocation(sideWithVariable, currentBlockEnd - 1);
            operationList.add(getOperationBlock(sideWithVariable, currentBlockStart, currentBlockEnd));
            currentBlockEnd = currentBlockStart - 1;
        }

        if(currentBlockEnd == 0){
            String operatorToBeAdded;
            if(mainOperation.equals(ADD.getOperator()) || mainOperation.equals(SUBTRACT.getOperator())){
                operatorToBeAdded = ADD.getOperator();
            }
            else{
                operatorToBeAdded = MULTIPLY.getOperator();
            }
            operationList.add(new String[] {sideWithVariable[0], operatorToBeAdded});
        }
        
        return operationList;
    }*/

    private List<String[]> getOperationList(String[] formula)
    {
        String mainOperation = getMainOperator(formula);
        
        List<String[]> operationList = new ArrayList<>();
        int operatorCount = 1;
        boolean wasOperator = true;

        //Summands -> ArrayList 1 = 3 + X / 2 ---> [3 +] [X 2 / +]
        // X 2 /
        // + <-> -
        // * <-> /
        StringBuilder currentOperation = new StringBuilder(mainOperation);
        for (int i = formula.length - 2; i >= 0; i--) {

            if (isOperator(formula[i])) {
                if (!wasOperator) {
                    operationList.add(currentOperation.toString().trim().split(DELIMITER));
                    currentOperation = new StringBuilder();
                }
                currentOperation.insert(0, formula[i] + " ");
                operatorCount++;
                wasOperator = true;
            } else {

                if (operatorCount == 0) {
                    operationList.add(currentOperation.toString().trim().split(DELIMITER));
                    String operatorToAppend = mainOperation;
                    if (mainOperation.equals("-") || mainOperation.equals("/")) {
                        operatorToAppend = OPERATOR_INVERTER.get(operatorToAppend);
                    }
                    operationList.add(new String[] {formula[i], operatorToAppend});

                    currentOperation = new StringBuilder();
                } else {

                    currentOperation.insert(0, formula[i] + " ");
                }
                operatorCount--;
                wasOperator = false;
            }
        }
        return operationList;
    }

    private String getInvertedOperator(String operator)
    {
        return OPERATOR_INVERTER.get(operator);
    }
    
    private String[] getOperationBlock(String[] formula, int currentBlockStart, int currentBlockEnd)
    {
        return Arrays.copyOfRange(formula, currentBlockStart, currentBlockEnd);
    }

    private int getLastNonOperatorLocation(String[] partialFormula, int startLocation)
    {

        for (int i = startLocation; i > 0; i--) {
            if (!isOperator(partialFormula[i])) {
                return i;
            }
        }

        return 0;
    }

    private String getMainOperator(String[] partialFormula)
    {
        return partialFormula[partialFormula.length - 1];
    }

    private boolean isVariableIsolated(String partialFormula, String variable)
    {
        return removeOperators(partialFormula).equals(variable);
    }

    private String removeOperators(String string)
    {
        return string.replaceAll("[" + POSITIV_OPERATORS + NEGATIV_OPERATORS + "]", "").trim();
    }

    private String[] splitFormula(String formula)
    {
        return formula.split(ASSIGN.getOperator());
    }

    private boolean isOperator(String string)
    {
        for (StandardOperator s : StandardOperator.values()) {
            if (s.getOperator().equals(string)) {
                return true;
            }
        }
        return false;
    }

    private int getVariableLocation(String[] splitFormula, String variable)
    {

        if (containsVariable(splitFormula[0], variable)) {
            return 0;
        } else if (containsVariable(splitFormula[1], variable)) {
            return 1;
        }

        return -1; //ACHTUNG!!!
    }

    private boolean containsVariable(String string, String unknownVariable)
    {
        String pattern;
        if (unknownVariable == null || unknownVariable.isEmpty()) {
            pattern = ".*[a-zA-Z].*";
        } else {
            pattern = ".*[" + unknownVariable + "].*";
        }
        return Pattern.compile(pattern).matcher(string).matches();
    }

    private String getShuntingYardFormat(String formula)
    {
        ShuntingYardAlgorithm shuntingYardAlgorithm = new ShuntingYardAlgorithm();

        String[] splitFormula = StringOperation.removeDuplicateSpaces(formula).split(ASSIGN.getOperator());

        String[] formulaLeft =
            StringOperation.removeDuplicateSpaces(shuntingYardAlgorithm.shuntingYard(splitFormula[0])).split(DELIMITER);
        String[] formulaRight =
            StringOperation.removeDuplicateSpaces(shuntingYardAlgorithm.shuntingYard(splitFormula[1])).split(DELIMITER);

        return arrayToString(formulaLeft) + ASSIGN.getOperator() + arrayToString(formulaRight);
    }

    private <T> T[] concatenateArrays(T[] first, T[] second) {
        int aLen = first.length;
        int bLen = second.length;

        @SuppressWarnings("unchecked")
        T[] newArray = (T[]) Array.newInstance(first.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(first, 0, newArray, 0, aLen);
        System.arraycopy(second, 0, newArray, aLen, bLen);

        return newArray;
    }
    
    private String arrayToString(String[] array){
        String string = Arrays.toString(array).replaceAll("[\\[\\],]", " ");
        return StringOperation.removeDuplicateSpaces(string);
    }
}
