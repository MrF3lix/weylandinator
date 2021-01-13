package ch.weylandinator.util;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.weylandinator.util.BasicOperator.*;

enum BasicOperator
{
    ASSIGN("=", 0),
    ADD("+", 1),
    SUBTRACT("-", 1),
    MULTIPLY("*", 2),
    DIVIDE("/", 2),
    ;

    private final String operator;
    private final int    grade;

    BasicOperator(String operator, int grade)
    {
        this.operator = operator;
        this.grade = grade;
    }

    public String getOperator()
    {
        return operator;
    }

    public int getGrade()
    {
        return grade;
    }
    
}

public class FormulaSolver
{
    private static final String              POSITIV_OPERATORS = ADD.getOperator() + MULTIPLY.getOperator();
    private static final String              NEGATIV_OPERATORS = SUBTRACT.getOperator() + DIVIDE.getOperator();
    private static final String              DELIMITER         = " ";
    private static final Map<String, String> OPERATOR_INVERTER =
        Map.of(ADD.getOperator(), SUBTRACT.getOperator(), SUBTRACT.getOperator(), ADD.getOperator(), MULTIPLY
            .getOperator(), DIVIDE.getOperator(), DIVIDE.getOperator(), MULTIPLY.getOperator());

    private final EvaluateReversePolishNotation evaluateReversePolishNotation = new EvaluateReversePolishNotation();

    public FormulaSolver()
    {

    }

    public double solveFormula(String formula, String variable)
    {
        formula = dissolveFormula(formula, variable).split(ASSIGN.getOperator())[1].trim();
        return evaluateReversePolishNotation.solvePolishReverseNotation(formula);
    }

    public String dissolveFormula(String formula, String variable)
    {
        boolean isFinished = false;
        String shuntingYardFormat = getShuntingYardFormat(formula);
        FormulaTuple formulaTuple = getFormulaTuple(shuntingYardFormat, variable);
        
        while (!isFinished) 
        {
            if (isVariableIsolated(arrayListToString(formulaTuple.getWithVar()), variable)) {
                if (operationIsPositiv(formulaTuple.getWithVar())) {
                    isFinished = true;
                } else {
                    //applyInvertedOperation(sideWithVariable, sideWithOutVariable);
                }
            } else {
                List<List<String>> operationList = getOperationList(formulaTuple.getWithVar());

                for (List<String> operation : operationList) {
                    if (!containsVariable(arrayListToString(operation), variable)) {
                        List<String> invertedOperation = invertOperator(operation);
                        formulaTuple.appendToWithoutVar(invertedOperation);
                    } else {
                        formulaTuple.setWithVar(operation);
                    }
                }
            }
        }

        return formulaTuple.toString();
    }

    private FormulaTuple getFormulaTuple(String formula, String variable)
    {
        String[] splitFormula     = splitFormula(formula);
        int      variableLocation = getVariableLocation(splitFormula, variable);

        List<String> sideWithVariable    = Arrays.asList(splitFormula[variableLocation].split(DELIMITER));
        List<String> sideWithOutVariable = Arrays.asList(splitFormula[1 - variableLocation].split(DELIMITER));

        return new FormulaTuple(sideWithVariable, sideWithOutVariable);
    }

    public boolean operationIsPositiv(List<String> sideWithVariable)
    {
        String lastElement = getLastElement(sideWithVariable);
        return lastElement.equals(ADD.getOperator()) || lastElement.equals(MULTIPLY.getOperator());
    }

    private String getLastElement(List<String> list){
        return list.get(list.size() - 1);
    }
    
    private List<String> invertOperator(List<String> operation)
    {
        operation.set(operation.size() - 1, getInvertedOperator(getLastElement(operation)));
        return operation;
    }
    
    public BasicOperator getOperatorByString(String operator){
        for (BasicOperator basicOperator : BasicOperator.values()){
            if(operator.equals(basicOperator.getOperator())){
                return basicOperator;
            }
        }
        return null;
    }
/*
    private List<String[]> getOperationList(String[] sideWithVariable)
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

    private List<List<String>> getOperationList(List<String> sideWithVariable)
    {
        String mainOperator = getMainOperator(sideWithVariable);

        List<List<String>> operationList = new ArrayList<>();
        int            operatorCount = 1;
        boolean        wasOperator   = true;

        StringBuilder currentOperation = new StringBuilder(mainOperator);

        int currentBlockStart = 0;
        
        int currentBlockEnd = sideWithVariable.size() - 1;
        int startSearchAt = currentBlockEnd - 1;
        
        for (int i = startSearchAt; i >= 0; i--) 
        {
            String currentElement = sideWithVariable.get(i); 
            
            if (isVariableOrValue(currentElement))
            {
                int steps = currentBlockEnd - i;
                currentBlockStart = i - steps + 1;
                operationList.add(getOperationBlock(sideWithVariable, currentBlockStart, currentBlockEnd));
            }
            else if (isOperatorWithSameGrade(currentElement, mainOperator)){
                
            } 
            else {
                
            }
            
            
            if (isOperator(currentElement)) {
                if (!wasOperator) {
                    operationList.add(splitOperation(currentOperation));
                    currentOperation = new StringBuilder();
                }
                currentOperation.insert(0, currentElement + DELIMITER);
                operatorCount++;
                wasOperator = true;
            } else {

                if (operatorCount == 0) {
                    operationList.add(splitOperation(currentOperation));
                    String operatorToAppend = mainOperator;
                    if (mainOperator.equals(SUBTRACT.getOperator()) || mainOperator.equals(DIVIDE.getOperator())) {
                        operatorToAppend = OPERATOR_INVERTER.get(operatorToAppend);
                    }
                    
                    operationList.add(Arrays.asList(currentElement, operatorToAppend));

                    currentOperation = new StringBuilder();
                } else {

                    currentOperation.insert(0, currentElement + DELIMITER);
                }
                operatorCount--;
                wasOperator = false;
            }
        }
        return operationList;
    }

    private List<String> splitOperation(StringBuilder operation){
        return Arrays.asList(operation.toString().trim().split(DELIMITER));
    }
    
    private boolean isOperatorWithSameGrade(String currentElement, String mainOperator)
    {
        BasicOperator operatorMain = getOperatorByString(mainOperator);
        
        if(isOperator(currentElement)){
            BasicOperator operatorCurrent = getOperatorByString(currentElement);
            return operatorCurrent.getGrade() == operatorMain.getGrade();
        }
        
        return false;
    }

    private boolean isVariableOrValue(String currentElement)
    {
        return isVariable(currentElement) || isValue(currentElement);
    }

    private String getInvertedOperator(String operator)
    {
        return OPERATOR_INVERTER.get(operator);
    }

    private List<String> getOperationBlock(List<String> formula, int currentBlockStart, int currentBlockEnd)
    {
        return formula.subList(currentBlockStart, currentBlockEnd);
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

    private String getMainOperator(List<String> partialFormula)
    {
        return getLastElement(partialFormula);
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
    
    private boolean isVariable(String string){
        String pattern = ".*[a-zA-Z].*";
        return Pattern.compile(pattern).matcher(string).matches();
    }

    private boolean isValue(String string){
        String pattern = ".*[0-9].*";
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

    private <T> T[] concatenateArrays(T[] first, T[] second)
    {
        int aLen = first.length;
        int bLen = second.length;

        @SuppressWarnings("unchecked") T[] newArray =
            (T[]) Array.newInstance(first.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(first, 0, newArray, 0, aLen);
        System.arraycopy(second, 0, newArray, aLen, bLen);

        return newArray;
    }

    private List<String> combineLists(List<String> first, List<String> second)
    {
        return Stream.concat(first.stream(), second.stream()).collect(Collectors.toList());
    }

    private String arrayToString(String[] array)
    {
        String string = Arrays.toString(array).replaceAll("[\\[\\],]", " ");
        return StringOperation.removeDuplicateSpaces(string);
    }

    private String arrayListToString(List<String> array)
    {
        String string = array.toString().replaceAll("[\\[\\],]", " ");
        return StringOperation.removeDuplicateSpaces(string);
    }
}

class FormulaTuple
{
    private List<String> withVar;
    private List<String> withoutVar;

    FormulaTuple(List<String> sideWithVariable, List<String> sideWithoutVariable)
    {
        this.withVar = sideWithVariable;
        this.withoutVar = sideWithoutVariable;
    }

    public void switchSides(){
        List<String> saveSideWithVariable = withVar;
        withVar = withoutVar;
        withoutVar = withVar;
    }

    public List<String> getWithoutVar()
    {
        return withoutVar;
    }

    public List<String> getWithVar()
    {
        return withVar;
    }

    public void setWithoutVar(List<String> withoutVar)
    {
        this.withoutVar = withoutVar;
    }

    public void setWithVar(List<String> withVar)
    {
        this.withVar = withVar;
    }
    
    public void appendToWithoutVar(List<String> listToAppend){
        this.withoutVar.addAll(listToAppend);
    }

    @Override
    public String toString()
    {
        return withVar.toString() + " " + ASSIGN.getOperator() + " " + withoutVar.toString();
    }
}
