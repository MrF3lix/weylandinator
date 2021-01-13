package ch.weylandinator.util.token.operations;

import ch.weylandinator.util.exceptions.ArityException;
import ch.weylandinator.util.token.Operand;
import ch.weylandinator.util.token.Operator;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Multiplication extends Operator {
    public static final Multiplication INSTANCE = new Multiplication();

    private Multiplication() {
        super("*", 2, MULTIPLICATIVE, Division.INSTANCE);
    }

    @Override
    public Operand evaluate(Operand... operands) throws ArityException {
        check(operands.length);
        return operands[0].multiply(operands[1]);
    }
}
