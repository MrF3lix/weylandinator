package ch.weylandinator.util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class FormulaSolverTest
{
    FormulaSolver formulaSolver = new FormulaSolver();
    
    @Test
    void operationIsPositiv(){
        assertTrue(formulaSolver.operationIsPositiv(new String[] {"X","+"}));
        
    }

}