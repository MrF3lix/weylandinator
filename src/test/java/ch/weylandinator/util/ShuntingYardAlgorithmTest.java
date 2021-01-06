package ch.weylandinator.util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ShuntingYardAlgorithmTest
{
    
    ShuntingYardAlgorithm shuntingYardAlgorithm = new ShuntingYardAlgorithm();
    
    @BeforeEach
    void setUp()
    {
    }

    @Test
    void sum()
    {
        assertEquals("A = B C +",shuntingYardAlgorithm.shuntingYard("A = B + C"));
    }
    
    @Test
    void difference()
    {
        assertEquals("A = B C -",shuntingYardAlgorithm.shuntingYard("A = B - C"));
    }

    @Test
    void product()
    {
        assertEquals("A = B C *",shuntingYardAlgorithm.shuntingYard("A = B * C"));
    }

    @Test
    void quotient()
    {
        assertEquals("A = B C /",shuntingYardAlgorithm.shuntingYard("A = B / C"));
    }

    @Test
    void sumAndProduct()
    {
        assertEquals("A = B C *",shuntingYardAlgorithm.shuntingYard("A = B + C * D"));
    }

    @Test
    void sumAndProduct2()
    {
        assertEquals("A = B C *",shuntingYardAlgorithm.shuntingYard("A = B * C + D"));
    }

    @Test
    void sumAndQuotient()
    {
        assertEquals("A = B C /",shuntingYardAlgorithm.shuntingYard("A = B + C / D"));
    }
    
    @Test
    void sumAndQuotientMore()
    {
        assertEquals("A = B C /",shuntingYardAlgorithm.shuntingYard("A + B / (C - D)"));
    }


    @Test
    void sumAndQuotientMore2()
    {
        assertEquals("A = B C /",shuntingYardAlgorithm.shuntingYard("A + B / (C - D) + E * F"));
    }
}