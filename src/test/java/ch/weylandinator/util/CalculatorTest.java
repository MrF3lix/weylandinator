package ch.weylandinator.util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class CalculatorTest
{

    Calculator calculator = new Calculator();
    @BeforeEach
    void setUp()
    {
    }
    
    @Test
    void solve(){
        FormulaTuple formulaTuple = calculator.dissolveByVariable("1 = B + 3", "A");
        System.out.println(formulaTuple.formulatLeft + " = " + formulaTuple.formulaRight);
        System.out.println(formulaTuple.formulaRight.substring(0,formulaTuple.formulaRight.length() - 1) + "= " + formulaTuple.formulatLeft);
        
        EvaluateReversePolishNotation evaluateReversePolishNotation = new EvaluateReversePolishNotation();
        int result = evaluateReversePolishNotation.solvePolishReverseNotation(formulaTuple.formulaRight + " = " + formulaTuple.formulatLeft);
        
        assertEquals(-2,result);
    }
    
}