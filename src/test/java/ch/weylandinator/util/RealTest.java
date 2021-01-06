package ch.weylandinator.util;

import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.operands.Real;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class RealTest {
    private static final int COUNT = 100;
    private final Random random = new Random();

    @Test
    public void testGetValue() throws Exception {
        for (int i = 0; i < COUNT; i++) {
            double value = random.nextDouble();
            Real operand = new Real(value);
            assertEquals(operand.getValue(), value);
        }
    }

    @Test
    public void testToString() throws Exception {
        for (int i = 0; i < COUNT; i++) {
            double value = random.nextDouble();
            Real operand = new Real(value);
            assertEquals(operand.toString(), String.format("%.2f", value));
        }
    }

}