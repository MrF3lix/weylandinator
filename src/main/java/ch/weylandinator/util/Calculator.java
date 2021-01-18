package ch.weylandinator.util;

import ch.weylandinator.model.CircuitElement;

import java.util.HashMap;
import java.util.List;
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

        }

        return null;
    }

    public double solve(Formula form, Map<String, Double> variableValueMap, String variable)
    {

        for (Map.Entry<String, Double> variableEntry : variableValueMap.entrySet()) {
            form.replaceVariable(variableEntry.getKey(), variableEntry.getValue());
        }

        final Expression expression = new Expression(form.getContent());
        return expression.evaluate().getValue();
    }


    private boolean hasDefaultValue(double value)
    {
        return value == 0d;
    }

}

