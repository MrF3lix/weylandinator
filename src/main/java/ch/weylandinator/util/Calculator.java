package ch.weylandinator.util;

import ch.weylandinator.model.CircuitElement;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.weylandinator.util.StandardOperator.*;

enum StandardOperator
{
    ADD("+", true), SUBTRACT("-", false), MULTIPLY("*", true), DIVIDE("/", false);

    private final String operator;
    private final boolean isPositivOperator;

    StandardOperator(String operator, boolean isPositivOperator)
    {
        this.operator = operator;
        this.isPositivOperator = isPositivOperator;
    }

    public String getOperator()
    {
        return operator;
    }

    public boolean isPositivOperator()
    {
        return isPositivOperator;
    }
}

/**
 * Greek Symobls
 * <ul>
 *     <li>Sigma: {@value SIGMA_L}</li>
 *     <li>Omega: {@value OMEGA_L}</li>
 *     <li>Rho: {@value RHO_S}</li>
 * </ul>
 * <a href="https://unicode-table.com/de/">Unicode-Symbol-Search</a>
 */
public class Calculator
{
    public static final String SIGMA_L = "\u03A3";
    public static final String OMEGA_L = "\u03A9";
    public static final String RHO_S = "\u03C1";
    private static final Map<String, String> OPERATOR_INVERTER = Map.of("+", "-", "-", "+", "*", "/", "/", "*");

    public List<CircuitElement> solveCircuit(List<CircuitElement> elementList)
    {

        for (CircuitElement element : elementList) {

            double result;

            Formula formula = Formula.URI;
            Map<String, Double> variableMap = new HashMap<>();

            if (!hasDefaultValue(element.getResistance())) {
                variableMap.put("R", (double) element.getResistance());
            } else {
                formula = Formula.URI_2;
            }

            if (!hasDefaultValue(element.getCurrent())) {
                variableMap.put("I", (double) element.getCurrent());
            } else {
                formula = Formula.URI_3;
            }

            if (!hasDefaultValue(element.getVoltage())) {
                variableMap.put("U", (double) element.getVoltage());
            } else {
                formula = Formula.URI;
            }

            if (hasExactlyOneUnknown(formula, variableMap)) {

                result = solve(formula, variableMap, "TODO");
            }
        }

        return null;
    }

    public double solve(Formula form, Map<String, Double> variableValueMap, String variable)
    {
        int variableLocation = getVariableLocation(form, variable);
        int equalsLocation = getEqualsLocation(form);
        
        /*
        String dissolvedFormula = form.toString();
        
        if(variableLocation > equalsLocation)
        {
            dissolvedFormula = dissolveByVariable(dissolvedFormula, variable);
        }*/

        for (Map.Entry<String, Double> variableEntry : variableValueMap.entrySet()) {
            form.replaceVariable(variableEntry.getKey(), variableEntry.getValue());
        }

        final Expression expression = new Expression(form.getContent());
        return expression.evaluate().getValue();
    }

    public String dissolveByVariable(String form, String variable)
    {
        //3 = 2 + X     ---> - Summand
        //3 = 10 - X    ---> + X
        //21 = 3 * X    ---> / Faktor
        //3 = 30 / X    ---> * X

        //3 = 2 + X * 2 + 4     ---> [2] [X*2] [4] - Summanden  

        //3 = 2 + 5 / (2 - X) + 4   ---> [2] [5/(2-X)] [4] - Summanden    ---> 3         = 2 5 2 X - / + 4 +
        //2 + 3 + 4 =  5 / (2 - X)    ---> [2] [5/(2-X)] [4] - Summanden  ---> 2 3 4 + + = 5 2 X - /
        //(2 - X) = 5 / (2 + 3 + 4)  ---> [2] [5/(2-X)] [4] - Summanden   ---> 2 X -     = 5 2 3 4 + + /

        //3 = 3 * (X + 2)         ---> [3] [X+2] - Faktoren ---> 3 X 2 + *
        //3 / 3 = X + 2         ---> [X] [2] - Summanden ---> 3 3 / = X 2 +

        // 1. Ist Unbekannte Links vom Gleich Zeichen?
        // -> Wenn Rechts 
        // 2. Split by Space / Delimiter
        // 3. [Value] [Operator] 
        // 4. Prüfen ob Hauptoperation Summe oder Produkt

        ShuntingYardAlgorithm shuntingYardAlgorithm = new ShuntingYardAlgorithm();

        String[] equationLeftRight = StringOperation.removeDuplicateSpaces(form).split("=");
        String left = equationLeftRight[0];
        String right = equationLeftRight[1];
        String[] formulaLeft =
            StringOperation.removeDuplicateSpaces(shuntingYardAlgorithm.shuntingYard(left)).split(" ");
        String[] formulaRight =
            StringOperation.removeDuplicateSpaces(shuntingYardAlgorithm.shuntingYard(right)).split(" ");

        FormulaTuple formulaTuple = new FormulaTuple(left, right);

        while (!variableIsIsolatedWithPositivOperation(formulaTuple.formulatLeft, variable) &&
               !variableIsIsolatedWithPositivOperation(formulaTuple.formulaRight, variable)) {

            //TODO Check if Variable is negated or divisor
            if (variableIsIsolatedWitNegativOperators(formulaTuple.formulatLeft, variable) ||
                variableIsIsolatedWitNegativOperators(formulaTuple.formulaRight, variable))
            {
                formulaTuple = invertMainOperators(formulaTuple.formulatLeft, formulaTuple.formulaRight);
            }
            
            switch (getLast(formulaRight)) {
                case "+" -> formulaTuple = performInvertedOperation(formulaRight, formulaLeft, variable, "+");
                case "-" -> formulaTuple = performInvertedOperation(formulaRight, formulaLeft, variable, "-");
                case "/" -> formulaTuple = performInvertedOperation(formulaRight, formulaLeft, variable, "/");
                case "*" -> formulaTuple = performInvertedOperation(formulaRight, formulaLeft, variable, "*");
            }
        }

        if (variableIsIsolatedWithPositivOperation(formulaTuple.formulatLeft, variable)) {
            return removePositivOperators(formulaTuple.formulatLeft) + " = " + formulaTuple.formulaRight;
        } else {
            return removePositivOperators(formulaTuple.formulaRight) + " = " + formulaTuple.formulatLeft;
        }
    }

    //3 = 2 + 5 / (2 - X) + 4   ---> [2] [5/(2-X)] [4] - Summanden    ---> 3         = 2 5 2 X - / + 4 +
    //2 + 3 + 4 =  5 / (2 - X)    ---> [2] [5/(2-X)] [4] - Summanden  ---> 2 3 4 + + = 5 2 X - /
    //(2 - X) = 5 / (2 + 3 + 4)  ---> [2] [5/(2-X)] [4] - Summanden   ---> 2 X -     = 5 2 3 4 + + /
    private FormulaTuple performInvertedOperation(String[] formulaRight, String[] formulaLeft, String unknownVariable, String operator)
    {
        // Right to Left
        List<String> operationList = getOperationList(formulaRight, operator);

        //Subtract
        String formulaRightNew = "";
        String formulaLeftNew = String.join(" ", formulaLeft) + " ";
        for (String string : operationList) {
            if (containsVariable(string, unknownVariable)) {
                formulaRightNew = string;
            } else {
                formulaLeftNew += invertOperator(string, operator) + " ";
            }
        }

        formulaLeftNew = StringOperation.removeDuplicateSpaces(formulaLeftNew);
        return new FormulaTuple(formulaLeftNew, formulaRightNew);
    }

    private List<String> getOperationList(String[] formula, String operator)
    {
        List<String> operationList = new ArrayList<>();
        int operatorCount = 1;
        boolean wasOperator = true;

        //Summands -> ArrayList 1 = 3 + X / 2 ---> [3 +] [X 2 / +]
        // X 2 /
        // + <-> -
        // * <-> /
        String currentOperation = operator;
        for (int i = formula.length - 2; i >= 0; i--) {

            if (isOperator(formula[i])) {
                if (!wasOperator) {
                    operationList.add(currentOperation.trim());
                    currentOperation = "";
                }
                currentOperation = formula[i] + " " + currentOperation;
                operatorCount++;
                wasOperator = true;
            } else {

                if (operatorCount == 0) {
                    operationList.add(currentOperation.trim());
                    String operatorToAppend = operator;
                    if (operator.equals("-") || operator.equals("/")) {
                        operatorToAppend = OPERATOR_INVERTER.get(operatorToAppend);
                    }
                    operationList.add(formula[i] + " " + operatorToAppend);

                    currentOperation = "";
                } else {

                    currentOperation = formula[i] + " " + currentOperation;
                }
                operatorCount--;
                wasOperator = false;
            }
        }
        return operationList;
    }

    private FormulaTuple invertMainOperators(String formulatLeft, String formulaRight)
    {
        //TODO Negated -> Negate Non-Variable-Side / remove variable-Negation
        //TODO Negated -> Divide By Non-Variable-Side / Multiply by Variable(-Side)
        return null;
    }

    private boolean hasDefaultValue(double value)
    {
        return value == 0d;
    }

    private String removePositivOperators(String string)
    {
        return string.replaceAll("[" + ADD.getOperator() + MULTIPLY.getOperator() + "]", "").trim();
    }

    private String removeNegativOperators(String string)
    {
        return string.replaceAll("[" + SUBTRACT.getOperator() + DIVIDE.getOperator() + "]", "").trim();
    }

    private boolean variableIsIsolatedWithPositivOperation(String partialFormula, String variable)
    {
        if (partialFormula == null || partialFormula.isEmpty()) {
            return false;
        }

        return removePositivOperators(partialFormula).equals(variable);
    }

    private boolean variableIsIsolatedWitNegativOperators(String partialFormula, String variable)
    {
        if (partialFormula == null || partialFormula.isEmpty()) {
            return false;
        }

        return removeNegativOperators(partialFormula).equals(variable);
    }

    private boolean hasExactlyOneUnknown(Formula formula, Map<String, Double> variableMap)
    {
        return formula.numberOfVariables() == variableMap.size() + 1;
    }

    private String invertOperator(String string, String operator)
    {
        return string.replaceAll(MessageFormat.format("\\{0}", operator), getInvertedOperator(operator));
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

    private boolean isOperator(String string)
    {
        for (StandardOperator s : StandardOperator.values()) {
            if (s.getOperator().equals(string)) {
                return true;
            }
        }
        return false;
    }

    private String getInvertedOperator(String operator)
    {
        return OPERATOR_INVERTER.get(operator);
    }

    private String getLast(String[] array)
    {
        return array[array.length - 1];
    }

    private int getEqualsLocation(Formula formula)
    {
        return formula.toString().indexOf("=");
    }

    private int getVariableLocation(Formula formula, String resolveByVariable)
    {
        return formula.toString().indexOf(resolveByVariable);
    }
}

class FormulaTuple
{
    String formulatLeft;
    String formulaRight;

    FormulaTuple(String formulatLeft, String formulaRight)
    {
        this.formulatLeft = formulatLeft;
        this.formulaRight = formulaRight;
    }

    public String getFormulaRight()
    {
        return formulaRight;
    }

    public String getFormulatLeft()
    {
        return formulatLeft;
    }
}
