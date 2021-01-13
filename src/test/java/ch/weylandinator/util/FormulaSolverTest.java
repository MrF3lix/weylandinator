package ch.weylandinator.util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class FormulaSolverTest
{
    FormulaSolver formulaSolver = new FormulaSolver();


    @Test
    @Disabled
    void solveAdd(){
        String initialFormula = "1 = B + 3";

        System.out.println(initialFormula);
        assertEquals(-2.0, formulaSolver.solveFormula(initialFormula, "B"));
    }


    @Test
    @Disabled
    void solveAddAndSubtract(){
        String initialFormula = "1 = B + 3 - 5";
        System.out.println(initialFormula);
        assertEquals(3.0, formulaSolver.solveFormula(initialFormula, "B"));
    }

    @Test
    @Disabled
    void solveAddWithMinusPrefix(){
        String initialFormula = "1 = - 11 + B + 3";
        System.out.println(initialFormula);
        assertEquals(9.0, formulaSolver.solveFormula(initialFormula, "B"));
    }

    @Test
    @Disabled
    void solveSubtract(){
        String initialFormula = "1 = B - 3";
        System.out.println(initialFormula);
        assertEquals(4.0, formulaSolver.solveFormula(initialFormula, "B"));
    }

    @Test
    @Disabled
    void solveMultiply(){
        String initialFormula = "1 = B * 3";
        System.out.println(initialFormula);
        assertEquals((1.0/3.0), formulaSolver.solveFormula(initialFormula, "B"));
    }

    @Test
    @Disabled
    void solveDivide(){
        String initialFormula = "4 = B / 3";
        System.out.println(initialFormula);
        assertEquals(12.0, formulaSolver.solveFormula(initialFormula, "B"));
    }

    @Test
    @Disabled
    void solveDivideByVariable(){
        String initialFormula = "4 = 24 / B";
        System.out.println(initialFormula);
        assertEquals(6.0, formulaSolver.solveFormula(initialFormula, "B"));
    }
    
}