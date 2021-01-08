package ch.weylandinator.util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    }
    
}