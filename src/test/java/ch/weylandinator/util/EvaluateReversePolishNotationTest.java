package ch.weylandinator.util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class EvaluateReversePolishNotationTest
{

    EvaluateReversePolishNotation evaluateReversePolishNotation = new EvaluateReversePolishNotation();
    
    @BeforeEach
    void setUp()
    {
    }
    
    @Test
    void sum(){
        double result = evaluateReversePolishNotation.solvePolishReverseNotation("9 5 +");
        assertEquals(14, result, "9 + 5 = " + result);
    }

    @Test
    void difference(){
        double result = evaluateReversePolishNotation.solvePolishReverseNotation("12 2 -");
        assertEquals(10, result, "12 - 2 = " + result);
    }

    @Test
    void product(){
        double result = evaluateReversePolishNotation.solvePolishReverseNotation("3 7 *");
        assertEquals(21, result, "3 * 7 = " + result);
    }

    @Test
    void quotient(){
        double result = evaluateReversePolishNotation.solvePolishReverseNotation("36 9 /");
        assertEquals(4, result, "36 / 9 = " + result);
    }

    @Test
    @Disabled
    void quotientWithPrefix(){
        double result = evaluateReversePolishNotation.solvePolishReverseNotation("9 36 9 / +");
        assertEquals(4.0, result, "+ 36 / 9 = " + result);
    }
    
    @Test 
    void sumAndProduct(){
        double result = evaluateReversePolishNotation.solvePolishReverseNotation("3 5 + 2 *");
        assertEquals(16,  result, "(3 + 5) * 2 = " + result);
    }

    @Test
    void sumAndQuotient(){
        double result = evaluateReversePolishNotation.solvePolishReverseNotation("8 10 + 2 /");
        assertEquals(9,  result, "(8 + 10) / 2 = " + result);
    }
    
}