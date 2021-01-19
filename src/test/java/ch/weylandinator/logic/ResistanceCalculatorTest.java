package ch.weylandinator.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.weylandinator.ui.model.CircuitElement;
import ch.weylandinator.ui.model.CircuitElementType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResistanceCalculatorTest {
    ResistanceCalculator resistanceCalculator;
    CircuitElement rootElement;
    CircuitElement r1;
    CircuitElement r2;
    CircuitElement r3;


    
    @BeforeEach
    void setUp()
    {
        rootElement = new CircuitElement("#ROOT");
        resistanceCalculator = new ResistanceCalculator(rootElement);

        r1 = new CircuitElement();
        r1.setName("r1");
        r1.setType(CircuitElementType.RESISTOR);

        r2 = new CircuitElement();
        r2.setName("r2");
        r2.setType(CircuitElementType.RESISTOR);

        r3 = new CircuitElement();
        r3.setName("r3");
        r3.setType(CircuitElementType.RESISTOR);
    }

    /**
     * 2 resistors in serial
     */
    @Test
    void canCalculateSimpleSerialResistance() {
        r1.setResistance(500d);
        r2.setResistance(500d);

        r1.addChildElement(r2);
        rootElement.addChildElement(r1);

        double totalResistance = resistanceCalculator.getTotalResistance();

        assertEquals(1000d, totalResistance);
    }

    /**
     * 2 resistors in parallel
     */
    @Test
    void canCalculateSimpleParallelResistance1() {
        r1.setResistance(500d);
        r2.setResistance(500d);

        rootElement.addChildElement(r1);
        rootElement.addChildElement(r2);

        double totalResistance = resistanceCalculator.getTotalResistance();

        assertEquals(250d, totalResistance);
    }

    /**
     * 3 resistors in parallel
     */
    @Test
    void canCalculateSimpleParallelCircuit2() {
        //TODO; doesn't work yet.
        r1.setResistance(500d);
        r2.setResistance(500d);
        r3.setResistance(500d);

        rootElement.addChildElement(r3);
        rootElement.addChildElement(r2);
        rootElement.addChildElement(r1);

        double totalResistance = resistanceCalculator.getTotalResistance();

        assertEquals(500d/3, totalResistance);
    }

    /**
     * 1 resistor and then 2 resistors in parallel
     */
    @Test
    void canCalculateSimpleMixedCircuit1() {
        r1.setResistance(500d);
        r2.setResistance(500d);
        r3.setResistance(500d);

        r1.addChildElement(r3);
        r1.addChildElement(r2);
        rootElement.addChildElement(r1);

        double totalResistance = resistanceCalculator.getTotalResistance();

        assertEquals(750d, totalResistance);
    }

    /**
     * 2 resistor in parallel and then 1 resistor
     */
    @Test
    void canCalculateSimpleMixedCircuit2() {
        r1.setResistance(500d);
        r2.setResistance(500d);
        r3.setResistance(500d);

        r2.addChildElement(r3);
        rootElement.addChildElement(r2);
        rootElement.addChildElement(r1);

        double totalResistance = resistanceCalculator.getTotalResistance();

        assertEquals(1000d/3, totalResistance);
    }
}
