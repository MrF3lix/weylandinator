package ch.weylandinator.util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class CalculatorTest
{

    Calculator calculator = new Calculator();
    EvaluateReversePolishNotation evaluateReversePolishNotation = new EvaluateReversePolishNotation();
    @BeforeEach
    void setUp()
    {
    }
    
    @Test
    void solveAdd(){
        String initialFormula = "1 = B + 3";
        String dissolvedFormula = calculator.dissolveByVariable(initialFormula, "B");

        System.out.println(initialFormula);
        assertEquals(-2.0, evaluateReversePolishNotation.solvePolishReverseNotation(prepareForSolve(dissolvedFormula)));
    }

    @Test
    void solveAddAndSubtract(){
        String initialFormula = "1 = B + 3 - 5";
        String dissolvedFormula = calculator.dissolveByVariable(initialFormula, "B");

        System.out.println(initialFormula);
        assertEquals(3.0, evaluateReversePolishNotation.solvePolishReverseNotation(prepareForSolve(dissolvedFormula)));
    }

    @Test
    @Disabled
    void solveAddWithMinusPrefix(){
        String initialFormula = "1 = - 11 + B + 3";
        String dissolvedFormula = calculator.dissolveByVariable(initialFormula, "B");

        System.out.println(initialFormula);
        assertEquals(9.0, evaluateReversePolishNotation.solvePolishReverseNotation(prepareForSolve(dissolvedFormula)));
    }

    @Test
    void solveSubtract(){
        String initialFormula = "1 = B - 3";
        String dissolvedFormula = calculator.dissolveByVariable(initialFormula, "B");

        System.out.println(initialFormula);
        assertEquals(4d, evaluateReversePolishNotation.solvePolishReverseNotation(prepareForSolve(dissolvedFormula)));
    }

    @Test
    void solveMultiply(){
        String initialFormula = "1 = B * 3";
        String dissolvedFormula = calculator.dissolveByVariable(initialFormula, "B");

        System.out.println(initialFormula);
        assertEquals((1.0/3.0), evaluateReversePolishNotation.solvePolishReverseNotation(prepareForSolve(dissolvedFormula)));
    }

    @Test
    void solveDivide(){
        String initialFormula = "4 = B / 3";
        String dissolvedFormula = calculator.dissolveByVariable(initialFormula, "B");

        System.out.println(initialFormula);
        assertEquals(12, evaluateReversePolishNotation.solvePolishReverseNotation(prepareForSolve(dissolvedFormula)));
    }

    @Test
   @Disabled
    void solveDivideByVariable(){
        String initialFormula = "4 = 24 / B";
        String dissolvedFormula = calculator.dissolveByVariable(initialFormula, "B");

        System.out.println(initialFormula);
        assertEquals(6, evaluateReversePolishNotation.solvePolishReverseNotation(prepareForSolve(dissolvedFormula)));
    }
    
    
    private String prepareForSolve(String dissolvedFormula){
        System.out.println(dissolvedFormula);
        return dissolvedFormula.split("=")[1].trim();
    }
    
}