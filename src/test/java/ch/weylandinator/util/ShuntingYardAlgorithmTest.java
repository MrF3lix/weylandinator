package ch.weylandinator.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class ShuntingYardAlgorithmTest
{

    ShuntingYardAlgorithm shuntingYardAlgorithm = new ShuntingYardAlgorithm();

    @BeforeEach
    void setUp()
    {
    }

    @Test
    @Disabled
    void sum()
    {
        assertEquals("A = B C +",shuntingYardAlgorithm.shuntingYard("A = B + C"));
    }

    @Test
    @Disabled
    void difference()
    {
        assertEquals("A = B C -",shuntingYardAlgorithm.shuntingYard("A = B - C"));
    }

    @Test
    @Disabled
    void product()
    {
        assertEquals("A = B C *",shuntingYardAlgorithm.shuntingYard("A = B * C"));
    }

    @Test
    @Disabled
    void quotient()
    {
        assertEquals("A = B C /",shuntingYardAlgorithm.shuntingYard("A = B / C"));
    }

    @Test
    @Disabled
    void sumAndProduct()
    {
        assertEquals("A = B C *",shuntingYardAlgorithm.shuntingYard("A = B + C * D"));
    }

    @Test
    @Disabled
    void sumAndProduct2()
    {
        assertEquals("A = B C *",shuntingYardAlgorithm.shuntingYard("A = B * C + D"));
    }

    @Test
    @Disabled
    void sumAndQuotient()
    {
        assertEquals("A = B C /",shuntingYardAlgorithm.shuntingYard("A = B + C / D"));
    }

    @Test
    @Disabled
    void sumAndQuotientMore()
    {
        assertEquals("A = B C /",shuntingYardAlgorithm.shuntingYard("A + B / (C - D)"));
    }
    
    @Test
    @Disabled
    void sumAndQuotientMore2()
    {
        //A B C D - / + E F * +     ---> [A] [B / (C - D)] [E * F]
        assertEquals("A = B C /",shuntingYardAlgorithm.shuntingYard("A + B / (C - D) + E * F"));
    }
}