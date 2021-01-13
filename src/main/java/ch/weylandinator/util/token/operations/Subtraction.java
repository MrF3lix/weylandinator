package ch.weylandinator.util.token.operations;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Subtraction extends Operator {
    public static final Subtraction INSTANCE = new Subtraction();

    private Subtraction() {
        super("-", 1, ADDITIVE, Addition.INSTANCE);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].add(operands[1]);
    }
}
