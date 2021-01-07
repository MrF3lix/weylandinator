package ch.weylandinator.util.functions;

import ch.weylandinator.util.token.Operand;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Subhomoy Haldar
 * @version 2017.05.12
 */
public class SquareRootTest {
    private final Random random = new Random();

    @Test
    public void testEvaluate() throws Exception {
        for (int i = 0; i < FunctionTestHelper.RUNS; i++) {
            Operand operand = FunctionTestHelper.getOperand(random).abs();
            double value = operand.getValue();
            double expected = Math.sqrt(value);
            double actual = operand.sqrt().getValue();
            assertEquals(actual, expected, FunctionTestHelper.EPS);
        }
    }
}