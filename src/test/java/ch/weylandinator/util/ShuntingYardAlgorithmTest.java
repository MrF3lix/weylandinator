package ch.weylandinator.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
class ShuntingYardAlgorithmTest
{

    ShuntingYardAlgorithm shuntingYardAlgorithm = new ShuntingYardAlgorithm();
    
    Map<String, String> inputVSExpected = Map.of(
        "B + C + D",                "B C + D +"
        ,"B - C",                   "B C -"
        ,"B * C",                   "B C *"
        ,"B / C",                   "B C /"
        ,"B + C * D",               "B C D * +"
        ,"B + C / D",               "B C D / +"
        ,"A + B / (C - D)",         "A B C D - / +"
        ,"A + B / (C - D) + E * F", "A B C D - / + E F * +"
    );

    @BeforeEach
    void setUp()
    {
    }

    @Test
    void execAll()
    {
        for(Map.Entry<String, String> entry : inputVSExpected.entrySet())
        {
            assertEquals(entry.getValue(),execShuntingYard(entry.getKey()));
        }
    }
    
    private String execShuntingYard(String string){
        return StringOperation.removeDuplicateSpaces(shuntingYardAlgorithm.shuntingYard(string));
    }
}