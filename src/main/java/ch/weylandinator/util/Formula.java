package ch.weylandinator.util;

import static ch.weylandinator.util.Calculator.SIGMA_L;

public enum Formula
{
    URI("U = R * I"),
    PUI("P = U * I"),
    KNOT("I_in = I_out1 + I_out2"),
    MESH("0 = " + SIGMA_L + "[I_n]"),
    R_SERIAL("R_s = R_1 + R_2"),
    R_PARALLEL("R_p = (R_1 * R_2) / (R_1 + R_2)"),
    R_TO_U_PROPORTION("U_2 = U_0 / (R_1 + R_2)")
    ;
    
    private String content;
    private int type; // 1 = Basic, 2 = Array...
    private int numberOfVariables; // 0 = infinity

    Formula(String content)
    {
        this.content = content;
    }
    
    public void replaceVariable(String variable, double value){
        content = content.replace(variable, String.valueOf(value));
    }

    public String getContent()
    {
        return content;
    }
}
