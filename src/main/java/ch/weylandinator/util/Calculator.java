package ch.weylandinator.util;

import java.util.Map;
/**
 * Greek Symobls
 * <ul>
 *     <li>Sigma: {@value SIGMA_L}</li>
 *     <li>Omega: {@value OMEGA_L}</li>
 *     <li>Rho: {@value RHO_S}</li>
 * </ul>
 * <a href="https://unicode-table.com/de/">Unicode-Symbol-Search</a>
 * 
 */
public class Calculator
{
    public static final String SIGMA_L = "\u03A3";
    public static final String OMEGA_L = "\u03A9";
    public static final String RHO_S = "\u03C1";

    private static final String OPERATOREN = "=+-/*" + SIGMA_L;
    
    /**
     * 0 = Keine Angabe = Gesucht
     *
     * @param voltage_U Spannung
     * @param resistance_R Widerstand
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
    
    public double solve(Formula form, Map<String, Double> variableValueMap, String variable){
        int variableLocation = getVariableLocation(form, variable);
        int equalsLocation = getEqualsLocation(form);
        String dissolvedFormula = form.toString();
        
        if(variableLocation > equalsLocation)
        {
            dissolvedFormula = dissolveByVariable(dissolvedFormula, variable);
        }
        
        for (Map.Entry<String, Double> variableEntry : variableValueMap.entrySet())
        {
            form.replaceVariable(variableEntry.getKey(), variableEntry.getValue());
        }

        final Expression expression = new Expression(dissolvedFormula);
        return expression.evaluate().getValue();
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
        
        int ADDITIVE = 1;
        int MULTIPLICATIVE = 2;
        int EXPONENTIAL = 3;
        int FUNCTIONAL = 4;
  
        return "";
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
