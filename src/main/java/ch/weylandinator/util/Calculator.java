package ch.weylandinator.util;

import ch.weylandinator.model.Element;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

    private static final String OPERATOREN = "+-/*" + SIGMA_L;

    /**
     * 0 = Keine Angabe = Gesucht
     *
     * @param voltage_U     Spannung
     * @param resistance_R  Widerstand
     * @param electricity_I Strom
     * @return Gesucht
     */
    public static int URI(int voltage_U, int resistance_R, int electricity_I)
    {
        if (voltage_U == 0) {
            voltage_U = resistance_R * electricity_I;
            return voltage_U;
        } else if (resistance_R == 0) {
            resistance_R = voltage_U / electricity_I;
            return resistance_R;
        } else if (electricity_I == 0) {
            electricity_I = voltage_U / resistance_R;
            return electricity_I;
        }
        return 0;
    }

    public List<Element> solveCircuit(List<Element> elementList)
    {

        for (Element element : elementList) {

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

    private boolean hasExactlyOneUnknown(Formula formula, Map<String, Double> variableMap)
    {
        return formula.numberOfVariables() == variableMap.size() + 1;
    }

    private boolean hasDefaultValue(double value)
    {
        return value == 0d;
    }

    private String dissolveByVariable(String form, String variable)
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

        String[] equationLeftRight = StringOperation.removeDuplicateSpaces(form).split("=");
        String left = equationLeftRight[0];
        String right = equationLeftRight[1];

        ShuntingYardAlgorithm shuntingYardAlgorithm = new ShuntingYardAlgorithm();
        String shuntingYardLeft = StringOperation.removeDuplicateSpaces(shuntingYardAlgorithm.shuntingYard(left));
        String shuntingYardRight = StringOperation.removeDuplicateSpaces(shuntingYardAlgorithm.shuntingYard(right));
        String[] formulaLeft = shuntingYardLeft.split(" ");
        String[] formulaRight = shuntingYardRight.split(" ");

        FormulaTuple formulaTuple;
        
        switch (getLast(formulaRight)) {
            case "+":
                formulaTuple = subtractAllExceptUnknown(formulaRight, formulaLeft);
                break;
            case "-":
                break;
            case "/":
                break;
            case "*":
                divideAllExceptUnknown(formulaRight);
                break;
        }

        return "";
    }

    private void divideAllExceptUnknown(String[] formulaRight)
    {

    }

    //3 = 2 + 5 / (2 - X) + 4   ---> [2] [5/(2-X)] [4] - Summanden    ---> 3         = 2 5 2 X - / + 4 +
    //2 + 3 + 4 =  5 / (2 - X)    ---> [2] [5/(2-X)] [4] - Summanden  ---> 2 3 4 + + = 5 2 X - /
    //(2 - X) = 5 / (2 + 3 + 4)  ---> [2] [5/(2-X)] [4] - Summanden   ---> 2 X -     = 5 2 3 4 + + /
    private FormulaTuple subtractAllExceptUnknown(String[] formulaRight, String[] formulaLeft)
    {
        // Right to Left
        List<String> summandList = new ArrayList<>();
        int operatorCount = 1;
        int startIndex = formulaRight.length - 1;
        boolean wasOperator = true;

        //Summands -> ArrayList
        String currentSummand = "+";
        for (int i = formulaRight.length - 2; i <= 0; i--) {

            if (isOperator(formulaRight[i])) {
                if (!wasOperator) {
                    summandList.add(currentSummand.trim());
                    currentSummand = "";
                }
                currentSummand = formulaRight[i] + " " + currentSummand;
                operatorCount++;
                wasOperator = true;
            } else {

                if (operatorCount == 0) {
                    summandList.add(currentSummand.trim());
                    summandList.add(formulaRight[i] + " +");

                    currentSummand = "";
                } else {

                    currentSummand = formulaRight[i] + " " + currentSummand;
                } operatorCount--;
                wasOperator = false;
            }
        }

        //Subtract
        String formulaRightNew = "";
        String formulaLeftNew = formulaLeft.toString();
        for (String string : summandList) {
            if (string.contains("[a-zA-Z]")) {
                formulaRightNew = string;
            } else {
                string.replaceAll("+", "-");
                formulaLeftNew += string;
            }
        }
        return new FormulaTuple(formulaLeftNew, formulaRightNew);
    }

    private boolean isOperator(String string)
    {
        return string.contains("[+-*/]");
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
