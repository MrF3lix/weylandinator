package ch.weylandinator.util;

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
    
    public int solve(Formula form, String variable){
        int variableLocation = getVariableLocation(form, variable);
        int equalsLocation = getEqualsLocation(form);
        String dissolvedFormula = form.toString();
        
        if(variableLocation > equalsLocation){
            dissolvedFormula = dissolveByVariable(dissolvedFormula, variable);
        }
        
        return 0;
    }

    private String dissolveByVariable(String form, String variable)
    {
        return "";
        //return new ShuntingYardAlgorithm().shuntingYard(form);
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
